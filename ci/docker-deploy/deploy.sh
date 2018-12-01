#!/bin/sh

aws s3 cp s3://jiggen/build-ui/jiggen.war /usr/local/tomcat/webapps/ROOT.war

catalina.sh run