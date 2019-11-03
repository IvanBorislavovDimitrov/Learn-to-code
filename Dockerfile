FROM tomcat:9.0.27-jdk13-openjdk-oracle

WORKDIR /usr/src/app

WORKDIR /usr/src/app


COPY ./com.code.to.learn.web/target/com.code.to.learn.web-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/com.code.to.learn.web-0.0.1-SNAPSHOT.war

EXPOSE 8080

