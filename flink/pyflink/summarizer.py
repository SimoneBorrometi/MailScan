from pyflink.common.typeinfo import Types
from pyflink.datastream import StreamExecutionEnvironment
from pyflink.datastream.connectors.kafka import FlinkKafkaConsumer
from pyflink.datastream.connectors.kafka import FlinkKafkaProducer
from pyflink.datastream.formats.json import JsonRowDeserializationSchema
from pyflink.datastream.formats.json import JsonRowSerializationSchema
from pyflink.datastream.functions import MapFunction
from pyflink.common import Row


import spacy
import pytextrank
import os
import re
import html as ihtml

from bs4 import BeautifulSoup

nlp = spacy.load("it_core_news_md")
nlp.add_pipe("textrank")

class SummaryMap(MapFunction):
    def clean_text(text):
        text = BeautifulSoup(ihtml.unescape(text)).text
        text = re.sub(r"http[s]?://\S+", "", text)
        text = re.sub(r"\s+", " ", text)    
        return text

    def map(self, value:Row):
        _to = value['to']
        _from = value['from']
        _message = SummaryMap.clean_text(value['message'])
        _summary = ""

        doc = nlp(_message)
        for sent in doc._.textrank.summary(limit_phrases=5, limit_sentences=5):
            # print(sent)
            _summary += sent.text
        _enrichedMail = Row(_to,_from,_message,_summary)
        return _enrichedMail



# Setup
def setup():
    env = StreamExecutionEnvironment.get_execution_environment()
    # the sql connector for kafka is used here as it's a fat jar and could avoid dependency issues
    env.add_jars("file:///flink-sql-connector-kafka-1.17.2.jar")

    deserialization_schema = JsonRowDeserializationSchema.builder() \
        .type_info(type_info=Types.ROW_NAMED(field_names=["to","from","message"],
                                             field_types=[Types.STRING(), Types.STRING(), Types.STRING()])).build()

    kafka_consumer = FlinkKafkaConsumer(
        topics='mails',
        deserialization_schema=deserialization_schema,
        properties={'bootstrap.servers': 'broker:9092', 'group.id': 'test_group'})

    # datastream
    ds = env.add_source(kafka_consumer)


    # summarization
    summaries = ds.map(SummaryMap(),
                        output_type=Types.ROW_NAMED(field_names=["to","from","message","summary"],
                                                     field_types=[Types.STRING(), Types.STRING(),Types.STRING(), Types.STRING()]))
    # summaries = ds.map(SummaryMap(), output_type=Types.STRING())

    serialization_schema = JsonRowSerializationSchema.builder().with_type_info(
                        type_info=Types.ROW_NAMED(field_names=["to","from","message","summary"],
                                                  field_types=[Types.STRING(), Types.STRING(),Types.STRING(), Types.STRING()])).build()
        

    kafka_producer = FlinkKafkaProducer(
        topic='summary',
        serialization_schema=serialization_schema,
        producer_config={'bootstrap.servers': 'broker:9092', 'group.id': 'test_group'})

    summaries.add_sink(kafka_producer)
    summaries.print()

    env.execute()

if __name__ == '__main__':
    setup()
