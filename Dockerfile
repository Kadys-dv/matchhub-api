FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/matchhub-api-*.jar app.jar
EXPOSE 8080
USER 10001
ENTRYPOINT ["java","-jar","/app/app.jar"]
