const express    = require('express');
const app        = express();
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
const api = require('./api');
const config = api.config;
const client = api.client;
const bcrypt = api.bcrypt;

app.post('/signup', (req, res) => {
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

app.use('/api', api.router);
app.use((req, res) => {
    res.sendStatus(404);
})

port = process.env.PORT || 8080;
app.listen(port)
console.log('Server started on ' + port); 

process.on('exit', () => client.close());