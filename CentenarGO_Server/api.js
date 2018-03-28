const express    = require('express');
const config = require('./config');
const { Client } = require('pg');
const client = new Client({
    user: config.postgresUser,
    password: config.postgresPassword,
    host: config.postgresHost,
    database: config.postgresDatabase,
    port: config.postgresPort
});
client.connect();
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');

const router = express.Router(); 

router.post('/signup', (req, res) => {
    bcrypt.hash(req.body.password, 10)
        .then((hash) => {
            client.query(`INSERT INTO users (username, email, password) 
                          VALUES ($1::text, $2::text, $3::text)`, 
                          [req.body.username, req.body.email, hash])
                .then(() => {
                    res.sendStatus(200);
                })
                .catch((err) => {
                    console.log(err.stack);
                    if (err.message.toLowerCase().includes('duplicate')) {
                        res.status(400).send('Duplicate primary key in User table (email already exists).');
                        return;
                    }
                    else {
                        res.sendStatus(500);
                    }
                });
        })
        .catch((err) => {
            console.log(err.stack);
            res.sendStatus(500);
        });
});

router.post('/login', (req, res) => {
    client.query(`SELECT id as id, password AS hash 
                  FROM users 
                  WHERE email = $1::text`, [req.body.email])
        .then((result) => {
            if (result.rows.length === 0) {
                res.status(404).send('User does not exist.');
                return;
            }
            hash = result.rows[0].hash;
            bcrypt.compare(req.body.password, hash)
                .then((same) => {
                    res.status(200);
                    jwt.sign({id: result.rows[0].id}, config.tokenSecret, {expiresIn: 30 * 24 * 60}, (err, token) => {
                        if (err) {
                            console.log(err.stack);
                            res.sendStatus(500);
                            return;
                        }
                        res.json({token: token});
                    });
                })
                .catch((err) => {
                    console.log(err.stack);
                    res.sendStatus(401);
                });
        })
        .catch((err) => {
            console.log(err.stack);
            res.sendStatus(500);
        });
});

router.use((req, res, next) => {

    let token = req.body.token || req.query.token || req.headers['x-access-token'];

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
            next();
        });
    }
    else {
        res.sendStatus(401);
    }
});

router.post('/routes', (req, res, next) => {
    /**
     * Rutele sunt aceleasi pentru toti userii logati. 
     * Se intorc numele "name" si coordonatele ("beginLatitude", "beginLongitude") ale primului obiectiv al fiecarei rute care are cel putin un obiectiv.
     */
    client.query(`SELECT r.id as id, r.name as name, l.latitude as beginLatitude, l.longitude as beginLongitude
                  FROM routes r
                  JOIN landmarks l ON l.route = r.id
                  WHERE l.routeorder = 1`)
        .then((result) => {
            if (result.rows.length === 0) {
                res.status(500).send('User has no routes');
                return;
            }
            res.status(200).json({routes: result.rows});
        })
        .catch((err) => {
            console.error(err.stack);
            res.sendStatus(500);
        });
});

router.post('/route/:routeId/landmarks', (req, res, next) => {
    /**
     * Se extrag obiectivele pentru ruta ceruta.
     * Daca ruta ceruta nu este cea curenta, se intoarce doar primul obiectiv.
     * Altfel, se intorc toate obiectivele pana la cel curent inclusiv.
     */
    client.query(`(WITH tbl1 AS 
                   (
                    SELECT l.id AS id, l.name AS name, l.latitude AS latitude, l.longitude AS longitude, l.routeorder AS routeorder
                    FROM landmarks l
                    JOIN routes r ON r.id = l.route
                    JOIN userdetails ud ON r.id = ud.currentroute
                    WHERE ud.userid = $1::uuid 
                    AND r.id = $2::int
                   )
                   SELECT id, name, latitude, longitude, routeorder, (routeorder = (SELECT MAX(routeorder) FROM tbl1)) AS is_current
                   FROM tbl1)
                  UNION
                  (SELECT l.id, l.name, l.latitude, l.longitude, l.routeorder, false as is_current 
                   FROM landmarks l
                   JOIN routes r ON r.id = l.route
                   JOIN userdetails ud ON ud.userid = $1::uuid
                   WHERE r.id = $2::int
                   AND l.routeorder = 1)
                  ORDER BY 5 ASC`, [req.id, req.params.routeId])
        .then(result => {
            if (result.rows.length === 0) {
                res.status(500).send('Route has no landmarks');
                return;
            }
            res.status(200).json({landmarks: result.rows});
        })
        .catch(err => {
            console.error(err.stack);
            res.sendStatus(500);
        });
});

router.post('/landmark/:landmarkId', (req, res, next) => {
    /**
     * Se intoarce obiectivul cerut din ruta curenta, cu toate datele din tabel.
     * Daca obiectivul este cel curent, content-ul se trunchiaza la 100 de caractere.
     */
    client.query(`SELECT l.id, l.name, l.content, l.route, l.latitude, l.longitude, l.routeorder, LEAST(1, COALESCE(ur.currentlandmark, 0)) as is_current
                  FROM landmarks l
                  JOIN routes r ON r.id = l.route
                  JOIN userdetails ud ON ud.currentroute = r.id 
                  LEFT JOIN userroutes ur ON ur.currentlandmark = l.id
                  WHERE ud.userid = $1::uuid 
                  AND l.id = $2::int`, [req.id, req.params.landmarkId])
        .then(result => {
            if (result.rows.length === 0) {
                res.status(500).send('Landmark does not exist or is not part of the current route.');
                return;
            }
            landmark = result.rows[0];
            landmark.is_current = landmark.is_current > 0;
            if (landmark.is_current) {
                landmark.content = landmark.content.substr(0, Math.min(landmark.content.length, 100));
            }
            res.status(200).json({landmark: landmark});
        })
        .catch(err => {
            console.error(err.stack);
            res.sendStatus(500);
        });
});

module.exports = {
    config: config,
    client: client,
    bcrypt: bcrypt,
    router: router
};