FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app

# Primero copiar solo pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Luego copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:25-ea-23-jdk-oraclelinux8
COPY --from=build /app/target/org-sas-mail-1.0.0.jar /opt/app/org-sas-mail-1.0.0.jar
WORKDIR /opt/app
EXPOSE 8715
ENTRYPOINT ["java","-jar","org-sas-mail-1.0.0.jar"]