# Logstash > 8.10 rompe la funzionallità del plugin imap
FROM docker.elastic.co/logstash/logstash:8.9.2

RUN logstash-plugin install logstash-input-imap

# ENV TZ=Europe/Rome

# Potrebbero non essere necessari 
USER root
RUN apt-get update && apt-get upgrade -y
RUN apt-get install netbase
USER logstash

# Rimuovi la configurazione di default
RUN rm -f /usr/share/logstash/pipeline/logstash.conf
# Importa i file di config
COPY pipeline/imap.conf /usr/share/logstash/pipeline/imap.conf
COPY config/ /usr/share/logstash/config/

# L'entrypoint configura il keystore e lancia logstash
COPY "entrypoint.sh" . 
ENTRYPOINT [ "./entrypoint.sh" ]