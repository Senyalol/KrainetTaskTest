FROM maven:3.9.9-eclipse-temurin-23 AS build

WORKDIR /auth

COPY . .

RUN mvn clean package -DskipTests


FROM eclipse-temurin:23-jre-alpine

WORKDIR /auth

COPY --from=build /auth/target/auth.jar /auth/auth.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "auth.jar"]