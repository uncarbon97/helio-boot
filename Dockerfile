# https://hub.docker.com/_/eclipse-temurin/tags
FROM eclipse-temurin:17-jre
COPY ./target/*.jar ./app.jar
COPY entrypoint.sh /
RUN chmod +x ./entrypoint.sh && \
    ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo Asia/Shanghai > /etc/timezone
ENTRYPOINT ["./entrypoint.sh"]
