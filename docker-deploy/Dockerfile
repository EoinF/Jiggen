FROM tomcat:8-alpine

RUN rm -rf /usr/local/tomcat/webapps

ADD jiggen.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]