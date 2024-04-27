FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

FROM eclipse-temurin:17-jdk-alpine

COPY --from=build /workspace/app/build/libs/app-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]