FROM openjdk:17-alpine
RUN mkdir -p /app
COPY target/onboard-service-*.jar  /app/onboard-service.jar
EXPOSE 9096
CMD ["java","-jar", "/app/onboard-service.jar"]
