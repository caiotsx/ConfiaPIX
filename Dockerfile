# Etapa 1: Build com JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


# Etapa 2: Runtime (Ubuntu, NÃO use Alpine)
FROM ubuntu:22.04

# Instala o Java + Tesseract + Leptonica
RUN apt-get update && apt-get install -y \
    openjdk-21-jre \
    tesseract-ocr \
    libtesseract-dev \
    liblept5 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]