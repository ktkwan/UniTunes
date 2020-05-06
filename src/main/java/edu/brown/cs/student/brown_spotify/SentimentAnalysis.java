package edu.brown.cs.student.brown_spotify;


import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;


public class SentimentAnalysis{
    private static String sentiment;
    private static StanfordCoreNLP pipeline;

    public static void main( String args ){

	  Properties properties = new Properties();

	  properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

	  pipeline = new StanfordCoreNLP(properties);


    }

    public static String analyze(String args) {


        // create an empty Annotation just with the given text
        CoreDocument document = new CoreDocument(args);


        pipeline.annotate(document);

       List<CoreSentence> sentences = document.sentences();

       for (CoreSentence sentence : sentences) {
		  sentiment = sentence.sentiment();
	       }

	return sentiment;

    }
}


