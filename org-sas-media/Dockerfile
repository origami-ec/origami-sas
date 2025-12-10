FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /ap
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:25-ea-23-jdk-oraclelinux8
COPY --from=build /app/target/org-ibarra-media-0.0.1-SNAPSHOT.jar /opt/app/org-ibarra-media-0.0.1.jar
WORKDIR /opt/app
EXPOSE 8720
ENTRYPOINT ["java","-jar","org-ibarra-media-0.0.1.jar"]