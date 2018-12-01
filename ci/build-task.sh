#!/bin/sh
set -x


cd ../html/react
npm install
cd ../..
./gradlew html:dist

aws s3 cp html/build/jiggen.war s3://jiggen/build-ui