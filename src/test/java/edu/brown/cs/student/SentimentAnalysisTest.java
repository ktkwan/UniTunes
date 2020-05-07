package edu.brown.cs.student;


import static org.junit.Assert.assertTrue;
import edu.brown.cs.student.brown_spotify.SentimentAnalysis;

import org.junit.Before;
import org.junit.Test;

public class SentimentAnalysisTest {
    private SentimentAnalysis analyzer;
    private String positive;
    private String neutral;
    private String negative;

    @Before
    public void analyzer() {
	analyzer = new SentimentAnalysis();
    }

    @Before
    public void createPhrases() {
	positive = new String("This is charming");
	neutral = new String("This is okay");
	negative = new String("This is very bad");
    }
    
    @Test
    public void sentimentTest() {	
	assertTrue(analyzer.analyze(positive) == 1.0);
	assertTrue(analyzer.analyze(neutral) == 0.0);
	assertTrue(analyzer.analyze(negative) == -1.0);
    }
}
