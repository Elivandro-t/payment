FROM ubuntu:latest as build

RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven

WORKDIR /payments

COPY pom.xml .

COPY . .

RUN mvn clean package

# Estágio de produção
FROM openjdk:17-jdk-slim

WORKDIR /payments

COPY --from=build /payments/target/payments-0.0.1-SNAPSHOT.jar payments.jar
ENV DATA_DIR=/var/lib/data
CMD ["java", "-jar", "payments.jar"]
