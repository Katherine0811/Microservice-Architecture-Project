# Use an official base image
FROM openjdk:17-jdk

# Set the working directory
WORKDIR /app

# Copy the executable JAR file to the container
COPY target/*.jar /app/app.jar

# Specify the command to run on container start
CMD ["java", "-jar", "app.jar"]

# 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