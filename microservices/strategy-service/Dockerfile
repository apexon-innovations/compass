FROM openjdk:17-alpine
RUN mkdir -p /app
COPY target/strategy-service-*.jar  /app/strategy-service.jar
EXPOSE 9093
CMD ["java","-jar", "/app/strategy-service.jar"]
