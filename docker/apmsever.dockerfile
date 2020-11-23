FROM docker.elastic.co/apm/apm-server:7.9.3

COPY ./apm/apm-server.docker.yml /usr/share/apm-server/apm-server.yml

RUN chmod go-w /usr/share/apm-server/apm-server.yml