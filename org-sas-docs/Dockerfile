FROM eclipse-temurin:23-jdk
#RUN apk add --no-cache fontconfig ttf-dejavu
COPY ./target/origami-docs-1.0.0.jar /opt/app/origami-docs.jar
WORKDIR /opt/app
RUN sh -c 'touch origami-docs.jar'
EXPOSE 8718
ENTRYPOINT ["java","-jar","origami-docs.jar"]
