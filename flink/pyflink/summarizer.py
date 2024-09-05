from pyflink.common.typeinfo import Types
from pyflink.datastream import StreamExecutionEnvironment
from pyflink.datastream.connectors.kafka import FlinkKafkaConsumer
from pyflink.datastream.connectors.kafka import FlinkKafkaProducer
from pyflink.datastream.formats.json import JsonRowDeserializationSchema
from pyflink.datastream.formats.json import JsonRowSerializationSchema
from pyflink.datastream.functions import MapFunction
from pyflink.common import Row
from pyflink.datastream.connectors.elasticsearch import Elasticsearch7SinkBuilder, ElasticsearchEmitter




import spacy
from spacytextblob.spacytextblob import SpacyTextBlob
import pytextrank
import os
import re
import html as ihtml

from bs4 import BeautifulSoup

nlp = spacy.load("it_core_news_md")
nlp.add_pipe("textrank")
nlp.add_pipe('spacytextblob')


class SummaryMap(MapFunction):
    def clean_text(text):
        text = BeautifulSoup(ihtml.unescape(text)).text
        text = re.sub(r"http[s]?://\S+", "", text)
        text = re.sub(r"\s+", " ", text)    
        return text

    def map(self, value:Row):
        _to = value['to']
        _from = value['from']
        _time_sent= value['time_sent']
        _message = SummaryMap.clean_text(value['message'])
        _summary = ""

        doc = nlp(_message)

        _polarity = doc._.blob.polarity                            #
        _subjectivity =  doc._.blob.subjectivity  
        for sent in doc._.textrank.summary(limit_phrases=2, limit_sentences=2):
            # print(sent)
            _summary += sent.text
        _enrichedMail = Row(_to,_from,_message,_time_sent,_summary,_polarity,_subjectivity)
        return _enrichedMail



# Setup
def setup():
    env = StreamExecutionEnvironment.get_execution_environment()
    # the sql connector for kafka is used here as it's a fat jar and could avoid dependency issues
    env.add_jars("file:///flink-sql-connector-kafka-1.17.2.jar",
                 "file:///flink-sql-connector-elasticsearch7-3.0.1-1.17.jar")

    deserialization_schema = JsonRowDeserializationSchema.builder() \
        .type_info(type_info=Types.ROW_NAMED(field_names=["to","from","message","time_sent"],
                                             field_types=[Types.STRING(), Types.STRING(), Types.STRING(), Types.STRING()])).build()

    kafka_consumer = FlinkKafkaConsumer(
        topics='mails',
        deserialization_schema=deserialization_schema,
        properties={'bootstrap.servers': 'broker:9092', 'group.id': 'test_group'})

    # datastream
    ds = env.add_source(kafka_consumer)


    # summarization
    summaries = ds.map(SummaryMap(),
                        output_type=Types.ROW_NAMED(field_names=["to","from","message","time_sent","summary","polarity","subjectivity"],
                                                     field_types=[Types.STRING(), Types.STRING(),Types.STRING(),Types.STRING(), Types.STRING(),Types.FLOAT(),Types.FLOAT()]))
    # summaries = ds.map(SummaryMap(), output_type=Types.STRING())

    serialization_schema = JsonRowSerializationSchema.builder().with_type_info(
                        type_info=Types.ROW_NAMED(field_names=["to","from","message","time_sent","summary","polarity","subjectivity"],
                                                  field_types=[Types.STRING(), Types.STRING(),Types.STRING(),Types.STRING(), Types.STRING(),Types.FLOAT(),Types.FLOAT()])).build()
        

    kafka_producer = FlinkKafkaProducer(
        topic='summary',
        serialization_schema=serialization_schema,
        producer_config={'bootstrap.servers': 'broker:9092', 'group.id': 'test_group'})
    
    #ELASTIC sink

    # es7_sink = Elasticsearch7SinkBuilder() \
    #     .set_emitter(ElasticsearchEmitter.dynamic_index('name', 'id')) \
    #     .set_hosts(['es01:9200']) \
    #     .build()

    # summaries.sink_to(es7_sink).name('es7 dynamic index sink')


    summaries.add_sink(kafka_producer)
    summaries.print()

    env.execute()

if __name__ == '__main__':
    setup()
