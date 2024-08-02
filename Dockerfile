FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y openjdk-21-jdk && \
    apt-get clean;

WORKDIR /app

COPY target/MonieFlex-0.0.1-SNAPSHOT.jar monieflex.jar

EXPOSE 8080

CMD ["java", "-jar", "monieflex.jar"]