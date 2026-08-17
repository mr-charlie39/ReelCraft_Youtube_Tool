# Multi-stage Dockerfile for building and running the Spring Boot app
# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
# Use a maven package to produce the fat jar
RUN mvn -B -DskipTests package

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copy the jar built in the previous stage. Use wildcard to match built artifact.
COPY --from=build /workspace/target/*.jar /app/app.jar

# Expose a sensible port. The container will respect the PORT env var if provided.
EXPOSE 8081

# Use the PORT environment variable provided by the hosting platform (Vercel provides PORT at runtime).
# Fall back to 8081 if PORT is not set.
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT:-8081} -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]
