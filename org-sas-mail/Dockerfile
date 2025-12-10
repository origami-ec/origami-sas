FROM openjdk:23-oraclelinux8
COPY ./target/org-sas-mail-1.0.0.jar /opt/app/org-sas-mail-1.0.0.jar
WORKDIR /opt/app
RUN sh -c 'touch org-sas-mail-1.0.0.jar'
EXPOSE 8715
ENTRYPOINT ["java","-jar","org-sas-mail-1.0.0.jar"]
