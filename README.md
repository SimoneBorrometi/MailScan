Progetto per Technologies for Advanced Programming

# Setup

```
<<<<<<< HEAD
$ docker network create --subnet=10.0.100.0/24 testing
<<<<<<< HEAD
=======
<<<<<<< HEAD
$ docker network create --subnet=10.0.100.0/24 testing
=======
$ docker network create --subnet=10.1.100.0/24 testing
>>>>>>> 3b52ba9 (Updated README, minor change to dockerfile)
>>>>>>> f089156 (Updated README, minor change to dockerfile)
=======

$ docker network create --subnet=10.0.100.0/24 testing
$ docker network create --subnet=10.1.100.0/24 testing
>>>>>>> 8b59dc4 (Updated README, minor change to dockerfile)
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
<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
=======
>>>>>>> 8b59dc4 (Updated README, minor change to dockerfile)
$ ./stalwart-cli -u https://localhost domain create 'example.org'

$ ./stalwart-cli -u https://localhost account create -d "John Snow" -i false -a "john@example.org" john 123456

<<<<<<< HEAD
>>>>>>> 3b52ba9 (Updated README, minor change to dockerfile)
>>>>>>> f089156 (Updated README, minor change to dockerfile)
=======
>>>>>>> 8b59dc4 (Updated README, minor change to dockerfile)
$ ./stalwart-cli -u https://localhost import messages -f mbox john /path/to/file.mbox
```
### Logstash
```
$ docker build logstash/ --tag logstash:tappro
$ docker run -it --network testing --rm  --name logstap logstash:tappro
<<<<<<< HEAD
<<<<<<< HEAD
```
=======
<<<<<<< HEAD
```
=======
```
>>>>>>> 3b52ba9 (Updated README, minor change to dockerfile)
>>>>>>> f089156 (Updated README, minor change to dockerfile)
=======
```
>>>>>>> 8b59dc4 (Updated README, minor change to dockerfile)
