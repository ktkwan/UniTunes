package edu.brown.cs.student.brown_spotify;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public final class DatabaseConnection {
	public static String connection_filename = null;
	private static Connection conn = null;
	public static HashMap<String, String> song_hashmap = new HashMap<>();


	/**
	 * Creates a connection to the database. Drops the current database if a new
	 * filename is passed in.
	 */
	public static void createConnection(String filename)
			throws SQLException, ClassNotFoundException {
		if (conn != null) {

			// Clear caches
			// FIXME clear clear clear
			
			// close connection
			conn.close();
			conn = null;
			connection_filename = null;
		}

		connection_filename = filename;
		Class.forName("org.sqlite.JDBC");
		String urlToDB = "jdbc:sqlite:" + filename;
		conn = DriverManager.getConnection(urlToDB);
		Statement stat = conn.createStatement();
		stat.executeUpdate("PRAGMA foreign_keys=ON;");
		stat.close();

	}
	

	public static String getGenreFromSongName(String song_name) throws SQLException {
		String genre = "";
		
		PreparedStatement prep;
		String statement = "SELECT genre FROM songs WHERE track_name=?;";
		prep = conn.prepareStatement(statement);
		prep.setString(1, song_name);
		ResultSet res = prep.executeQuery();
		
		while (res.next()) {
			genre = res.getString(1);
		}
		
		
		return genre;
	}
	
	public static HashMap<String, String> getAllSongNames() throws SQLException {
		HashMap<String, String> id_to_name = new HashMap<>();
		song_hashmap = new HashMap<>();
		PreparedStatement prep;
		String statement = "SELECT DISTINCT track_name, spotify_id FROM songs;";
		prep = conn.prepareStatement(statement);
		ResultSet res = prep.executeQuery();
		while (res.next()) {
			id_to_name.put(res.getString(2), res.getString(1));
			song_hashmap.put(res.getString(2), res.getString(1));
		}
		
//		song_hashmap = id_to_name;
//		song_hashmap = id_to_name;
		return id_to_name;
		
	}
	
	public static double getDurationFromSongName(String song_name) throws SQLException {
		double duration = 0;
		
		PreparedStatement prep;
		String statement = "SELECT duration FROM songs WHERE track_name=?;";
		prep = conn.prepareStatement(statement);
		prep.setString(1, song_name);
		ResultSet res = prep.executeQuery();
		
		while (res.next()) {
			duration = Double.parseDouble(res.getString(1));
		}
		
		
		return duration;
	}
	
	public static double getPopularityFromSongName(String song_name) throws SQLException {
		double popularity = 0;
		PreparedStatement prep;
		String statement = "SELECT popularity FROM songs WHERE track_name=?;";
		prep = conn.prepareStatement(statement);
		prep.setString(1, song_name);
		ResultSet res = prep.executeQuery();
		
		while (res.next()) {
			popularity = Double.parseDouble(res.getString(1));
		}
		

		
		return popularity;
	}
	
	/**
	 * get artist from the song name
	 * */
	public static String getArtistFromSongID(String song_id) throws SQLException {
		String artist = "";
		PreparedStatement prep;
		String statement = "SELECT artist FROM songs WHERE spotify_id=?;";
		prep = conn.prepareStatement(statement);
		prep.setString(1, song_id);
		ResultSet res = prep.executeQuery();
		
		while (res.next()) {
			artist = res.getString(1);
		}
		

		
		return artist;
	}
	
	/**
	 * gets link to the track : use later when we need to play songs
	 * */
	public static String getSpotifyLinkFromSongName(String song_name) throws SQLException {
		String spotify_id = "";
		PreparedStatement prep;
		String statement = "SELECT spotify_id FROM songs WHERE track_name=?;";
		prep = conn.prepareStatement(statement);
		prep.setString(1, song_name);
		ResultSet res = prep.executeQuery();
		String base_url = "open.spotify.com/track/";
		while (res.next()) {
			spotify_id = res.getString(1);
		}

		return base_url + spotify_id;
	}
	
	public static String getAlbumArt(String song_id) throws SQLException {
		String album_art = "";
		PreparedStatement prep;
		String statement = "SELECT album_art FROM songs WHERE spotify_id=?;";
		prep = conn.prepareStatement(statement);
		prep.setString(1, song_id);
		ResultSet res = prep.executeQuery();
		while (res.next()) {
			album_art = res.getString(1);
		}

		return album_art;
	}
	
	

	/**
	 * @return three random songs from database
	 * @throws SQLException
	 */
	public static List<String> getRandomTrackNames() throws SQLException {
		List<String> randTrackNames = new ArrayList();
		PreparedStatement prep;
		prep = conn.prepareStatement("SELECT track_name FROM songs ORDER BY RANDOM() LIMIT 3;");
		ResultSet rs = prep.executeQuery();
		while(rs.next()) {
			randTrackNames.add(rs.getString(1));
		}
		return randTrackNames;
	}
	
	public static String getSpotifyLinkFromID(String spotify_id) throws SQLException {
		String base_url = "open.spotify.com/track/";
		
		return base_url + spotify_id;
	}

	
	/*Not sure if we need just the id, but made it just in case */
	public static String getIDFromSongName(String song_name) throws SQLException {
		String spotify_id = "";
		PreparedStatement prep;
		String statement = "SELECT spotify_id FROM songs WHERE track_name=?;";
		prep = conn.prepareStatement(statement);
		prep.setString(1, song_name);
		ResultSet res = prep.executeQuery();
		while (res.next()) {
			spotify_id = res.getString(1);
		}
		

		
		return spotify_id;
	}

	/**
	 * Prints out a table if you give it the table name : debugging puposes
	 */
	private static void showTable(String tableName) throws SQLException {
		PreparedStatement prep;
		String statement = "SELECT * from " + tableName + ";";
		prep = conn.prepareStatement(statement);
		ResultSet res = prep.executeQuery();
		printResultSet(res);
		prep.close();
	}

	/**
	 * Prints a ResultSet Object NOTE: Because result set is an iterator it will
	 * be unusable after printing
	 * Debugging purposes
	 */
	private static void printResultSet(ResultSet res) throws SQLException {
		ResultSetMetaData resMD = res.getMetaData();
		int colNum = resMD.getColumnCount();

		// Printing columns
		String columns = "";
		for (int i = 1; i <= colNum; i += 1) {
			columns += resMD.getColumnName(i);
			if (i < colNum) {
				columns += ", ";
			}
		}
		System.out.println(columns);

		// Printing values by row
		while (res.next()) {
			String value = "";
			for (int i = 1; i <= colNum; i += 1) {
				value += res.getString(i);
				if (i < colNum) {
					value += ", ";
				}
			}
			System.out.println(value);
		}
	}
	
	
}
