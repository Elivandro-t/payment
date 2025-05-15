# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /payments

COPY pom.xml .
COPY . .

RUN mvn clean package -DskipTests

# Etapa de produção
FROM eclipse-temurin:21-jdk AS production

WORKDIR /payments

COPY --from=builder /payments/target/payments-0.0.1-SNAPSHOT.jar payments.jar

ENV DATA_DIR=/var/lib/data

CMD ["java", "-jar", "payments.jar"]
