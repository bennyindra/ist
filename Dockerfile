# Build stage
FROM gradle:8.4-jdk17 AS builder
WORKDIR /

# Copy source code and build files
COPY . .

# Initialize and build the application
RUN gradle init --type java-application --dsl groovy --test-framework junit --project-name app --package com.example.app
RUN gradle build --no-daemon -x test -x spotlessCheck

# List the build output directory to verify location
RUN ls -la app/build/libs || ls -la build/libs

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /
COPY --from=builder /build/libs/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]