const express = require('express'),
    errorhandler = require('errorhandler'),
    logger = require('morgan'),
    bodyparser = require('body-parser')
 app = express()
 
var routes = require('./libraries/routes.js');

process.on('uncaughtException', function (err) {
    console.log(err);
});

app.use(errorhandler());
// 5MB limit for request bosdy
app.use(bodyparser.urlencoded({
    extended: false,
    limit: 1024  * 1024 * 5,
}));
app.use(bodyparser.json());

routes(app);
// Listen at port 80 on all interfaces
var http = require('http').Server(app);
http.listen(80, "0.0.0.0");
console.log('Server listening at port 80')
