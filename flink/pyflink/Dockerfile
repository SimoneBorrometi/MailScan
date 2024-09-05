FROM  flink:1.17.2

# install python3 and pip3
RUN apt-get update -y && \
apt-get install -y python3 python3-pip python3-dev python3-bs4 git && rm -rf /var/lib/apt/lists/*
RUN ln -s /usr/bin/python3 /usr/bin/python




<<<<<<< HEAD
=======
# install PyFlink
RUN python -m pip install apache-flink==1.17.2
>>>>>>> cf2e5b4 (Added grafana and elasticsearch; polarity and subjectivity calculation)
#Install Python dependecies
RUN python -m pip install spacy
RUN python -m spacy download it_core_news_md
RUN python -m pip install pytextrank
<<<<<<< HEAD
# install PyFlink
RUN python -m pip install apache-flink==1.17.2
=======
RUN python -m pip install spacytextblob
RUN python -m textblob.download_corpora
>>>>>>> cf2e5b4 (Added grafana and elasticsearch; polarity and subjectivity calculation)


# COPY apache-flink*.tar.gz /
COPY ./flink-sql-connector-kafka-1.17.2.jar /
COPY ./flink-sql-connector-elasticsearch7-3.0.1-1.17.jar /



#Load and exectute the file
COPY ./summarizer.py /
ENTRYPOINT [ "python", "/summarizer.py"]
# ENTRYPOINT [ "bash" ]