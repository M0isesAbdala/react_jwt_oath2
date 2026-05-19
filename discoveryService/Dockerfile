FROM eclipse-temurin:17

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY . .

EXPOSE $DISCOVERY_SERVICE_PORT

CMD ["./mvnw", "spring-boot:run"]