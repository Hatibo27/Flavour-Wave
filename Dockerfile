FROM maven:3.8.5-openjdk-11 AS build
COPY..
RUN mvn clean package -DskipTests

FROM openjdk:11.0.16-jdk-slim
COPY --from=build /target/cke-ecommerce-0.0.1-SNAPSHOT.jar cke-ecommerce.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","cke-ecommerce.jar"]
