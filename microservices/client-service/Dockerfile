FROM openjdk:17-alpine
RUN mkdir -p /app
COPY target/client-dashboard-service-*.jar  /app/client-dashboard-service.jar
EXPOSE 9095
CMD ["java","-jar", "/app/client-dashboard-service.jar"]
