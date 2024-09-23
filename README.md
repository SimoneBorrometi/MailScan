Progetto per Technologies for Advanced Programming

Run with
```
docker compose up
```

Web uis
- [Stalwart](http://localhost:8080)
- [Redpanda (kafka ui)](http://localhost:6060)
- [Grafana](http://localhost:3000)
- [Flink UI](http://localhost:8081) (*IF* run in session or application mode)

## Setup
### Mailserver testing

Get [stalwart-cli](https://github.com/stalwartlabs/mail-server/releases/tag/v0.8.1)

```
mkdir stalwart-mail
```

Run docker
The first time stalwart is run, it will print the admin password, save it

###### Via web UI
Set hostname to 'localhost' and allow IMAP plain text auth then reload

###### Via CLI

Create domain and user 
```
stalwart-cli -u https://localhost domain create 'example.org'

stalwart-cli -u https://localhost account create -d "John Snow" -i false -a "john@example.org" john 123456

stalwart-cli -u https://localhost import messages -f mbox john /path/to/file.mbox
```
### Logstash
Create directory secrets
Create file secrets/logstash-kewystore and secrets/user-passowrd and fill them.

### Flink 
Download the kafka connector
```
cd flink/pyflink/
wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-kafka/1.17.2/flink-sql-connector-kafka-1.17.2.jar
```


### Grafana
Import the dashboard configurations from ./grafana via web ui, manually configure the elasticsearch source (just the url)