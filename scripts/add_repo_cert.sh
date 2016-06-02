#!/bin/bash

FULLPATH=$(readlink -f $0)
DIR=$(dirname ${FULLPATH})
OLDDIR=$(pwd)
cd ${DIR}

REPO_SERVER=$(grep url ../repositories.gradle | cut -f3 -d/)

echo -n | openssl s_client -connect ${REPO_SERVER}:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > /tmp/${REPO_SERVER}.cert

if [ -z ${JAVA_HOME} ]; then
  echo JAVA_HOME not set
  JAVABIN=$(which java)
  JAVABIN=$(readlink -f ${JAVABIN})
  REAL_JAVA_HOME=$(readlink -f $(dirname ${JAVABIN})/..)
else
  REAL_JAVA_HOME=$(readlink -f ${JAVA_HOME})
fi
echo using ${REAL_JAVA_HOME}

CACERTS=$(locate cacerts | grep ${REAL_JAVA_HOME})
sudo ${JAVA_HOME}/bin/keytool -importcert -alias "$REPO_SERVER" -file /tmp/${REPO_SERVER}.cert -keystore $CACERTS -storepass changeit

cd ${OLDDIR}


