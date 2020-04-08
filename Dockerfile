FROM openjdk:8
VOLUME /tmp
EXPOSE 8090
ADD ./target/service-zuul-server-0.0.1-SNAPSHOT.jar service-zuul-server.jar
ENTRYPOINT ["java","-jar","/service-zuul-server.jar"] 