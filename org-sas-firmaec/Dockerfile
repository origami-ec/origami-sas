FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app

# Primero copiar solo pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Luego copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=build /app/target/origami-firma-ec-1.0.0.jar /opt/app/origami-firma-ec.jar
WORKDIR /opt/app
RUN sh -c 'touch origami-firma-ec.jar'
EXPOSE 8722
ENTRYPOINT ["java","-jar","origami-firma-ec.jar"]