#!/bin/bash
set -x

cd ../html/react
npm install -q
cd ../..
./gradlew html:dist -q

find html/build/dist -type f \
    ! -name '*.gz' \
    ! -name '*.png' \
    ! -name '*.ico' \
    ! -name '*.swf' \
        -exec gzip "{}" \;

for f in `find html/build/dist -type f -name '*.gz'`; do
    mv $f ${f%.gz}
done

aws s3 sync html/build/dist s3://cdn.jiggen.app/ \
--include "*" --acl "public-read" \
--exclude "*.js" \
--exclude "*.css" \
--exclude "*.html" \
--exclude "*.fnt" \
--exclude "*.atlas" \
--exclude "*.txt" \
--exclude "*.glsl" \
--exclude "*.frag" \
--exclude "*.vert" \
--exclude "*.json" \
--exclude "*.map" \
--delete

aws s3 sync html/build/dist s3://cdn.jiggen.app/ \
--exclude "*" \
--include "*.js" --acl "public-read" --content-encoding gzip \
--include "*.css" --acl "public-read" --content-encoding gzip \
--include "*.html" --acl "public-read" --content-encoding gzip \
--include "*.fnt" --acl "public-read" --content-encoding gzip \
--include "*.atlas" --acl "public-read" --content-encoding gzip \
--include "*.txt" --acl "public-read" --content-encoding gzip \
--include "*.glsl" --acl "public-read" --content-encoding gzip \
--include "*.frag" --acl "public-read" --content-encoding gzip \
--include "*.vert" --acl "public-read" --content-encoding gzip \
--include "*.json" --acl "public-read" --content-encoding gzip \
--delete
