const express    = require('express');
const app        = express();
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
const api = require('./api');
const config = api.config;
const client = api.client;
const bcrypt = api.bcrypt;

app.use('/api', api.router);
app.use((req, res) => {
    res.sendStatus(404);
})

port = process.env.PORT || 8080;
app.listen(port,'0.0.0.0');
console.log('Server started on ' + port); 

process.on('exit', () => client.close());