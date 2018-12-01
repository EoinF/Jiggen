FROM tomcat:8-alpine

RUN apk -v --update add \
        dos2unix \
        python \
        py-pip \
        bash \
        && \
    pip install --upgrade awscli && \
    rm -rf /var/cache/apk/*

WORKDIR /deploy

COPY . /deploy

RUN dos2unix /deploy/deploy.sh && \
    apk del dos2unix

RUN rm -rf /usr/local/tomcat/webapps

CMD ["sh", "deploy.sh"]