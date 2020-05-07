package edu.brown.cs.student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import edu.brown.cs.student.kdtree.Coordinates;
import edu.brown.cs.student.kdtree.KdTree;
import edu.brown.cs.student.kdtree.KdTreeNode;


import edu.brown.cs.student.brown_spotify.Song;
import edu.brown.cs.student.brown_spotify.UniTunes;
import edu.brown.cs.student.database.SongDatabase;
import edu.brown.cs.student.database.UserDatabase;

public class UniTunesTest {
    private static SongDatabase songdb;
    private static UserDatabase userdb; 
    private static UniTunes tunes;
    
    
    @Before
    public void setup() {
	 tunes = new UniTunes(songdb, userdb);
	 tunes.setUpClusters();
    }
    
   

}
