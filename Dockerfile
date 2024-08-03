FROM ubuntu:latest
LABEL authors="ridhogymnastiar"

FROM amazoncorretto:17
WORKDIR /app
COPY target/simple-bank-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]