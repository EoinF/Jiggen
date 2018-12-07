#!/bin/bash
set -x

cd ../html/react
npm install -q
cd ../..
./gradlew html:dist -q

aws s3 cp html/build s3://jiggen-frontend/ --recursive
