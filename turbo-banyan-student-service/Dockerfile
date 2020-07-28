FROM openjdk:8-jdk-alpine

RUN addgroup -S uvsmtid && adduser -S uvsmtid -G uvsmtid
USER uvsmtid:uvsmtid

VOLUME /tmp

# TODO: use layers to deduplicate and reduce size
COPY target/turbo-banyan-student-service.jar turbo-banyan-student-service.jar

ENTRYPOINT [ "java" , "-jar" , "/turbo-banyan-student-service.jar" ]
