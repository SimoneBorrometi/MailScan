#!/bin/bash
#le env. variable _FILE indicano la posizione dei docker compose secrets
key=$(cat $LOGSTASH_KEYSTORE_FILE)
export LOGSTASH_KEYSTORE_PASS=$key
bin/logstash-keystore create

cat $MAIL_USR_PSW_FILE | bin/logstash-keystore add MAIL_USR_PSW
/usr/local/bin/docker-entrypoint