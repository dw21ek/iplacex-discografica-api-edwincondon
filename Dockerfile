# Stage 1: Construcción (Generar archivo .war)
FROM gradle:8-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

# Stage 2: Ejecución
FROM openjdk:21-jdk-slim
EXPOSE 8080
# Como en build.gradle tiene "id 'war'" y "version = '1'", el archivo generado será discografia-1.war
COPY --from=build /home/gradle/src/build/libs/discografia-1.war app.war
ENTRYPOINT ["java", "-jar", "/app.war"]
