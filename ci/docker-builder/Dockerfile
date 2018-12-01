FROM openjdk:8u181-jdk-alpine3.8

RUN apk -v --update add \
        dos2unix \
        git \
        python \
        py-pip \
        npm \
        docker \
        && \
    pip install --upgrade awscli && \
    npm install npm@6 -g && \
    rm -rf /var/cache/apk/*

WORKDIR /build

COPY . /build

RUN dos2unix /build/build.sh && \
    apk del dos2unix

CMD ["sh", "/build/build.sh"]