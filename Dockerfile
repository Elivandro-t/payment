FROM ubuntu:latest as build

RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven

WORKDIR /payments

COPY pom.xml .

COPY . .

RUN mvn clean package

# Estágio de produção
FROM eclipse-temurin:21-jdk AS build
WORKDIR /payments

COPY --from=build /payments/target/payments-0.0.1-SNAPSHOT.jar payments.jar
EXPOSE 8080
ENV DATA_DIR=/var/lib/data
CMD ["java", "-jar", "payments.jar"]
