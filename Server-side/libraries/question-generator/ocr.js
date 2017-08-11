/**
 * Created by Harsh on 01-08-2017.
 */
const _defaultOcrSpaceUrl = 'https://api.ocr.space/parse/image';
const _base64ImagePattern = 'data:%s;base64,%s';
const _defaultImageType = 'image/gif';
const _defaultLanguade = 'eng';
const _isOverlayRequired = false;
var request = require('request');
var util = require('util');
var watsonAPI = require('../watson').watsonApi

function decodebase64File(){

}



exports.convertocrtotext = function (base64image, options,isConcept,callback) {


             formOptions = {
                language: options.language ? options.language : _defaultLanguade,
                apikey: options.apikey,
                isOverlayRequired: options.isOverlayRequired ? options.isOverlayRequired : false
            };

          //  formOptions.Base64Image = util.format(_base64ImagePattern, (options.imageFormat) ? options.imageFormat : _defaultImageType, base64image);
             formOptions.Base64Image = base64image
             uri = {
                method: 'post',
                url: options.url ? options.url : _defaultOcrSpaceUrl,
                form: formOptions,
                headers: {
                    "content-type": "application/json",
                },
                json: true,
                 proxy:'http://proxy22.iitd.ac.in:3128'
            };

            request(uri, function (error, response, ocrParsedResult) {
                if (error) {
                    deferred.reject(error);
                } else {

                    //Get the parsed results, exit code and error message and details
                    var parsedResults = ocrParsedResult["ParsedResults"];

                    if (parsedResults) {
                        var pageText = '';

                        parsedResults.forEach(function(value) {
                            var exitCode = value["FileParseExitCode"];
                            var parsedText = value["ParsedText"];
                            //var errorMessage = responseBody["ParsedTextFileName"];
                            //var errorDetails = responseBody["ErrorDetails"];

                            //var textOverlay = responseBody["TextOverlay"];

                            switch (+exitCode) {
                                case 1:
                                    pageText = parsedText;
                                    break;
                                case 0:
                                case -10:
                                case -20:
                                case -30:
                                case -99:
                                default:
                                    pageText += "Error: " + errorMessage;
                                    break;
                            }

                        }, this);

                        // Monta o objeto com os resultados
                         result = {
                            parsedText: pageText,
                            ocrParsedResult: ocrParsedResult
                        }
                        if(isConcept)
                            watsonAPI.getConcepts(result.parsedText,callback);
                         else callback(result.parsedText)
                    } else {
                        callback(ocrParsedResult);
                    }
                }
            })

}