FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:25-ea-23-jdk-oraclelinux8
COPY --from=build /app/target/origami-docs-1.0.0.jar /opt/app/origami-docs.jar
WORKDIR /opt/app
EXPOSE 8716
ENTRYPOINT ["java" ,"-jar","org-sas-administrativo.jar"]
