#!/bin/bash
set -x

cd ../html/react
npm install -q
cd ../..
./gradlew html:dist -q
gzip -r html/build/dist

tail -f /dev/null # Wait forever

#aws s3 cp html/build/dist s3://cdn.jiggen.app/ --recursive \
#--include "*" --acl "public-read" \
#--exclude "*.js" \
#--exclude "*.css" \
#--delete
#
#
#aws s3 cp html/build/dist s3://cdn.jiggen.app/ --recursive \
#--exclude "*" \
#--include "*.js" --acl "public-read" --content-encoding gzip \
#--include "*.css" --acl "public-read" --content-encoding gzip \
#--delete
