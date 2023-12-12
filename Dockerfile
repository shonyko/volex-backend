# docker buildx build --platform linux/amd64,linux/arm64,linux/arm/v7,linux/arm/v8 -t statiemeteo.dyndns.org:5000/volex/backend . --push
FROM maven:3.9.6-eclipse-temurin-17-focal AS build
WORKDIR /build
COPY src src
COPY pom.xml .
RUN mvn -f ./pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-focal
COPY --from=build ./build/target/backend-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "app.jar"]