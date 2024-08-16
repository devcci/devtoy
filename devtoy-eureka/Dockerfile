FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY gradlew ./
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew --no-daemon dependencies
COPY . .
RUN ./gradlew --no-daemon build -x test

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar devtoy-eureka-0.0.1-SNAPSHOT.jar
EXPOSE 8070
ENTRYPOINT ["java", "-jar", "devtoy-eureka-0.0.1-SNAPSHOT.jar"]