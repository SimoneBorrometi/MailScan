Progetto per Technologies for Advanced Programming

# Setup

```
$ docker network create --subnet=10.0.100.0/24 testing
```

### Mailserver testing

```
$ docker pull stalwartlabs/mail-server:latest

$ mkdir ./stalwart-mail

$ docker run -d -ti -p 443:443 -p 8080:8080 \
                   -p 25:25 -p 587:587 -p 465:465 \
                   -p 143:143 -p 993:993 -p 4190:4190 \
                   -v ./stalwart-mail:/opt/stalwart-mail \
                    --network testing \
                   --name my_stalwart stalwartlabs/mail-server:latest
```
Ottieni i dati di accesso
```
$ docker logs my_stalwart
```
```
$ ./stalwart-cli -u https://localhost import messages -f mbox john /path/to/file.mbox
```
### Logstash
```
$ docker build logstash/ --tag logstash:tappro
$ docker run -it --network testing --rm  --name logstap logstash:tappro
```