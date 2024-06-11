Progetto per Technologies for Advanced Programming

Run with
```
docker compose up
```

### Mailserver testing

Ottieni i dati di accesso
```
$ docker logs my_stalwart
```
#### Only once
Ottieni [stalwart-cli](https://github.com/stalwartlabs/mail-server/releases/tag/v0.8.1)

Crea dominio e utente 
```
$ stalwart-cli -u https://localhost domain create 'example.org'

$ stalwart-cli -u https://localhost account create -d "John Snow" -i false -a "john@example.org" john 123456

$ stalwart-cli -u https://localhost import messages -f mbox john /path/to/file.mbox
```