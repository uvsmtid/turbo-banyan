FROM openjdk:8-jdk-alpine

RUN addgroup -S uvsmtid && adduser -S uvsmtid -G uvsmtid
USER uvsmtid:uvsmtid

VOLUME /tmp

COPY target/turbo-banyan-student-service.jar turbo-banyan-student-service.jar

ENTRYPOINT [ "java" , "-jar" , "/turbo-banyan-student-service.jar" ]
