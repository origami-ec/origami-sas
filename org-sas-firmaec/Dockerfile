FROM adoptopenjdk/openjdk11:alpine-jre
COPY ./target/origami-firma-ec-1.0.0.jar /opt/app/origami-firma-ec.jar
WORKDIR /opt/app
RUN sh -c 'touch origami-firma-ec.jar'
EXPOSE 8722
ENTRYPOINT ["java","-jar","origami-firma-ec.jar"]
