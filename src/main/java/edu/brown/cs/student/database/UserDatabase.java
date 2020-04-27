package edu.brown.cs.student.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The database where all user information is stored
 */
public class UserDatabase {

  private static Connection conn = null;

  public UserDatabase(String filename) throws SQLException, ClassNotFoundException {

    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
    // these two lines tell the database to enforce foreign keys during operations, and should be present
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");

    PreparedStatement prep;
    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users("
        + "username TEXT,"
        + "password TEXT,"
        + "email TEXT,"
        + "id TEXT,"
        + "genre TEXT);");
    prep.executeUpdate();
  }

  public void addNewUser(String username, String password, String email, String id) throws SQLException {
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
    prep.setString(4, id);
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


