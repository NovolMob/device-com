FROM gradle:7-jdk11 AS build
ENV PORT=3000
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src/admin-web-app
RUN gradle build

FROM nginx
RUN mkdir app
RUN mkdir app/admin
RUN mkdir app/general
COPY --from=build /home/gradle/src/admin-web-app/build/distributions/ /app/admin
COPY --from=build /home/gradle/src/admin-web-app/build/distributions/ /app/general
COPY --from=build /home/gradle/src/nginx.conf /etc/nginx/conf.d/default.conf