# Use official Java image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/*.jar app.jar

# Expose port (Spring Boot runs here)
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","/app/app.jar"]