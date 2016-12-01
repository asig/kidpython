#!/usr/bin/env python

import json
import os
import requests
import sys

from requests.auth import HTTPBasicAuth

GITHUB_USER = 'asig'
GITHUB_REPO = 'test'
GITHUB_ACCESS_TOKEN = os.environ['GITHUB_ACCESS_TOKEN']


def release():
    if GITHUB_ACCESS_TOKEN == "":
        print >> sys.stderr, 'GITHUB_ACCESS_TOKEN is not set!'
        os.exit(1)

    auth = HTTPBasicAuth(GITHUB_USER, GITHUB_ACCESS_TOKEN)

    # create a new release
    url = 'https://api.github.com/repos/' + GITHUB_USER+ '/' + GITHUB_REPO +'/releases'
    payload = {
                'tag_name' : 'v2.0.6',
                'name' : 'v2.0.6',
                'body' : 'Is this now the latest?',
                'draft' : False,
                'prerelease' : False
              }
    r = requests.post(url, auth=auth, data=json.dumps(payload))
    releaseId = r.json()['id']
    uploadUrl = r.json()['upload_url'].split('{')[0]

    # upload the asset
    url = uploadUrl + "?name=ProgrammableFun.exe"
    headers = {
        'content-type': 'application/vnd.microsoft.portable-executable'
    }
    r = requests.post(url, auth=auth, headers=headers, data=open('../build/launch4j/ProgrammableFun.exe', 'rb'))
    print r.json()


if __name__ == "__main__":
    release()
