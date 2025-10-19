FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw &&\
    ./mvnw install -DskipTests

FROM eclipse-temurin:21-jdk-alpine

COPY --from=build /workspace/app/target/*.jar app.jar

RUN apk add --no-cache ffmpeg

ENTRYPOINT ["java", "-jar", "app.jar"]
