FROM openjdk

WORKDIR /app

COPY target/toledoBank-0.0.1-SNAPSHOT.jar /app/toledoBank.jar
ENTRYPOINT ["java", "-jar", "toledoBank.jar"]