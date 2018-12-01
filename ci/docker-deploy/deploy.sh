#!/bin/sh

aws s3 cp html/build/jiggen.war s3://jiggen/build-ui --dry-run

mv jiggen.war /usr/local/tomcat/webapps/ROOT.war

catalina.sh run