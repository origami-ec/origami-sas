FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Primero copiar solo pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Luego copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:23-oraclelinux8
COPY --from=build /app/target/org-ibarra-gateway-1.2.0.jar /opt/app/org-ibarra-gateway.jar
WORKDIR /opt/app
RUN sh -c 'touch org-ibarra-gateway.jar'
EXPOSE 80
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=dev", "org-ibarra-gateway.jar"]