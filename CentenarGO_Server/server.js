
const express    = require('express');
const fileUpload = require('express-fileupload');
const app        = express();
const bodyParser = require('body-parser');
const morgan  	 = require('morgan');
const path       = require('path')
const fs         = require('fs');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(fileUpload());
const api = require('./api');
const config = api.config;
const client = api.client;
const bcrypt = api.bcrypt;

const dir = path.join(__dirname, 'public')
app.use(express.static(dir));

const images = path.join(__dirname, 'images')
app.use(express.static(images));

app.use(morgan(':date[web]\t:method\t:url\t:remote-addr\tStatus :status\t:response-time ms'));
app.use('/api', api.router);
app.use((req, res) => {
    res.sendStatus(404);
});

port = process.env.PORT || 8080;
app.listen(port,'0.0.0.0');
console.log('Server started on ' + port); 

process.on('exit', () => client.close());