FROM  flink:1.17.2

# install python3 and pip3
RUN apt-get update -y && \
apt-get install -y python3 python3-pip python3-dev python3-bs4 git && rm -rf /var/lib/apt/lists/*
RUN ln -s /usr/bin/python3 /usr/bin/python




# install PyFlink
RUN python -m pip install apache-flink==1.17.2
#Install Python dependecies
RUN python -m pip install spacy
RUN python -m pip install pytextrank
RUN python -m pip install spacytextblob
RUN python -m textblob.download_corpora
RUN python -m spacy download it_core_news_md


# COPY apache-flink*.tar.gz /
COPY ./flink-sql-connector-kafka-1.17.2.jar /

COPY ./summarizer.py /


#Load and exectute the file
# ENTRYPOINT [ "python", "/summarizer.py"]
# ENTRYPOINT [ "bash" ]