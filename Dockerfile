FROM openjdk:12-jdk-alpine

RUN apk add --no-cache bash

WORKDIR /kotlin-app

CMD ./gradlew run