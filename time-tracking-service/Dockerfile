FROM amazoncorretto:17-alpine
WORKDIR /application
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
