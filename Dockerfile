FROM openjdk:17
ADD target/ms_cliente-0.0.1-SNAPSHOT.jar ms_cliente-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ms_cliente-0.0.1-SNAPSHOT.jar"]