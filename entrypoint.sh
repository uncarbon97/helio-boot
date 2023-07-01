#!/bin/sh
echo "The application will start ..."
exec java ${JAVA_OPTS} ${APP_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -jar app.jar  "$@"
