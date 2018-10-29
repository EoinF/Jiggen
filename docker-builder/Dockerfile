FROM openjdk:8u181-jdk-alpine3.8

RUN apk -v --update add \
        bash \
        dos2unix \
        git \
        python \
        py-pip \
        nodejs \
        && \
    pip install --upgrade awscli && \
    rm -rf /var/cache/apk/*

WORKDIR /build

COPY . /build

RUN dos2unix /build/build.sh

CMD ["sh", "/build/build.sh"]