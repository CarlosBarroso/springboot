#docker build -f springboot.dockerfile -t springboot:v1 .
FROM alpine:latest as build

RUN apk --update --no-cache add openjdk11 curl tar bash procps

ARG USER_HOME_DIR="/root"
#ARG SHA=1c095ed556eda06c6d82fdf52200bc4f3437a1bab42387e801d6f4c56e833fb82b16e8bf0aab95c9708de7bfb55ec27f653a7cf0f491acebc541af234eded94d

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && echo "Downlaoding maven" \
  && curl -fsSL -o /tmp/apache-maven.tar.gz https://ftp.cixug.es/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz \
  \
#  && echo "Checking download hash" \
#  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
#  \
  && echo "Unziping maven" \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  \
  && echo "Cleaning and setting links" \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

RUN apk --update add git less openssh && \
    rm -rf /var/lib/apt/lists/* && \
    rm /var/cache/apk/*
RUN git clone https://github.com/CarlosBarroso/springboot.git
#RUN git clone https://github.com/elastic/apm-agent-java.git

WORKDIR /springboot	 

RUN mvn package -Dmaven.test.skip=true 

#WORKDIR /apm-agent-java
#RUN mvn clean install -DskipTests=true -Dmaven.javadoc.skip=true

RUN curl -fsSL -o elastic-apm-agent-1.19.0.jar https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.19.0/elastic-apm-agent-1.19.0.jar

FROM alpine:latest
WORKDIR /root

RUN apk --update --no-cache add openjdk11

COPY --from=build /springboot/worker/target/worker-0.0.1-SNAPSHOT.jar worker-0.0.1-SNAPSHOT.jar
COPY --from=build /springboot/elastic-apm-agent-1.19.0.jar elastic-apm-agent-1.19.0.jar

ENTRYPOINT ["java", "-javaagent:./elastic-apm-agent-1.19.0.jar", "-Delastic.apm.server_url=http://apmserver:8200", "-Delastic.apm.service_name=worker", "-jar","worker-0.0.1-SNAPSHOT.jar"]

