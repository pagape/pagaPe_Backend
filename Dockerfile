FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Instalar Maven
RUN apk add --no-cache maven

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/target/app.jar"]
