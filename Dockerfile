FROM openjdk:8

WORKDIR /app

COPY ./release/mdds-*.jar ./mdds.jar

RUN ls

CMD [ "java", "-jar", "./mdds.jar"]
