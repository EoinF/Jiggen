#!/bin/sh
set -x

git clone https://github.com/EoinF/Jiggen.git

cd Jiggen/html/react
npm install
cd ../..
./gradlew html:dist

mv html/build/jiggen.war ./docker-deploy/
cd docker-deploy

docker build -t jiggen:deploy-ui
docker tag jiggen:deploy-ui 025171290744.dkr.ecr.eu-west-1.amazonaws.com/jiggen:deploy-ui
docker push 025171290744.dkr.ecr.eu-west-1.amazonaws.com/jiggen:deploy-ui