URL Shorten Service
========================================

### Usage 
please take a look at the application.properties and change your settings.

### Generate short url

     curl -vX POST http://localhost:8080/http://www.taobao.com

### Development
please install Docker Compose and start Redis container.

### Build Docker Image

    $ mvn -DskipTests clean package
    $ docker build -t linuxchina/url-shortener . 

### Docker Compose Usage
Please add following content into your docker-compose.yml

      urlshortener:
          image: linuxchina/url-shortener
          ports:
            - "8080:8080"
          environment:
             DOMAIN_NAME: "http://mydomain.com/"
             REDIS_HOST: "192.168.59.103"
  