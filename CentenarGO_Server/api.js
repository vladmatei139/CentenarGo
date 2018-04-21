const express    = require('express');
const config = require('./config');
const { Client } = require('pg');
const client = new Client({
    user: config.postgresUser,
    password: config.postgresPassword,
    host: config.postgresHost,
    database: config.postgresDatabase,
    port: config.postgresPort,
    ssl: true
});
client.connect().catch(err => {console.error(err.stack);});
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');

const router = express.Router(); 

const errorCatcher = fn => (req, res, next) => {
    Promise.resolve(fn(req, res, next)).catch(next); 
};

router.post('/signup', errorCatcher(async (req, res) => {
    hash = await bcrypt.hash(req.body.password, 10);
    const result = await client.query(`INSERT INTO users (username, email, password) 
                        VALUES ($1::text, $2::text, $3::text)
						RETURNING id`, 
                        [req.body.username, req.body.email, hash]);

	await client.query(`INSERT INTO userdetails (lastname, firstname, userid, currentroute) 
                        VALUES ($1::text, $2::text, $3::uuid, 1)`, 
                        [req.body.lastname, req.body.firstname, result.rows[0].id]);

	await client.query(`INSERT INTO userroutes (routeid, datecompleted, currentlandmark, userid)
						VALUES (1, null, 1, $1::uuid)`,
						[result.rows[0].id])

    res.sendStatus(200);
}));

router.post('/login', errorCatcher(async (req, res) => {
    const { rows } = await client.query(`SELECT id as id, password AS hash 
                                         FROM users 
                                         WHERE email = $1::text`, [req.body.email])
    if (rows.length === 0) {
        res.status(404).send('User does not exist.');
        return;
    }
    same = await bcrypt.compare(req.body.password, rows[0].hash);
    if (same) {
        token = await jwt.sign({data: rows[0].id}, config.tokenSecret, {expiresIn: 30 * 24 * 60}); 
        res.status(200).json({token: token});
        return;
    }
    res.sendStatus(401);
}));

router.use((req, res, next) => {

    let token = req.body.token || req.query.token || req.headers['x-access-token'];
    console.log('Token-ul este ' + token);
    if (token) {
        jwt.verify(token, config.tokenSecret, (err, decoded) => {
            if (err) {
                if (err.message.toLowerCase().includes('expired')) {
                    res.status(401).send('Token expired');
                    return;
                }
                res.sendStatus(401);
                return;
            } 
            req.id = decoded.id;
            console.log('ID TOKEN: ' + req.id);
            next();
        });
    }
    else {
        res.sendStatus(401);
    }
});

router.post('/routes', errorCatcher(async (req, res, next) => {
    /**
     * Rutele sunt aceleasi pentru toti userii logati. 
     * Se intorc numele "name" si coordonatele ("beginLatitude", "beginLongitude") ale primului obiectiv al fiecarei rute care are cel putin un obiectiv.
     */
    const { rows } = await client.query(`SELECT r.id as id, r.name as name, l.latitude as beginLatitude, l.longitude as beginLongitude
                                         FROM routes r
                                         JOIN landmarks l ON l.route = r.id
                                         WHERE l.routeorder = 1`);
    if (rows.length === 0) {
        res.status(500).send('No routes.');
        return;
    }
    res.status(200).json({routes: rows});
}));

router.post('/route/:routeId', errorCatcher(async (req, res, next) => {
    /**
     * Se extrag obiectivele pentru ruta ceruta.
     * Daca ruta ceruta nu este cea curenta, se intoarce doar primul obiectiv.
     * Altfel, se intorc toate obiectivele pana la cel curent inclusiv.
     */
    const { rows } = await client.query(`SELECT l.id, l.name, l.latitude, l.longitude, l.routeorder as routerorder
                                          FROM landmarks l
                                          WHERE l.route = $1::int
										  ORDER BY routeorder`, [req.params.routeId]);
    if (rows.length === 0) {
        res.status(500).send('Route has no landmarks or does not exist.');
        return;
    }
    res.status(200).send({landmarks: rows});
}));

router.post('/landmark/:landmarkId', errorCatcher(async (req, res, next) => {
    /**
     * Se intoarce obiectivul cerut din ruta curenta, cu toate datele din tabel.
     * Daca obiectivul este cel curent, content-ul se trunchiaza la 100 de caractere.
     */
    
    console.log(`Id-ul este: ` + req.id);

    const { rows } = await client.query(`SELECT l.id, l.name, l.content, l.route, l.latitude, l.longitude, l.routeorder, LEAST(1, COALESCE(ur.currentlandmark, 0)) as is_current
                                         FROM landmarks l
                                         JOIN routes r ON r.id = l.route
                                         JOIN userdetails ud ON ud.currentroute = r.id 
                                         LEFT JOIN userroutes ur ON ur.currentlandmark = l.id
                                         WHERE ud.userid = $1::uuid 
                                         AND l.id = $2::int
                                         AND l.routeorder <= (
                                             SELECT routeorder
                                             FROM landmarks
                                             JOIN userroutes ON landmarks.route = userroutes.routeid 
                                             WHERE id = userroutes.currentlandmark
                                             AND userid = $1::uuid)`, [req.id, req.params.landmarkId]);


    if (rows.length === 0) {
        res.status(400).send('Landmark part of the current route, but more advanced than the current landmark, is not part of the current route, or does not exist.');
        return;
    }
    
    landmark = rows[0];
    landmark.is_current = landmark.is_current > 0;
    if (landmark.is_current) {
        landmark.content = landmark.content.substr(0, Math.min(landmark.content.length, 100));
    }
    res.status(200).json({landmark: landmark});
}));

router.post('/route/change/:routeId', errorCatcher(async (req, res, next) => {
    /**
     * Schimba ruta curenta.
     */
    await client.query('BEGIN');
    await client.query(`UPDATE userroutes
                        SET routeid = $2::int,
                            currentlandmark = (
                                SELECT id
                                FROM landmarks
                                WHERE route = $2::int
                                AND routeorder = 1
                            )
                        WHERE userid = $1::uuid
                        AND routeid = (
                            SELECT currentroute
                            FROM userdetails
                            WHERE userid = $1::uuid)`, [req.id, req.params.routeId]);
    await client.query(`UPDATE userdetails
                        SET currentroute = $2::int
                        WHERE userid = $1::uuid`, [req.id, req.params.routeId]);
    await client.query('COMMIT');
    res.sendStatus(200);
}));

router.use((err, req, res, next) => {
    console.error(err);
    client.query('ROLLBACK')
        .then(() => {
            res.sendStatus(500);
        })
        .catch(err => {
            console.error(err);
            //ROLLBACK error... R.I.P.
            res.sendStatus(500);
        });
});

module.exports = {
    config: config,
    client: client,
    bcrypt: bcrypt,
    router: router
};