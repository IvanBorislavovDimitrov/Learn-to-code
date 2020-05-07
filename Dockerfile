FROM tomcat:9.0.34-jdk8-openjdk

ENV spring_profiles_active=dev
ENV CLIENT_URL="http://localhost:3000"

WORKDIR /usr/src/app

COPY ./com.code.to.learn.web/target/com.code.to.learn.web-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/com.code.to.learn.web-0.0.1-SNAPSHOT.war

RUN mv /usr/local/tomcat/webapps/com.code.to.learn.web-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh","run"]

EXPOSE 8080