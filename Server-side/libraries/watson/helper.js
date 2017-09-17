/**
 * Created by Harsh on 01-08-2017.
 */

var watson = require('watson-developer-cloud');
var config = require('../config/private')
var DocumentConversionV1 = require('watson-developer-cloud/document-conversion/v1');
var document_conversion = new DocumentConversionV1(config.document_conversion);
var NaturalLanguageUnderstandingV1 = require('watson-developer-cloud/natural-language-understanding/v1.js');

var nlu = new NaturalLanguageUnderstandingV1(config.nlu);


function convertPDFtoHTML(file_to_upload,callback) {
    document_conversion.convert({
        file: file_to_upload,
        conversion_target: document_conversion.conversion_target.NORMALIZED_HTML,

    }, function (err, response) {
        if (err) {
            console.error(err);
        } else {
            callback(response);
        }
    });
}

function getConcepts(text,callback){
    nlu.analyze({
        'html': text, // Buffer or String
        'features': {
            'concepts': {},
            'keywords': {},
        }
    }, function(err, response) {
        if (err)
            console.log('error:', err);
        else
            callback(response);
    });
}

exports.getConcepts = getConcepts
exports.convertPDFtoHTML = convertPDFtoHTML