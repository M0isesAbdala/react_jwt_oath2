FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE $GATEWAY_PORT

ENTRYPOINT ["sh", "-c", "sleep 10 && java -jar app.jar"]