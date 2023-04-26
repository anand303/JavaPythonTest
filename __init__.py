import logging
import json
import tarfile
import random
import azure.functions as func
import os
import shutil
from azure.storage.blob import BlobServiceClient
from urllib.parse import parse_qs


async def main(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Python HTTP trigger function processed a request.') 
    req_json = None
    req_body = None
    fileData = None
    req_body = req.get_body()
    filedata = ""
    key = None
    value = None
    for key, value in req.files.items():
        key = key
        value = value 
    a = None
    b = None
    for a, b in req.form.items():
        b = b
        a = a     
    # print(b.encode())
    with open("Hello.pdf","wb") as fo:
        fo.write(req_body)
    resp = { "file" : str(key), "reqjson" : str(req_json), "reqbody" : str(req_body), "formkey" : str(a), "formvalue" : str(b) }
    print(req.params.get('file'))
    for header , headervalue in req.headers.items():
        print(header + ":" + headervalue)
    
    
    # print(req)
    return func.HttpResponse(
             json.dumps(resp),
             status_code=200,mimetype='application/json'
        )         
        
        