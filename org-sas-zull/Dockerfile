FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app

# Primero copiar solo pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Luego copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:25-ea-23-jdk-oraclelinux8
COPY --from=build /app/target/org.sas.zull-0.0.1-SNAPSHOT.jar /opt/app/org.sas.zull.jar
WORKDIR /opt/app
EXPOSE 8713
ENTRYPOINT ["java", "-jar", "org.sas.zull.jar"]