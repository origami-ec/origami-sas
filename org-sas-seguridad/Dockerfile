FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:25-ea-23-jdk-oraclelinux8
COPY --from=build /app/target/org-sas-seguridad-0.0.1-SNAPSHOT.jar /opt/app/org-sas-seguridad.jar
WORKDIR /opt/app
EXPOSE 8712
ENTRYPOINT ["java", "-jar", "org-sas-seguridad.jar"]
