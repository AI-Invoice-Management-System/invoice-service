FROM gradle:9.4.1-jdk21 AS build
WORKDIR /app
COPY . .

RUN ./gradlew clean build --no-daemon
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 9669
ENTRYPOINT ["java", "-jar", "app.jar"]