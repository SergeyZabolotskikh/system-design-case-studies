FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Set debug port and suspend (waits for debugger to attach)
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

COPY target/shorten-url-service-1.0.0.jar app.jar
EXPOSE 8080 5005

ENTRYPOINT ["java", "-jar", "app.jar"]
