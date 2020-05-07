package edu.brown.cs.student.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.student.brown_spotify.Song;
import edu.brown.cs.student.kdtree.Coordinates; 
import edu.brown.cs.student.brown_spotify.SentimentAnalysis;


/**
 * Autocorrect but with databases.
 *
 * Chooses to pass SQL exceptions on to the class that instantiates it.
 *
 */
public class SongDatabase {


  private static Connection conn = null;
  private static List<String> words = new ArrayList<>();
  private static Map<String, Song> songMap = new HashMap<String, Song>();;  // maps from id, to Song 
  private SentimentAnalysis sent; 
  

  /**
   * Instantiates the database, creating tables if necessary.
   * Automatically loads files.
   * @param filename file name of SQLite3 database to open.
   * @throws SQLException if an error occurs in any SQL query.
   */
  public SongDatabase(String filename) throws SQLException, ClassNotFoundException {
	 //initialize the sentiment analysis 
	  this.sent = new SentimentAnalysis(); 
	  

    /*
    * TODO: Initialize the database connection, turn foreign keys on,
    *  and then create the word and corpus tables if they do not exist.
     */
	  
	  Class.forName("org.sqlite.JDBC");
	  String urlToDB = "jdbc:sqlite:" + filename; 
	  conn=DriverManager.getConnection(urlToDB); 
	  // these two lines tell the database to enforce foreign keys during operations, and should be present 
	  Statement stat = conn.createStatement(); 
	  stat.executeUpdate("PRAGMA foreign_keys=ON;"); 
	  
	  PreparedStatement prep;
		prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS songs("
		+ "track_name TEXT,"
		+ "artist TEXT,"
		+ "spotify_id TEXT,"
		+ "genre TEXT,"
		+ "popularity DOUBLE,"
		+ "duration DOUBLE,"
		+ "album_art TEXT,"
		+ "danceability DOUBLE,"
		+ "energy DOUBLE,"
		+ "loudness DOUBLE,"
		+ "speechiness DOUBLE,"
		+ "acousticness DOUBLE,"
		+ "liveness DOUBLE,"
		+ "tempo DOUBLE)");
		prep.executeUpdate();
  }

  /**
   * Reads song info from csv file.
   *
   * @param filename name of file to read.
   * @throws SQLException if something goes wrong with a SQL query.
   */
	public void read_csv(String filename) throws SQLException, ClassNotFoundException {


//    PreparedStatement prep = conn.prepareStatement("INSERT INTO songs VALUES (?, ?, ?, ?, ?, ?)");
    	List<String[]> csv = FileParsing.parse_csv(filename);
    	for (String[] song: csv) {
    		System.out.println(song);
    		PreparedStatement prep = conn.prepareStatement("INSERT INTO songs VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?)");
    		prep.setString(1, song[0]);
    		prep.setString(2, song[1]);
    		prep.setString(3, song[2]);
    		prep.setString(4, song[3]);
    		prep.setString(5, song[4]);
    		prep.setString(6, song[5]);
    		prep.setString(7, song[6]);
    		prep.setString(8, song[7]);
    		prep.setString(9, song[8]);
    		prep.setString(10, song[9]);
    		prep.setString(11, song[10]);
    		prep.setString(12, song[11]);
    		prep.setString(13, song[12]);
    		prep.setString(14, song[13]);
    		prep.executeUpdate();
    	    prep.close();
    }
  }
	
	/*
	 * gets all the songs in the database and creates a list of songs.
	 * @return: list of songs 
	 */
	 public List<Song> getSongs() throws SQLException {
		 	List<Song> songs = new ArrayList<Song>(); 
		    String name = ""; 
		    String spotify_id = ""; 
		    String genre = ""; 
		    Double duration = null; 
		    Double popularity = null; 
		    Double danceability = null;  
		    Double energy = null; 
		    Double loudness = null; 
		    Double speechiness = null; 
		    Double acousticness = null; 
		    Double liveness = null; 
		    Double tempo = null; 
		    
		    String query = "SELECT track_name, spotify_id, genre, duration, popularity,danceability, energy, loudness, speechiness, acousticness, liveness, tempo from songs";
		    try (PreparedStatement prep = conn.prepareStatement(query)) {
		      try (ResultSet rs = prep.executeQuery()) {
		        while (rs.next()) {
				  
		          name = rs.getString(1);
			
		          spotify_id =rs.getString(2); 
			
		          genre = rs.getString(3); 
		          duration = rs.getDouble(4); 
		          popularity = rs.getDouble(5); 
		          danceability = rs.getDouble(6); 
				  energy = rs.getDouble(7);
				  loudness = rs.getDouble(8); 
				  speechiness = rs.getDouble(9); 
				  acousticness = rs.getDouble(10);
				  liveness = rs.getDouble(11); 
				  tempo = rs.getDouble(12); 
				  Double[] coordValues = new Double[11]; 
				  //name 
				  coordValues[0] = this.sent.analyze(name); 
				  //genre
				  coordValues[1] = this.sent.analyze(genre); 
				  coordValues[2] = duration; 
				  coordValues[3] = popularity; 
				  coordValues[4] = danceability; 
				  coordValues[5] = energy; 
				  coordValues[6] = loudness; 
				  coordValues[7] = speechiness; 
				  coordValues[8] = acousticness;
				  coordValues[9] = liveness; 
				  coordValues[10] = tempo; 
				  Coordinates c = new Coordinates(coordValues); 
		          Song s = new Song(null, null, c, spotify_id); 
		          s.setClassifiable(genre, duration, popularity, danceability);
		          s.setData(name, spotify_id);
		          songs.add(s); 
		          songMap.put(name, s); 
				}
		        rs.close();
		      } catch (SQLException e1) {
		        throw (e1);
		      }
		      prep.close();
		    } catch (SQLException e2) {
		      throw (e2);
			}
			System.out.println("loading songs"); 
		    return songs;
		  }
	 
	 /*
	  * helper function to get a song based on song name 
	  */
	 public static Song getSongFromName(String songName) {
		 return songMap.get(songName); 
	 }

	 
	
}


