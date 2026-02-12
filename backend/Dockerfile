FROM gradle:8.7-jdk21 AS build

WORKDIR /workspace
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
COPY src src

RUN ./gradlew bootJar -x test

FROM eclipse-temurin:21-jre

WORKDIR /app
RUN mkdir -p /app/data
COPY --from=build /workspace/build/libs/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
