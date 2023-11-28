# https://hub.docker.com/_/eclipse-temurin/tags?page=1&name=alpine
FROM eclipse-temurin:17.0.7_7-jre-alpine
COPY ./target/*.jar ./app.jar
COPY entrypoint.sh /
RUN chmod +x ./entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]
