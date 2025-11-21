# Etapa 1: build do backend
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: runtime (usar Ubuntu baseado no Temurin)
FROM ubuntu:22.04

# Instalar Java
RUN apt-get update && apt-get install -y openjdk-21-jdk

# Instalar dependências nativas do Tess4J
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    tesseract-ocr-por \
    tesseract-ocr-eng \
    libtesseract-dev \
    libleptonica-dev \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/4.00/tessdata

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
