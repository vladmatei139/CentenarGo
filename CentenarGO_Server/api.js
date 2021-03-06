const express    = require('express');
const fs         = require('fs');
const uuidv1     = require('uuid/v1')
const base64Img = require('base64-img');
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

const path = require('path');
const dir = path.join(__dirname, 'public');
const images = path.join(__dirname, 'images');

const errorCatcher = fn => (req, res, next) => {
    Promise.resolve(fn(req, res, next)).catch(next); 
};

router.post('/signup', errorCatcher(async (req, res) => {
    hash = await bcrypt.hash(req.body.password, 10);
    const { rows } = await client.query(`SELECT 1
                                         FROM users
                                         WHERE email = $1::text`, [req.body.email]);
    if (rows.length === 0) {
        const result = await client.query(`INSERT INTO users (username, email, password) 
                VALUES ($1::text, $2::text, $3::text)
                                        RETURNING id`, 
                [req.body.username, req.body.email, hash]);

        await client.query(`INSERT INTO userdetails (lastname,  firstname, userid) 
                VALUES ($1::text, $2::text, $3::uuid)`, 
                [req.body.lastname, req.body.firstname, result.rows[0].id]);
        res.sendStatus(200);

    }
    else {
        res.status(400).send('User already exists.');
    }

	/*await client.query(`INSERT INTO userroutes (routeid, datecompleted, currentlandmark, userid)
						VALUES (1, null, 1, $1::uuid)`,
						[result.rows[0].id])*/
    
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
        token = await jwt.sign({id: rows[0].id}, config.tokenSecret, {expiresIn: 30 * 24 * 60}); 
        res.status(200).json({token: token});
        return;
    }
    res.sendStatus(401);
}));

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

router.post('/routes', errorCatcher(async (req, res, next) => {
    /**
     * Rutele sunt aceleasi pentru toti userii logati. 
     * Se intorc numele "name" si coordonatele ("beginLatitude", "beginLongitude") ale primului obiectiv al fiecarei rute care are cel putin un obiectiv.
     */
    const { rows } = await client.query(`SELECT r.id as id, r.name as name,
											(CASE 
												WHEN datecompleted IS NULL THEN 0
												ELSE 1
											END) as completed, l.latitude as beginLatitude, l.longitude as beginLongitude
                                         FROM routes r
                                         JOIN landmarks l ON l.route = r.id
										 LEFT JOIN userroutes ur
										 ON ur.routeid = r.id
										 AND ur.userid = $1::uuid
                                         WHERE l.routeorder = 1`, [req.id]);
    if (rows.length === 0) {
        res.status(500).send('No routes.');
        return;
    }
    res.status(200).json({routes: rows});
}));

router.post('/getCurrentInfo/:routeId', errorCatcher(async (req, res, next) => {
	/**
	 * Se ia ruta curenta pentru un anumit user
	 */
	
	const { rows } = await client.query(`SELECT currentroute
                                         FROM userdetails
                                         WHERE userid = $1::uuid`, [req.id]);
    if (rows.length === 0) {
        res.status(500).send('No current route.');
        return;
    }
	let currentRoute = {currentroute: rows[0].currentroute}.currentroute;
	
	let rowsCurrentCompleted = await client.query(`SELECT 1
									 FROM userroutes
									 WHERE userid = $1::uuid
									 AND routeid = $2::int
									 AND datecompleted IS NOT NULL`, [req.id, currentRoute]); 
	
	let rowsNewCompleted = await client.query(`SELECT 1
									 FROM userroutes
									 WHERE userid = $1::uuid
									 AND routeid = $2::int
									 AND datecompleted IS NOT NULL`, [req.id, req.params.routeId]); 
	
	res.status(200).json({currentroute: rows[0].currentroute, currentCompleted: rowsCurrentCompleted.rows.length, newCompleted: rowsNewCompleted.rows.length});
}));

