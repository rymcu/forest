FROM rymcu/java-container:1.0.0

MAINTAINER rymcu.com

COPY target/forest.war /usr/local/tomcat/webapps/

ENV JAVA_HOME=/usr/lib/jvm/jdk1.8.0_202

ENV PATH=$JAVA_HOME/bin:$PATH

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]

EXPOSE 8080