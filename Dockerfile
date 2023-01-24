FROM --platform=linux/amd64 openjdk:17-oracle
ARG JAR_FILE=target/*.jar
EXPOSE 80
COPY ./target/Spring-Compressor-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]