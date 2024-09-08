# Use a base image with Java installed
FROM openjdk:17

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Copy the JAR file into the container
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]