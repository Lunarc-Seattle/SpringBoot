FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app


RUN mvn clean package -X

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package

FROM openjdk:21-jdk AS runner

WORKDIR /app
RUN mvn clean package -X


COPY --from=builder ./app/target/analytics-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 4002
# zhelishao

ENTRYPOINT ["java", "-jar", "app.jar"]