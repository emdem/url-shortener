FROM java:8-jdk
MAINTAINER linux_china linux_china@hotmail.com

ADD target/hsf-server-demo-0.0.1-SNAPSHOT.jar /opt/url-shortener-0.0.1-SNAPSHOT.jar


EXPOSE 8080

CMD java -jar /opt/url-shortener-0.0.1-SNAPSHOT.jar --info.app.domain="${DOMAIN_NAME}" --spring.redis.host="${REDIS_HOST}"