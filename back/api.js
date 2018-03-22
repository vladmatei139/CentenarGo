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
            client.query('INSERT INTO users (username, email, password) VALUES ($1::text, $2::text, $3::text)',
                        [req.body.username, req.body.email, hash])
                .then(() => {
                    res.sendStatus(200);
                })
                .catch((err) => {
                    console.log(err.stack);
                    if (err.message.toLowerCase().includes('duplicate')) {
                        res.status(400).send('Duplicate primary key in User table (email already exists).');
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

    client.query('SELECT id as id, password AS hash FROM users WHERE email = $1::text',
                 [req.body.email])
        .then((result) => {
            if (result.rows.length > 0) {
                hash = result.rows[0].hash;
                bcrypt.compare(req.body.password, hash)
                    .then((same) => {
                        res.status(200);
                        let token = jwt.sign({data: result.rows[0].id}, config.tokenSecret, {expiresIn: 30 * 24 * 60});
                        res.json({token: token});
                    })
                    .catch((err) => {
                        console.log(err.stack);
                        res.sendStatus(401);
                    });
            }
            else {
                res.status(404).send('User does not exist.');
            }
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
                res.sendStatus(403);
            } 
            req.decoded = decoded;
            next();
        });
    }
    else {
        res.sendStatus(403);
    }
});

module.exports = {
    config: config,
    client: client,
    bcrypt: bcrypt,
    router: router
};