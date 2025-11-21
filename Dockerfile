FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn -e -X clean package -DskipTests

FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    tesseract-ocr-por \
    tesseract-ocr-eng \
    tesseract-ocr-osd \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/4.00/tessdata

# Render exige porta 8080
EXPOSE 8080

COPY --from=build /app/target/*.jar /app/app.jar

ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=${PORT}"]
