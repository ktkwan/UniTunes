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


//import edu.brown.cs.student.brown_spotify.Song;
//import edu.brown.cs.student.kdtree.Coordinates;
//import edu.brown.cs.student.kdtree.KdTreeNode;


/**
 * Autocorrect but with databases.
 *
 * Chooses to pass SQL exceptions on to the class that instantiates it.
 *
 */
public class SongDatabase {


  private static Connection conn = null;
  private static List<String> words = new ArrayList<>();
  private static final int SONG_CACHE_SIZE = 10000; // set a max size for the song cache 
  
  
  /**
   * This method overrides from the Database interface. It connects the class to
   * our database. It initializes the private variable "conn", which will be used
   * in subsequent queries.
   *
   * @param file the specific file that we want to run Dijkstra's on
   * @throws ClassNotFoundException thrown when there is an error loading a driver
   *                                to memory
   * @throws SQLException           thrown when there is an error with SQL parsing
   *                                or execution.
   */
  @Override
  public void connect(String file) throws ClassNotFoundException, SQLException {
    // this line loads the driver manager class, and must be
    // present for everything else to work properly
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
    // these two lines tell the database to enforce foreign keys during operations,
    // and should be present
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
  }
  
  /**
   * This method overrides from the Database interface. It is called by the
   * CommandManager. When a different path is inputed using the "mdb" command, we
   * want to empty our cache by calling invalidateAll on all of our caches.
   */
  public void invalidate() {
    actorMap.invalidateAll();
    filmMap.invalidateAll();
    nameToIdMap.invalidateAll();
    idToNameMap.invalidateAll();
    filmIdToNameMap.invalidateAll();
    numActorMap.invalidateAll();
  }

  /**
   * Instantiates the database, creating tables if necessary.
   * Automatically loads files.
   * @param filename file name of SQLite3 database to open.
   * @throws SQLException if an error occurs in any SQL query.
   */
  public SongDatabase(String filename) throws SQLException, ClassNotFoundException {

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
		+ "popularity INTEGER,"
		+ "duration INTEGER,"
		+ "album_art TEXT,"
		+ "danceability INTEGER,"
		+ "energy INTEGER,"
		+ "loudness INTEGER,"
		+ "speechiness INTEGER,"
		+ "acousticness INTEGER,"
		+ "liveness INTEGER,"
		+ "tempo INTEGER)");
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
	
<<<<<<< HEAD
	/*
	 * gets all the songs in the database and creates a list of songs.
	 * @return: list of songs 
	 */
	 public List<Song> getSongs() throws SQLException {
		 	List<Song> songs = new ArrayList<Song>(); 
		    String name = ""; 
		    String spotify_id = ""; 
		    String genre = ""; 
		    Integer duration = null; 
		    Integer popularity = null; 
		    String query = "SELECT track_name, spotify_id, genre, duration, popularity from SongDatabase";
		    try (PreparedStatement prep = conn.prepareStatement(query)) {
		      try (ResultSet rs = prep.executeQuery()) {
		        while (rs.next()) {
		          name = rs.getString(1);
		          spotify_id = rs.getString(2); 
		          genre = rs.getString(3); 
		          duration = rs.getInt(4); 
		          popularity = rs.getInt(5); 
//		          Song s = new Song(null, null, null, spotify_id); 
//		          s.setClassifiable(genre, duration, popularity);
//		          s.setData(name, spotify_id);
//		          songs.add(s); 
		        }
		        rs.close();
		      } catch (SQLException e1) {
		        throw (e1);
		      }
		      prep.close();
		    } catch (SQLException e2) {
		      throw (e2);
		    }
		    return songs;
		  }

	
	
	/*
	 * creates a cache that for each cluster, finds all the songs within that cluster 
	 */
	
=======
  /**
   * It connects the class to
   * our database. It initializes the private variable "conn", which will be used
   * in subsequent queries.
   *
   * @param fileName the specific file that we want to run Dijkstra's on
   * @throws ClassNotFoundException class not found
   * @throws SQLException uh o h
   **/
  public void connect(String fileName) throws ClassNotFoundException, SQLException {
    ResultSet rs = null;

    // this line loads the driver manager class, and must be
    // present for everything else to work properly
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + fileName;
    this.conn = DriverManager.getConnection(urlToDB);

    // these two lines tell the database to enforce foreign keys during operations,
    // and should be present
    Statement stat = conn.createStatement();

    // Check if table names are correct
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
    String[] tablenames = {"node", "way"};
    DatabaseMetaData dmd = conn.getMetaData();
    for (String name : tablenames) {
      rs = dmd.getTables(null, null, name, null);
      if (!rs.next()) {
        throw new SQLException();
      }
    }
    rs.close();
  }
  
  
>>>>>>> rzuoBranch

 
}


