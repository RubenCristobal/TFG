FROM jenkins/ssh-agent:alpine-jdk8
RUN delgroup ping
RUN addgroup -g 999 docker
RUN addgroup jenkins docker
RUN apk add docker
RUN apk add docker-compose
RUN apk add git