FROM tomcat:8-alpine

RUN rm -rf /usr/local/tomcat/webapps

WORKDIR /build

COPY . /build

CMD ["sh", "deploy.sh"]