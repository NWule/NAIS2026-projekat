
# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/PlayerInfoService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (standard Spring Boot port)
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]