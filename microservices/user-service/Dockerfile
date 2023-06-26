FROM openjdk:8-jre-slim
RUN mkdir -p /app
COPY target/user-service-*.jar  /app/user-service.jar
EXPOSE 9092
CMD ["java","-jar", "/app/user-service.jar"]
