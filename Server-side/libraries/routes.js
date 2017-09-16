/**
 * Created by Harsh on 01-08-2017.
 */

var questionHelper = require('./question-generator')
var watsonAPI = require('./watson').watsonApi
var config = require('./config/private')
module.exports = function(app){
	
    app.all('/search/keyword',function(req,res){
        var query = req.query.query
        var accept = function(answer){
            res.json({'api':'search api','message':'successful','text':answer.replace(/<\/?a[^>]*>/g, "")})
        }
        questionHelper.searchConcept.getWikiText(query,accept);
    })

    app.all('/convertpdf/html',function(req,res){
        var accept = function(answer){
            res.json({'api':'Convert PDF to HTML','message':'successful','text':answer})
        }
        console.log(req.body.name)

        watsonAPI.convertPDFtoHTML(new Buffer(req.body.file,'base64'),accept)
        })

    app.all('/getQuestion/',function(req,res){

       
        var text = req.query.text
        var accept = function(answer){
            res.json({'api':'get Question','message':'successful','text':answer})
        }
        questionHelper.createQuestion.getQuestion(text,accept)
        })

    app.all('/getRelatedConcepts/',function(req,res){
        var text = req.query.text
        var accept = function(answer){
            res.json({'api':'get Related Concepts','message':'successful','text':answer})
        }
        watsonAPI.getConcepts(text,accept)
        })

    app.all('/getRelatedConcepts/ocr',function(req,res){
        var text = req.query.text
        var accept = function(answer){
            res.json({'api':'get Related Concepts from image','message':'successful','text':answer})
        }

        var extImage = req.body.name
        console.log(extImage)
        options =     {
                apikey: config.ocrapi,
                language: 'eng',
                isOverlayRequired: true,
                url: 'https://api.ocr.space/parse/image'  ,
                 imageFormat: 'image/'+extImage
                 }
        questionHelper.ocr.convertocrtotext(req.body.file,options,true,accept)
    })

    app.all('getText/ocr',function(req,res){
        var text = req.query.text
        var accept = function(answer){
            res.json({'api':'get Text from image','message':'successful','text':answer})
        }

        var extImage = req.body.name
        console.log(extImage)
        options =     {
            apikey: config.ocrapi,
            language: 'eng',
            isOverlayRequired: true,
            url: 'https://api.ocr.space/parse/image'  ,
            imageFormat: 'image/'+extImage
        }
        questionHelper.ocr.convertocrtotext(req.body.file,options,false,accept)
    })
    
}

