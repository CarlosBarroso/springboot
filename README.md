This project is a example of java swing boot project

It has been deploy:
- api rest to expose http enpoints 
- worker to implement async messages

Main topics:
- spring boot
- api rest
- hateoas
- jpa to use repositories
- aspects
- dsl
- jmx
- integration with rabbitmq
- integration send mail to greenmail

It has been used Docker and Docker compose to deploy the stack

Images used:
- postgressql: relational database
- rabbitmq: queue system
- greenmail: mail system
- Elasticsearch: document database
- Kibana: interfaz for elastic search and apm
- Logstash: pipelines to ingest elasticsearch
- Elastic APM: apm
- grafana: monitor system
- mongodb: event store system (wire tap pattern)
- influxdb: monitor database

use the next url to access swager developer site:
- http://localhost:8090/swagger-ui.html

next steps:
------------
- use cache (redis)
- apply athentication
- use metricbeats to monitorize rabbitmq
- add config files to grafana to create datasources to elastic and 
- apply unit test
- apply channels and queues to api 
- create mvc front
- nginx as reverse proxy
