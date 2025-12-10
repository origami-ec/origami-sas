#FROM adoptopenjdk/openjdk11:alpine-jre
FROM openjdk:23-oraclelinux8
COPY ./target/org-ibarra-gateway-1.2.0.jar /opt/app/org-ibarra-gateway.jar
WORKDIR /opt/app
RUN sh -c 'touch org-ibarra-gateway.jar'
EXPOSE 80
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "org-ibarra-gateway.jar"]
