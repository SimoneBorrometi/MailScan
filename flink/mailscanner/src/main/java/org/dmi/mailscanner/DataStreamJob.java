/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 package org.dmi.mailscanner;

import java.util.List;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**
 * Skeleton for a Flink DataStream Job.
 *
 * <p>For a tutorial how to write a Flink application, check the
 * tutorials and examples on the <a href="https://flink.apache.org">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class DataStreamJob {

	private static final String bootstrapServer = "broker:9092";

	public static void main(String[] args) throws Exception {
		// Sets up the execution environment, which is the main entry point
		// to building Flink applications.
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		


        JsonDeserializationSchema<Email> jsonFormat=new JsonDeserializationSchema<>(
            Email.class,
            () -> new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            );

		KafkaSource<Email> source;
            source = KafkaSource.<Email>builder()
                    .setBootstrapServers(bootstrapServer)
                    .setTopics("mails")
                    .setGroupId("org.dmi.mailscanner")
                    .setStartingOffsets(OffsetsInitializer.earliest())
                    .setValueOnlyDeserializer(jsonFormat)
                    .build();

		env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

		DataStream<Email> emails = env.fromSource(source, WatermarkStrategy.noWatermarks(), "kafka-input");

        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
            .setBootstrapServers(bootstrapServer)
            .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                .setTopic("sentiment")
                .setValueSerializationSchema(new SimpleStringSchema())
                .build()
            )
            .build();


		// emails.print().name("print-kafkaSink");
    
        DataStream<Tuple2<Email, Tuple2<List<Integer>, List<String>>>> sentiment = emails.map(new AnalyzeSentiment());
        DataStream<String> sentimentPrint = sentiment.map((tuple2) -> {
            String out = "";
            out+= tuple2.f0.toString();
            out+= tuple2.f1.toString();
            return out;
        });
        sentimentPrint.sinkTo(kafkaSink);

		env.execute("Flink Java API Skeleton");
	}

    public static String getBootstrapServer() {
        return bootstrapServer;
    }
}