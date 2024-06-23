FROM openjdk:17-jdk-slim
WORKDIR /mapService
COPY . .
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/mapService-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080