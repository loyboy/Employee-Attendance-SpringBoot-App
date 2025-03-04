# Use Maven to build the application
FROM maven:3.8.6-openjdk-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the entire project source
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Go to the Next Step to run the application

# Use a lightweight OpenJDK runtime for the final image
FROM openjdk:17-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/employee-attendance-register-app-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8085

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
