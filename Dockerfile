# Use a JDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy jar from the build context
COPY target/MovieReservationSystem-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
