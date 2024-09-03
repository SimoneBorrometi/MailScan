FROM  flink:1.17.2

# install python3 and pip3
RUN apt-get update -y && \
apt-get install -y python3 python3-pip python3-dev python3-bs4 git && rm -rf /var/lib/apt/lists/*
RUN ln -s /usr/bin/python3 /usr/bin/python


# install PyFlink
# COPY apache-flink*.tar.gz /
RUN python -m pip install apache-flink==1.17.2
COPY ./flink-sql-connector-kafka-1.17.2.jar /


#Install Python dependecies
RUN python -m pip install spacy
RUN python -m spacy download it_core_news_md
RUN python -m pip install pytextrank



#Load and exectute the file
COPY ./summarizer.py /
ENTRYPOINT [ "python", "/summarizer.py"]
# ENTRYPOINT [ "bash" ]