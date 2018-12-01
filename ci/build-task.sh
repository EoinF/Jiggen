#!/bin/bash
set -x

cd ../html/react
npm install -q
cd ../..
./gradlew html:dist -q

aws s3 cp html/build/jiggen.war s3://jiggen/build-ui