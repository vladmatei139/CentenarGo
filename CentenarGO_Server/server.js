const express    = require('express');
const fileUpload = require('express-fileupload');
const app        = express();
const bodyParser = require('body-parser');
const morgan  	 = require('morgan');
const path       = require('path')
const fs         = require('fs');
morgan(':method :url :status :res[content-length] - :response-time ms')
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(fileUpload());
const api = require('./api');
const config = api.config;
const client = api.client;
const bcrypt = api.bcrypt;

const dir = path.join(__dirname, 'public')
app.use(express.static(dir));

app.use('/api', api.router);
app.use((req, res) => {
    res.sendStatus(404);
});

port = process.env.PORT || 8080;
app.listen(port,'0.0.0.0');
console.log('Server started on ' + port); 

process.on('exit', () => client.close());