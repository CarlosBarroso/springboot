FROM docker.elastic.co/apm/apm-server:7.9.3

COPY ./apm/apm-server.docker.yml /usr/share/apm-server/apm-server.yml
USER root
RUN chown root:apm-server /usr/share/apm-server/apm-server.yml
USER apm-server