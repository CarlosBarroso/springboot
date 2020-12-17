This project is a example of java swing boot project

It has been deploy:
-------------------
- api rest to expose http enpoints 
- worker to implement async messages

Main topics:
------------
- spring boot
- spring integration
- spring jpa
- spring actuator
- api rest
- hateoas
- repositories pattern
- swagger ui
- aspects
- dsl
- jmx
- integration with rabbitmq
- integration send mail to greenmail
- store in mongodb event with wireTap

It has been used Docker and Docker compose to deploy the stack

Images used:
------------
- postgressql: relational database
- rabbitmq: queue system
- greenmail: mail system
- Elasticsearch: document database, apm, logs... 
- Kibana: advanced dashboards, interfaz for elastic search and apm
- Logstash: pipelines to ingest elasticsearch
- Elastic APM: apm
- grafana: monitor system
- mongodb: event store system 
- influxdb: monitor database

use the next url to access swager developer site:
- http://localhost:8090/swagger-ui.html

next steps:
------------
- use cache (redis)
- apply athentication (spring security)
- use metricbeats to monitorize rabbitmq - it could be done with influxdb and grafana
- add config files to grafana to create datasources to elastic and influxdb
- web send metrics to influxdb instead of elasticserch
- apply unit test
- apply channels and queues to api 
- create mvc front
- nginx as reverse proxy
