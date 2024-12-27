FROM eclipse-temurin:23-jdk
WORKDIR /app

COPY book-storage-service/target/book-storage-service-0.0.1-SNAPSHOT.jar bookStorage.jar
COPY book-tracker-service/target/book-tracker-service-0.0.1-SNAPSHOT.jar bookTracker.jar

EXPOSE 8080
EXPOSE 8081

CMD ["sh", "-c", "java -jar bookStorage.jar & java -jar bookTracker.jar"]