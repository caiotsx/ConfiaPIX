# Etapa 1: build do backend
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn -e -X clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:21-jdk

# Instala Tesseract + pacotes de idiomas
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    tesseract-ocr-por \
    tesseract-ocr-eng \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# NÃO recrie o diretório tessdata — ele já existe!

# Defina TESSDATA_PREFIX corretamente
ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/4.00/tessdata

# Expor porta
EXPOSE 8081

# Copiar aplicação
COPY --from=build /app/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
