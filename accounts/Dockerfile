# Use an official base image
FROM openjdk:17-jdk

# Set the working directory
WORKDIR /app

# Copy the WAR file to the container
COPY target/*.war /app/app.war

# Specify the command to run on container start
CMD ["java", "-jar", "app.war"]

# Test for GitHub !