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

### Mailserver testing

Just for testing purposes
The first time stalwart is run, it will print the admin password, save it

#### Only once
Set hostname to 'localhost' via web UI

Get stalwart-cli 
```
wget https://github.com/stalwartlabs/mail-server/releases/tag/v0.8.1
```

Crea dominio e utente 
```
stalwart-cli -u https://localhost domain create 'example.org'

stalwart-cli -u https://localhost account create -d "John Snow" -i false -a "john@example.org" john 123456

stalwart-cli -u https://localhost import messages -f mbox john /path/to/file.mbox
```

### Flink setup
Download the kafka connector
```
cd flink/pyflink/
wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-kafka/1.17.2/flink-sql-connector-kafka-1.17.2.jar
```


### Grafana
Import the dashboard configurations from ./grafana via web ui, manually configure the elasticsearch source (just the url)