router.post('/routeLoad/:routeId', errorCatcher(async (req, res, next) => {
    /**
     * Se extrag obiectivele pentru ruta ceruta.
     * Daca ruta ceruta nu este cea curenta, se intoarce doar primul obiectiv.
     * Altfel, se intorc toate obiectivele pana la cel curent inclusiv.
     */
	
	let rowsCurrentLandmark = await client.query(`SELECT currentlandmark
									 FROM userroutes r
									 WHERE userid = $1::uuid
									 AND routeid = $2::int`, [req.id, req.params.routeId]); 
	if (rowsCurrentLandmark.rows.length === 0) {
        res.status(500).send('No current landmark.');
        return;
    }
	
	let currentLandmark = {currentlandmark: rowsCurrentLandmark.rows[0].currentlandmark }.currentlandmark;
	
    let rowsLandmarks = await client.query(`SELECT l.id, l.name, l.latitude, l.longitude, l.routeorder as routerorder
                                          FROM landmarks l
                                          WHERE l.route = $1::int
										  AND routeorder <= 
											(SELECT routeOrder
											FROM landmarks
											WHERE id = $2::int)
										  ORDER BY routeorder DESC`, [req.params.routeId, currentLandmark]);

    if (rowsLandmarks.rows.length === 0) {
        res.status(500).send('Route has no landmarks or does not exist.');
        return;
    }
    res.status(200).send({landmarks: rowsLandmarks.rows});
}));

router.post('/landmark', errorCatcher(async (req, res, next) => {
    /**
     * Se intoarce obiectivul cerut din ruta curenta, cu toate datele din tabel.
     * Daca obiectivul este cel curent, content-ul se trunchiaza la 100 de caractere.
     */
    const { rows } = await client.query(`SELECT l.id, l.name, l.content, l.route, l.latitude, l.longitude, l.routeorder, 
                        COALESCE((SELECT 1
				FROM userroutes ur
				WHERE datecompleted is NULL
				AND ur.userId = $1::uuid
				AND ur.routeId = $3::int
				AND l.routeorder >= (SELECT routeorder
									FROM landmarks
									WHERE ur.currentlandmark = id)), 0) as is_current
                                         FROM landmarks l
                                         WHERE l.id = $2::int`, [req.id, req.body.landmarkId, req.body.routeId]);


    if (rows.length === 0) {
        res.status(400).send('Landmark part of the current route, but more advanced than the current landmark, is not part of the current route, or does not exist.');
        return;
    }
    
    landmark = rows[0];
    landmark.is_current = landmark.is_current > 0;
    if (landmark.is_current) {
        landmark.content = landmark.content.substr(0, Math.min(landmark.content.length, 100));
    }
    res.status(200).json({landmark: landmark, image: landmark.name + '.jpg'});
}));

router.post('/landmark/:landmarkId/upload', errorCatcher(async (req, res, next) => {
    var imageFile = req.body.image;

    imageName = uuidv1();
    imageName = req.body.landmarkId + imageName;
    base64Img.img('data:image/png;base64,' + imageFile, "./images", imageName, function(err) {
    if (err)
      return res.status(500).send(err);
  	});
    await client.query(`INSERT INTO images(landmarkId, title, path, userid)
                        VALUES ($1::int, $2::text, $3::text, $4::uuid)
                        ON CONFLICT DO NOTHING`, [req.body.landmarkId, req.body.title, imageName, req.id]);
    await client.query('COMMIT');
 
    res.status(201).send('File uploaded!');

}));

router.use(express.static(images));

router.post('/landmarkContent/:landmarkId', errorCatcher(async (req, res, next) => {
    /**
     * Se intoarce contentul obiectivului cerut din ruta curenta.
     */
    const { rows } = await client.query(`SELECT content
                                         FROM landmarks
                                         WHERE id = $1::int`, [req.params.landmarkId]);

    if (rows.length === 0) {
        res.status(400).send('Inexistent landmark.');
        return;
    }
    res.status(200).json({content: rows[0].content});
}));

router.use(express.static(dir));

