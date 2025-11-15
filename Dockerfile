# --- Stage 1: build ---
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: runtime ---
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/demoapp-1.0.0.jar ./demoapp.jar
EXPOSE 8080
CMD ["java", "-jar", "demoapp.jar"]

