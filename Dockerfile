FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

COPY target/*.jar app.jar

RUN apk add --no-cache ffmpeg

ENTRYPOINT ["java","-jar","/app.jar"]