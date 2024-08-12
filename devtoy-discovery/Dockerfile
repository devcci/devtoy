FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} devtoy-discovery-0.0.1-SNAPSHOT.jar
EXPOSE 8070
ENTRYPOINT ["java","-jar","/devtoy-discovery-0.0.1-SNAPSHOT.jar"]