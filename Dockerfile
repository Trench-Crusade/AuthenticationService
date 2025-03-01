FROM maven:3.9-eclipse-temurin-22-alpine AS build
COPY src /app/src
COPY pom.xml /app
COPY local-maven-repo /app/local-maven-repo

RUN mvn -f /app/pom.xml clean package

FROM eclipse-temurin:22-jre-alpine

WORKDIR /app

COPY --from=build /app/target/authService-0.6.jar .

RUN apk --no-cache add curl

EXPOSE 8091

CMD ["java", "-jar", "authService-0.6.jar"]