router.post('/route/change/:routeId', errorCatcher(async (req, res, next) => {
    /**
     * Schimba ruta curenta.
     */
	await client.query('BEGIN');
	const { rows } = await client.query(`SELECT 1
                                         FROM userdetails
                                         WHERE userid = $1::uuid
										 AND currentroute IS NOT NULL`, [req.id]);
    if (rows.length === 0) {
		await client.query(`UPDATE userdetails
							SET currentroute = $2::int
							WHERE userid = $1::uuid`, [req.id, req.params.routeId]);
		
		let rowsLandmark = await client.query(`SELECT id
												FROM landmarks
												WHERE route = $1::int
												ORDER BY routeorder
												LIMIT 1`, [req.params.routeId]);
		if (rowsLandmark.rows.length === 0) {
			res.status(500).send('No first landmark.');
			return;
		}
		
		let firstLandmark = {firstlandmark: rowsLandmark.rows[0].id }.firstlandmark;		
		
		await client.query(`INSERT INTO userroutes(routeid, currentlandmark, userid)
							VALUES($3::int, $2::int, $1::uuid)`, [req.id, firstLandmark, req.params.routeId]);
    
	}
	else {
		let rowsRoute = await client.query(`SELECT routeid
											FROM userroutes
											WHERE routeid = $1::int
											AND userid = $2::uuid`, [req.params.routeId, req.id]);
		if (rowsRoute.rows.length === 0) {
			let rowsLandmark = await client.query(`SELECT id
												FROM landmarks
												WHERE route = $1::int
												ORDER BY routeorder
												LIMIT 1`, [req.params.routeId]);
			if (rowsLandmark.rows.length === 0) {
				res.status(500).send('No first landmark.');
				return;
			}
			
			let firstLandmark = {firstlandmark: rowsLandmark.rows[0].id }.firstlandmark;		
			
			await client.query(`INSERT INTO userroutes(routeid, currentlandmark, userid)
							VALUES($3::int, $2::int, $1::uuid)`, [req.id, firstLandmark, req.params.routeId]);
		}
		else {
			await client.query(`UPDATE userroutes
								SET currentlandmark = (
										SELECT id
										FROM landmarks
										WHERE route = (
											SELECT currentroute
											FROM userdetails
											WHERE userid = $1::uuid)
										AND routeorder = 1),
                                                                    locationchecked = FALSE
								WHERE userid = $1::uuid
								AND routeid = (
									SELECT currentroute
									FROM userdetails
									WHERE userid = $1::uuid)
								AND datecompleted IS NULL`, [req.id]);
									
			await client.query(`UPDATE userroutes
								SET currentlandmark = (
										SELECT id
										FROM landmarks
										WHERE route = $2::int
										AND routeorder = 1
									),
                                                                    locationchecked = FALSE
								WHERE userid = $1::uuid
								AND routeid = $2::int
								AND datecompleted IS NULL`, [req.id, req.params.routeId]);
		}
		await client.query(`UPDATE userdetails
							SET currentroute = $2::int
							WHERE userid = $1::uuid`, [req.id, req.params.routeId]);
	}
	await client.query('COMMIT');
    res.sendStatus(200);
}));

router.post('/landmark/:landmarkId/questions', errorCatcher(async (req, res, next) => {
    const { rows } = await client.query(`SELECT q.id AS question_id, q.text as question, a.id as answer_id, a.text as answer
                                         FROM questions q
                                         JOIN answers a ON a.questionid = q.id
                                         WHERE q.landmarkid = $1::int`, [req.params.landmarkId]);
    if (rows.length === 0) {
        res.status(500).send('Landmark has no questions.');
        return;
    }
    questions = {};
    for (let i = 0; i < rows.length; i++) {
        qid = rows[i].question_id;
        if (!questions[qid]) {
            questions[qid] = {'question': rows[i].question};
            questions[qid]['answers'] = [];
        }
        questions[qid]['answers'].push({'id': rows[i].answer_id, 'answer': rows[i].answer});
    }
    res.status(200).json(questions);
}));

