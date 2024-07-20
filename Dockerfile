FROM openjdk:8u302-jdk-bullseye
WORKDIR /app
COPY ./target/retailorders.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Xms512m","-Xmx512m", "-XX:+HeapDumpOnOutOfMemoryError","-XX:HeapDumpPath=/app/logs/retailorders-heap.hprof","-jar","app.jar"]