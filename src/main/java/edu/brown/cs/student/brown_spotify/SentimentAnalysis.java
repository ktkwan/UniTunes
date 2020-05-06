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

    public SentimentAnalysis(){

	  Properties properties = new Properties();

	  properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

	  pipeline = new StanfordCoreNLP(properties);


    }
    /*
    * @returns: Positive 1, Negative -1, Neutral 0
    */ 
    public double analyze(String args) {


        // create an empty Annotation just with the given text
        CoreDocument document = new CoreDocument(args);
        double output = 0.0 ; 

        pipeline.annotate(document);

       List<CoreSentence> sentences = document.sentences();

       for (CoreSentence sentence : sentences) {
		  sentiment = sentence.sentiment();
	       }
       if(sentiment.equals("Positive")) {
    	   output = 1.0; 
    	   
       }else if(sentiment.equals("Negative")) {
    	   output = -1.0; 
    	   
       }else {
    	   output = 0.0; 
       }

	return output;

    }
}


