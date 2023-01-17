FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src/backend
RUN gradle runDocker --info