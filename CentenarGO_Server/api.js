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
            bcrypt.compare(req.body.password, hash, (err, same) => {
					if(same) {
						res.status(200);
                        let token = jwt.sign({data: result.rows[0].id}, config.tokenSecret, {expiresIn: 30 * 24 * 60});
                        res.json({token: token});
					} else {
						console.log(err);
                        res.sendStatus(401);
					}
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

module.exports = {
    config: config,
    client: client,
    bcrypt: bcrypt,
    router: router
};