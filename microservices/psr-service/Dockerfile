FROM openjdk:17-alpine
RUN mkdir -p /app
COPY target/psr-service-*.jar  /app/psr-service.jar
EXPOSE 9091
CMD ["java","-jar", "/app/psr-service.jar"]
