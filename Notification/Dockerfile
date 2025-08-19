FROM maven:3.9.9-eclipse-temurin-23 AS build

WORKDIR /notification

COPY . .

RUN mvn clean package -DskipTests


FROM eclipse-temurin:23-jre-alpine

WORKDIR /notification

COPY --from=build /notification/target/notification.jar /notification/notification.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "notification.jar"]