router.post('/landmark/:landmarkId/questions/validate-answers', errorCatcher(async (req, res, next) => {
    /**
     * Primeste lista de id-uri de raspunsuri, returneaza daca sunt toate corecte sau nu.
     * Exemplu JSON primit: { "token": "...", "answers": [112, 22, 35]}
     * JSON inapoiat: { "correct": true/false }, cod 200 chiar si in cazul raspunsurilor gresite (request-ul este corect, raspunsurile nu sunt)
     */ 
    const { rows } = await client.query(`SELECT a.id
                                         FROM answers a
                                         JOIN questions q ON a.questionid = q.id
                                         JOIN landmarks l ON q.landmarkid = l.id
                                         WHERE l.id = $1::int
                                         AND a.iscorrect = true
                                         EXCEPT
                                         SELECT id
                                         FROM answers
                                         WHERE id = ANY($2::int[])`, [req.params.landmarkId, req.body.answers]);
    correct = (rows.length === 0);
    if (correct) {
        await client.query(`UPDATE userroutes ur
                        SET currentlandmark = COALESCE((
                                SELECT l.id
                                FROM landmarks l
                                WHERE l.route = ur.routeid
                                AND l.routeorder = 1 + (
                                        SELECT routeorder
                                        FROM landmarks
                                        WHERE id = ur.currentlandmark)), currentlandmark),
                            datecompleted = 
                                CASE WHEN (
                                        SELECT routeorder
                                        FROM landmarks
                                        WHERE id = ur.currentlandmark
                                ) = (
                                        SELECT MAX(routeorder)
                                        FROM landmarks l
                                        WHERE route = ur.routeid
                                ) THEN now()
                                ELSE NULL END,
                            locationchecked = FALSE    
                        WHERE userid = $1::uuid
                        AND routeid = $2::int
                        AND currentLandmark = $3::int`, [req.id, req.body.routeId, req.params.landmarkId]);
    }
    res.status(200).json({'correct': correct});
}));

router.post('/landmark/:landmarkId/validateCheck', errorCatcher(async (req, res, next) => {

    const { rows } = await client.query(`SELECT 1
                                         FROM userroutes ur
                                         WHERE userid = $1::uuid
                                         AND routeid = $2::int
                                         AND ((SELECT routeOrder
                                           FROM landmarks
                                           WHERE id = $3::int) < (SELECT routeOrder
                                           FROM landmarks
                                           WHERE id = ur.currentlandmark)
                                             OR (currentlandmark = $3::int 
                                               AND locationchecked = TRUE))`, [req.id, req.body.routeId, req.params.landmarkId]);
    correct = (rows.length != 0);
    res.status(200).json({'correct': correct});
}));

router.post('/landmark/:landmarkId/checklocation', errorCatcher(async (req, res, next) => {

        await client.query(`UPDATE userroutes ur
                        SET locationchecked = TRUE    
                        WHERE userid = $1::uuid
                        AND routeid = $2::int
                        AND currentLandmark = $3::int`, [req.id, req.body.routeId, req.params.landmarkId]);
    res.status(200);
}));

router.post('/gallery/:imageId/addLike', errorCatcher(async (req, res, next) => {

    await client.query(`INSERT INTO likes
                        VALUES ($1::uuid, $2::int)
                        ON CONFLICT DO NOTHING`, [req.id, req.params.imageId]);
    await client.query('COMMIT');
    res.status(200).json({});
}));

router.post('/gallery/:imageId/removeLike', errorCatcher(async (req, res, next) => {

    await client.query(`DELETE FROM likes
                        WHERE userid = $1::uuid
                        AND imageid = $2::int`, [req.id, req.params.imageId]);
    await client.query('COMMIT');
    res.status(200).json({});
}));

router.post('/gallery/:imageId/checkLiked', errorCatcher(async (req, res, next) => {

    const { rows } = await client.query(`SELECT 1
                                         FROM likes
                                         WHERE userid = $1::uuid
                                         AND imageid = $2::int`, [req.id, req.params.imageId]);
    correct = (rows.length != 0);
    res.status(200).json({'correct': correct});
}));

router.post('/gallery/:imageId/likes', errorCatcher(async (req, res, next) => {
    const { rows } = await client.query(`SELECT COUNT(imageid) AS likes
                                         FROM likes
                                         WHERE imageid = $1::int`, [req.params.imageId])
    res.status(200).json({likes: rows[0].likes});
}));

router.post('/gallery', errorCatcher(async (req, res, next) => {
    /**
     * Trimite toate imaginile.
     */
    const { rows } = await client.query(`SELECT i.id, i.title, i.path, u.username, COUNT(l.imageid) as likes
                                         FROM images i
                                         JOIN users u ON i.userid = u.id
                                         LEFT JOIN likes l ON l.imageid = i.id
                                         GROUP BY i.id, i.title, i.path, u.username
                                         ORDER BY likes DESC`);
    res.status(200).json({images: rows});
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