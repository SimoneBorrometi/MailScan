# Logstash > 8.10 rompe la funzionallità del plugin imap
FROM docker.elastic.co/logstash/logstash:8.9.2

RUN logstash-plugin install logstash-input-imap

# Potrebbero non essere necessari 
USER root
RUN apt-get update && apt-get upgrade -y
RUN apt-get install netbase
USER logstash

RUN rm -f /usr/share/logstash/pipeline/logstash.conf
COPY pipeline/imap.conf /usr/share/logstash/pipeline/imap.conf
COPY config/ /usr/share/logstash/config/
