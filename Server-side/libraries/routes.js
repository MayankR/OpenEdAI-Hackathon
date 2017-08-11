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
        //res.json({"sucess":true,"message":"PDF to HTML convert API",'text':'<p>This will be HTML</p>'})
        })

    app.all('/getQuestion/',function(req,res){

        /*res.json({"sucess":true,"message":"generate questions API",questions:[
            {'question':'What is your name?',
            'answer':['Tumhein matlab','main nahin bataunga','Tom Cruise','Kya karega jaan ke']}
            ]}
            )*/
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

        //res.json({'api':'get Related Concepts from image','message':'successful'})
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
            res.json({'api':'get Related Concepts from image','message':'successful','text':answer})
        }

        //res.json({'api':'get Related Concepts from image','message':'successful'})
        var extImage = req.body.name
        console.log(extImage)
        options =     {
            apikey: '6dff6a8e3388957',
            language: 'eng',
            isOverlayRequired: true,
            url: 'https://api.ocr.space/parse/image'  ,
            imageFormat: 'image/'+extImage
        }
        questionHelper.ocr.convertocrtotext(req.body.file,options,false,accept)
    })
    
    app.all('/test',function (req,res) {
        res.json({'api':'search api','message':'successful','text':"<a href='juioi'>I am hero</a>".replace(/<\/?a[^>]*>/g, "")})
    })


}

