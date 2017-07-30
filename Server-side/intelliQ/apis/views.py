from django.shortcuts import render
from django.http import HttpResponse, HttpResponseBadRequest
from django.views.decorators.http import require_http_methods
from django.views.decorators.csrf import csrf_exempt
from qa_generator.wikitrivia.scripts.wikitrivia import generate_trivia
import json
import traceback



def index(request):
    return HttpResponse("Welcome to IntelliQ")


@require_http_methods(["POST"])
@csrf_exempt 
def generate_qa(request):
    """ Endpoint definition for generating question answers form text """

    try:
        json_str = request.body.decode('utf-8')
        request_json = json.loads(json_str)
    except:
        traceback.print_exc()
        return HttpResponseBadRequest("Error in parsing request params")

    response_json = generate_trivia(request_json)

    return HttpResponse(response_json, content_type="application/json")
