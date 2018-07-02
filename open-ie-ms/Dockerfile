FROM openjdk:9-jre
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "--add-modules=java.xml.bind", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
