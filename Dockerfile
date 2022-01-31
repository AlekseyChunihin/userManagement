FROM tomcat:jdk11

COPY target/usermanagement.war /usr/local/tomcat/webapps/usermanagement.war

EXPOSE 8080
CMD ["catalina.sh", "run"]