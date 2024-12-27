FROM openjdk:21-jdk-slim

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Set the application JAR file
ARG JAR_FILE=build/libs/event_sourcing-0.0.1-SNAPSHOT.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app.jar"]