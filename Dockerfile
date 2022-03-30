FROM openjdk:11

COPY target/untitled1-1.0-SNAPSHOT.jar /demo.jar

CMD ["java", "-jar", "/demo.jar"]