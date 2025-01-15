FROM maven:3.9-eclipse-temurin-22-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:resolve-plugins

COPY src/main src/main

RUN mvn package

FROM eclipse-temurin:22-jre-alpine

WORKDIR /app

COPY --from=build /app/target/authService-0.1.jar .

RUN apk --no-cache add curl

EXPOSE 8091

CMD ["java", "-jar", "authService-0.1.jar"]
