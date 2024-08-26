package org.dmi.mailscanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import static edu.stanford.nlp.neural.rnn.RNNCoreAnnotations.getPredictedClass;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentClass;
import edu.stanford.nlp.trees.Tree;

public class AnalyzeSentiment extends RichMapFunction<Email, 
    Tuple2<Email,
     Tuple2<List<Integer>, List<String>>>> {


    private StanfordCoreNLP pipeline;


    @Override
    public void open(Configuration configuration) {
        Properties properties = new Properties();
        properties.setProperty(
                "annotators",
                "tokenize, ssplit, parse, sentiment");
        //Crea frasi da massimo X token
        properties.setProperty("parse.maxlen", "20");

        pipeline = new StanfordCoreNLP(properties);
    }

    @Override
    public Tuple2<Email, Tuple2<List<Integer>, List<String>>> map(Email email) {
        return new Tuple2<>(
                email,
                getSentiment(email.getMessage()));
    }

    private Tuple2<List<Integer>, List<String>> getSentiment(String message) {
        List<Integer> scores = new ArrayList<>();
        List<String> classes = new ArrayList<>();

        if (message != null && !message.isEmpty()) {
            Annotation annotation = pipeline.process(message);

            annotation.get(SentencesAnnotation.class).forEach(sentence -> {
                // sentiment score
                Tree tree = sentence
                        .get(SentimentAnnotatedTree.class);
                scores.add(getPredictedClass(tree));

                // sentiment class
                classes.add(sentence.get(SentimentClass.class));
            });
        }

        return new Tuple2<>(scores, classes);
    }
}
