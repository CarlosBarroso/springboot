#https://devops.datenkollektiv.de/creating-a-custom-rabbitmq-container-with-preconfigured-queues.html
#https://stackoverflow.com/questions/30747469/how-to-add-initial-users-when-starting-a-rabbitmq-docker-container
FROM rabbitmq:3.8.9-management-alpine

RUN rabbitmq-plugins enable --offline rabbitmq_management

EXPOSE 15671 15672

ADD rabbitmq/rabbitmq.config /etc/rabbitmq/
ADD rabbitmq/definitions.json /etc/rabbitmq/
#RUN chown rabbitmq:rabbitmq /etc/rabbitmq/rabbitmq.config /etc/rabbitmq/definitions.json
#CMD ["rabbitmq-server"]
