# -------- Build Stage --------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# -------- Final Runtime Stage (Only the JAR) --------
FROM gcr.io/distroless/java21-debian12

WORKDIR /app
COPY --from=builder /app/target/db-initializer-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]