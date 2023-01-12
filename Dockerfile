FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src/backend
RUN gradle installDist --no-daemon --info

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/backend/build/install/backend/ /app
ENTRYPOINT ["/app/bin/backend"]
