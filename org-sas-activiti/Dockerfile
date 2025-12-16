FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:25-ea-23-jdk-oraclelinux8
COPY --from=build /app/target/org-ibarra-activiti-0.0.1.jar /opt/app/org-ibarra-activiti-0.0.1.jar
WORKDIR /opt/app
EXPOSE 8714
ENV JAVA_OPTS="-server --add-exports java.desktop/sun.font=ALL-UNNAMED"
ENTRYPOINT ["java", "-jar","org-ibarra-activiti-0.0.1.jar"]