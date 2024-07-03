# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Package the application
RUN ./mvnw package -DskipTests

FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/urlshortener-statistics-*.jar /app/urlshortener-statistics.jar

# Make port 8081 available to the world outside this container
EXPOSE 8081

# Run the jar file
CMD ["java", "-jar", "urlshortener-statistics.jar"]
