/**
 * Created by Harsh on 01-08-2017.
 */
// Calls intelliq server for question generation 
var request = require('request');
exports.getQuestion = function (text,callback){
    var options = {
        uri: 'http://127.0.0.1:8000/generate_qa',
        method: 'POST',
        json: {
            "mytopic": text
        }
    };
    request(options, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            console.log(body)
            callback(body);
        }
    })
}
