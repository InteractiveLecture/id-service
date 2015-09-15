FROM java:8
RUN mkdir /lecture
WORKDIR /lecture
COPY build/distributions/id-service.tar id-service.tar
RUN tar -xf id-service.tar
EXPOSE 8080
ENTRYPOINT id-service/bin/id-service
