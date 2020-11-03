#FROM openjdk:12-jdk-alpine
#
#RUN apk add --no-cache bash
#
#WORKDIR /kotlin-app
#
#CMD ./gradlew run

FROM openjdk:8-jre-alpine

ENV APPLICATION_USER ktor
RUN adduser -D -g '' $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

#Copy app
COPY ./build/libs/kotlin-app-1.0-SNAPSHOT.jar /app/kotlin-app.jar
#Copy db
COPY ./db/db.mv.db /app/db/db.mv.db

WORKDIR /app

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "kotlin-app.jar"]
