# Usamos una imagen base de Maven para compilar el proyecto
FROM maven:3.9-eclipse-temurin-22 AS build

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar los archivos de tu código fuente al contenedor
COPY . .

# Ejecutar Maven para construir el proyecto y generar el archivo .jar
RUN mvn clean package -Dmaven.test.skip=true

# Usamos una imagen base de OpenJDK para ejecutar la aplicación
FROM openjdk:25-ea-23-jdk-oraclelinux8

# Copiar el archivo .jar desde la etapa de construcción (build)
COPY --from=build /app/target/org-sas-config-0.0.1-SNAPSHOT.jar /opt/app/org-sas-config.jar

# Definir el directorio de trabajo
WORKDIR /opt/app

# Exponer el puerto necesario
EXPOSE 8888

# Definir el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "org-sas-config.jar"]
