FROM maven:latest AS build
COPY ./src ./build/src
COPY ./pom.xml ./build
RUN mvn -f ./build/pom.xml clean package

FROM openjdk:latest
COPY --from=build ./build/target/energy-utility-platform-backend-0.0.1-SNAPSHOT.jar ./app.jar
COPY --from=build ./build/target/classes/certs ./classes
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "app.jar"]