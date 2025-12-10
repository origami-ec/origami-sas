FROM eclipse-temurin:23-jdk
COPY ./target/org-sas-administrativo-0.0.1-SNAPSHOT.jar /opt/app/org-sas-administrativo.jar
WORKDIR /opt/app
RUN sh -c 'touch org-sas-administrativo.jar'
EXPOSE 8712
ENTRYPOINT ["java" ,"-jar","org-sas-administrativo.jar"]

