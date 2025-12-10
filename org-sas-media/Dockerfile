FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1-alpine
#FROM openjdk:23-oraclelinux8
COPY ./target/org-ibarra-media-0.0.1-SNAPSHOT.jar /opt/app/org-ibarra-media-0.0.1.jar
WORKDIR /opt/app
RUN sh -c 'touch org-ibarra-media-0.0.1.jar'
EXPOSE 8720
ENTRYPOINT ["java","-jar","org-ibarra-media-0.0.1.jar"]
