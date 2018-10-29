#!/bin/sh
set -x

git clone https://github.com/EoinF/Jiggen.git

cd Jiggen
./gradlew html:dist

# Push war file to s3
aws s3 cp /html/build/jiggen.war s3://jiggen/artifacts/html
