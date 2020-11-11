There are the next containers:
- api (spring boot, publish actuator endpoints)
- postgresql database
- elasticsearch
- kibana
- logstash (check the api actuators endpoints)
- Apm server

Next step:
- process the logstash messages to parse json
- split into two differnt pipelines the two actuator calls
- integrate api log into elastic search
- prepare kibana dashboards
- use env files into docker-compose.yml file
- into docker-compose, for api, replace the with build section
- review jmx to ingress messages into elastic search