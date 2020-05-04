package edu.brown.cs.student.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The database where all user information is stored
 */
public class UserDatabase {

  private static Connection conn = null;
  public static String connection_filename = null;
  //This is the user library. It maps a username to a map of their songs. Their personal song map 
  //maps a song name to a song list. 
  private static Map<String, List<String>> userLibrary; 

  public UserDatabase(String filename) throws SQLException, ClassNotFoundException {

    if (conn != null) {
      conn.close();
      conn = null;
      connection_filename = null;
    }

    userLibrary = new HashMap<String, List<String>>(); 
    connection_filename = filename;
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + connection_filename;
    conn = DriverManager.getConnection(urlToDB);
    System.out.println("setting user database to" + urlToDB);
    // these two lines tell the database to enforce foreign keys during operations, and should be present
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");

    PreparedStatement prep;
    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users("
        + "username TEXT,"
        + "password TEXT,"
        + "email TEXT,"
        + "genre TEXT);");
    prep.executeUpdate();
  }


  /** 
   * Adds a user to the UserDatabase when they create an account 
   * @param username
   * @param password
   * @param email
   * @throws SQLException
   */
  public static void addNewUser(String username, String password, String email) throws SQLException {
	
    PreparedStatement prep;
    prep = conn.prepareStatement("INSERT INTO " +
        "users " +
        "(username, " +
        "password, " +
        "email" +
        ") VALUES (?, ?, ?)");
    prep.setString(1, username);
    prep.setString(2, password);
    prep.setString(3, email);
    prep.executeUpdate();
    prep.close();
    
    //Adding the user to the hashmap that stores their  library
    List<String> userSongList = new ArrayList<String>(); 
    userLibrary.put(username, userSongList); 

  }

  public void addTopGenreForUser(String id, String genre) throws SQLException {
    PreparedStatement prep;
    prep = conn.prepareStatement("INSERT INTO users(genre) VALUES(?) WHERE (users.id = ?);");
    prep.setString(1, genre);
    prep.setString(2, id);
    prep.executeUpdate();
    prep.close();
  }
  
  public void addSongToLibrary(String username, String song) { 
	  List<String> library = userLibrary.get(username); 
	  library.add(song); 
	  
  }
  
  public List<String> getUserLibrary(String username){
	  return userLibrary.get(username); 	  
  }

}


