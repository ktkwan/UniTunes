package edu.brown.cs.student.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import edu.brown.cs.ngao.timdb.Film;
import edu.brown.cs.student.kdtree.Coordinates;
import edu.brown.cs.student.kdtree.KdTreeNode;
import src.main.java.edu.brown.cs.student.brown_spotify.User;

/**
 * The database where all user information is stored
 */
public class UserDatabase {

  private static Connection conn = null;
  public static String connection_filename = null;
  private static final int USER_CACHE_SIZE = 10000;
  private static final int EXPIRE_AFTER_WRITE = 10;
  private static LoadingCache<String, User> userMap;

  public UserDatabase(String filename) throws SQLException, ClassNotFoundException {

    if (conn != null) {
      conn.close();
      conn = null;
      connection_filename = null;
    }

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
        + "id INTEGER,"
        + "genre TEXT);");
    prep.executeUpdate();
  }
  

  public void invalidate() {
	  this.userMap.invalidateAll();
  }

  /**
   * This method initializes all of our caches. The maximum size of each cache is
   * set by trial and error, which we found to be the most optimal our the
   * searches.
   *
   * @throws ExecutionException thrown when attempting to retrieve the result of a
   *                            task that aborted
   */
  private void initialize() throws ExecutionException {
    // actor map gets all of the edges of an actor. For each actor, stores all of
    // the movies that the actor is in. Key is an actor id
    this.userMap = CacheBuilder.newBuilder().maximumSize(USER_CACHE_SIZE)
        .expireAfterWrite(EXPIRE_AFTER_WRITE, TimeUnit.MINUTES)
        .build(new CacheLoader<Integer, User>() {
		@Override
		public User load(Integer userID) throws Exception {
			return getUserGivenId(userID);
		}
        });
  }
  
  /*
   * queries the user database and creates a user 
   * @input: userName
   * @output: User
   */
  private User getUserGiveId(String userName) {
    String id; 
    String query = "SELECT username, id FROM users WHERE username = ?";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setString(1, userName);
      try (ResultSet rs = prep.executeQuery()) {
        id = rs.getString(1);
        // create the new user here and cache it 
        User newUser = new User(null, null, null, id); 
        newUser.setData(userName, id); 
        rs.close();
      } catch (SQLException e1) {
        throw (e1);
      }
      prep.close();
    } catch (SQLException e2) {
      throw (e2);
    }
    return newUser; 
  }
  
  /**
   * Getter for userMap.
   *
   * @return userMap
   */
  public LoadingCache<Integer, User> getNumActorMap() {
    return userMap;
  }

  /*
   * Adds a new user to the database and creates a new user
   */
  public static void addNewUser(String username, String password, String email, Integer id) throws SQLException {
    PreparedStatement prep;
    prep = conn.prepareStatement("INSERT INTO " +
        "users " +
        "(username, " +
        "password, " +
        "email, " +
        "id) VALUES (?, ?, ?, ?)");
    prep.setString(1, username);
    prep.setString(2, password);
    prep.setString(3, email);
    prep.setInt(4, id);
    prep.executeUpdate();
    prep.close();

  }

  public void addTopGenreForUser(String id, String genre) throws SQLException {
    PreparedStatement prep;
    prep = conn.prepareStatement("INSERT INTO users(genre) VALUES(?) WHERE (users.id = ?);");
    prep.setString(1, genre);
    prep.setString(2, id);
    prep.executeUpdate();
    prep.close();
  }

}


