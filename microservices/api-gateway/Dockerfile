FROM openjdk:17-alpine
RUN mkdir -p /app
COPY target/api-gateway-*.jar  /app/api-gateway.jar
EXPOSE 8765
CMD ["java","-jar", "/app/api-gateway.jar"]
