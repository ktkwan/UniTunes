package edu.brown.cs.student.front_end;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.student.brown_spotify.DatabaseConnection;
import edu.brown.cs.student.database.SongDatabase;
import edu.brown.cs.student.database.UserDatabase;
import edu.brown.cs.student.brown_spotify.User; 
import edu.brown.cs.student.brown_spotify.Song; 
import org.eclipse.jetty.server.Authentication;
import spark.*;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Login {

  private static UserDatabase db;
  public Map<String, List> userLibrary; 
  private static String userName; 
  private static DatabaseConnection connection; 
  public static boolean loggedIn; 


  public Login(){

  }

  public static TemplateViewRoute getLoginHandler(){
    return new FrontHandler();
  }

  public static TemplateViewRoute getCreateHandler() { 
	  return new CreateHandler(); }
  
  public static TemplateViewRoute getLoadLibraryHandler() {
		return new LoadLibraryHandler();
	}
  
  public static TemplateViewRoute getVerifyLoginHandler() { 
	  return new VerifyLoginHandler(); 
  }
  /** 
   * Handles login of existing users *
   */
  public static class VerifyLoginHandler implements TemplateViewRoute {

	@Override
	public ModelAndView handle(Request request, Response response) throws Exception {
//		userName=""; 
		QueryParamsMap qm = request.queryMap(); 
		String email = qm.value("email_login");
		String password = qm.value("password_login");
		if(email.contentEquals("") || password.contentEquals("") || email == null || password == null) { 
			userName = "";
			Map<String, Object> variables = ImmutableMap.of("title",
			          "uniTunes", "status", "Enter an email and password to log in!");
		    return new ModelAndView(variables, "query.ftl");
		}	
		
		//If user exists, check if their password is correct
		else { 
	    
		if(UserDatabase.checkIfUserExists(email)) { 
			//If their password is correct, bring them to their library 			
			if(UserDatabase.verifyPasswordWithEmail(email, password)) { 
				userName = UserDatabase.getUserFromEmailAndPassword(email, password); 
				//if username is null or empty, reload home page 
				if(userName == null || userName.contentEquals("")) { 
					Map<String, Object> variables = ImmutableMap.of("title",
					          "uniTunes", "status", "Enter an email and password to log in!");
				    return new ModelAndView(variables, "query.ftl");
				}					
				System.out.println("User exists and correct password");
				List<String> libraryList = new ArrayList<String>();
			    List<String> names = new ArrayList<String>();
			    List<String> art = new ArrayList<String>();
					String user = Login.getCurrentUser(); 		
				Login.LoadUserLibrary(libraryList, names, art, user); 
				Map<String, Object> variables = ImmutableMap.of("username", user, 
						"library", libraryList, "names", names, "art", art); 
				return new ModelAndView(variables, "library.ftl"); 
			} 
			//If password is incorrect, reload home page with status message
			else { 
				Map<String, Object> variables = ImmutableMap.of("title",
				          "uniTunes", "status", "Incorrect password");
			    return new ModelAndView(variables, "query.ftl");
			}
		}
		//If email doesn't exist in database, reload home page with status message 
		Map<String, Object> variables = ImmutableMap.of("title",
		          "uniTunes", "status", "User doesn't exist! Try using another email.");
	    return new ModelAndView(variables, "query.ftl");
		}
	} 
	  
  }
  
  public static class LoadLibraryHandler implements TemplateViewRoute {

	@Override
	public ModelAndView handle(Request request, Response response) throws Exception {
		QueryParamsMap qm = request.queryMap(); 
		String libraryLoad = qm.value("library");
    List<String> libraryList = new ArrayList<String>();
    List<String> names = new ArrayList<String>();
    List<String> art = new ArrayList<String>();
		String user = Login.getCurrentUser(); 
		
//	Login.LoadUserLibrary(libraryList, names, art, user); 
    User curUser = UserDatabase.getUserLibrary(user); 
    List<Song> favoriteSongs = curUser.getFavorites();
		for(int i = 0; i < favoriteSongs.size() ; i ++) {
			Song curSong = favoriteSongs.get(i);
			String songlink = DatabaseConnection.getSpotifyLinkFromID(curSong.id); 
			String item = "<li>"+ songlink + "</li>" ; 
			libraryList.add(item); 
      names.add(curSong.name);
      
      String album = String.format("<a href=\"song/%s\"> <img src=%s> </a>", curSong.id, DatabaseConnection.getAlbumArt(curSong.id));
      art.add(album);
		}
		
		System.out.println("this is the user: "+ user);
		Map<String, Object> variables = ImmutableMap.of("username", user, 
				"library", libraryList, "names", names, "art", art); 
		return new ModelAndView(variables, "library.ftl"); 
	} 	  
  }
  
  public static void LoadUserLibrary(List<String> libraryList, List<String> names, List<String> art, String user) throws SQLException { 
	    libraryList = new ArrayList<String>();
	    names = new ArrayList<String>();
	    art = new ArrayList<String>();
	    user = Login.getCurrentUser(); 
	    User curUser = UserDatabase.getUserLibrary(user); 
	    List<Song> favoriteSongs = curUser.getFavorites();
			for(int i = 0; i < favoriteSongs.size() ; i ++) {
				Song curSong = favoriteSongs.get(i);
				String songlink = DatabaseConnection.getSpotifyLinkFromID(curSong.id); 
				String item = "<li>"+ songlink + "</li>" ; 
				libraryList.add(item); 
	      names.add(curSong.name);
	      
	      String album = String.format("<a href=\"song/%s\"> <img src=%s> </a>", curSong.id, DatabaseConnection.getAlbumArt(curSong.id));
	      art.add(album);
			}
	  
  }


  public static String getCurrentUser() { 
	  return userName; 
  }
  /**
   * Handler that opens the login page
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
//     loggedIn = false; 
//     userName = ""; 
      Map<String, Object> variables = ImmutableMap.of("title",
          "uniTunes", "status", "");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handles the creation of a new account
   */
  private static class CreateHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) throws SQLException {
      loggedIn = true; 

      
      QueryParamsMap qm = req.queryMap(); 
		  String songToAdd = qm.value("add");
      System.out.println("song: " + songToAdd);
      String firstName = req.queryParams("firstName");
      String lastName = req.queryParams("lastName");
      String email = req.queryParams("email");
      String password = req.queryParams("password");
      String confirmPassword = req.queryParams("confirm-password");
      String selectedSong = req.queryParams("song");
      userName = (firstName + lastName).toLowerCase(); 
      try {
    	String userName = (firstName + lastName).toLowerCase(); 
        UserDatabase.addNewUser(userName, email, password);
      } catch(SQLException e) {
        System.out.println("ERROR: Unable to add new user to User Database");
      }
      //randomly generate three songs
      List<String> randomTracks = DatabaseConnection.getRandomTrackNames();

      //Organising information for first song
      String firstSong = randomTracks.get(0);
      String firstSongID = DatabaseConnection.getIDFromSongName(firstSong);
      String firstArtist = DatabaseConnection.getArtistFromSongID(firstSongID);
      String firstLink = String.format("<a href=\"http://%s\" target=\"_blank\"> %s </a>",
          DatabaseConnection.getSpotifyLinkFromID(firstSongID), "Listen here!");

      //Organising information for second song
      String secondSong = randomTracks.get(1);
      String secondSongID = DatabaseConnection.getIDFromSongName(secondSong);
      String secondArtist = DatabaseConnection.getArtistFromSongID(secondSongID);
      String secondLink = String.format("<a href=\"http://%s\" target=\"_blank\"> %s </a>",
          DatabaseConnection.getSpotifyLinkFromID(secondSongID), "Listen here!");

      //Organising information for third song
      String thirdSong = randomTracks.get(2);
      String thirdSongID = DatabaseConnection.getIDFromSongName(thirdSong);
      String thirdArtist = DatabaseConnection.getArtistFromSongID(thirdSongID);
      String thirdLink = String.format("<a href=\"http://%s\" target=\"_blank\"> %s </a>",
          DatabaseConnection.getSpotifyLinkFromID(thirdSongID), "Listen here!");

      StringBuilder songListBuilder = new StringBuilder();
      songListBuilder.append("<li id= \"song-list\">" + "<a href=/songs id=\"song\">" + firstSong +
          " by " + firstArtist +
          "</a> " + "   " +
          firstLink + "<a href=/songs id=\"song\">" + "</li>" + "<li id= \"song-list\">" + secondSong + " by " + secondArtist +
          "</a> " + "   " +
          secondLink + "</li>" + "<li id= \"song-list\">" + "<a href=/songs " +
          "id=\"song\">" + thirdSong + " by " + thirdArtist +
          "</a> " + "   " +
          thirdLink + "</li>");
      
          List<String> songs = new ArrayList<>();
          List<String> songs2 = new ArrayList<>();
          songs.add("<li id= \"song-list\">" + "<a  href=/songs id=\"song\" >" + firstSong +
          " by " + firstArtist +
          "</a> " + "   " +
          firstLink + "<a href=/songs id=\"song\">" + "</li>");
          songs.add("<li id= \"song-list\">" + secondSong + " by " + secondArtist +
          "</a> " + "   " +
          secondLink + "</li>");
          songs.add("<li id= \"song-list\">" + "<a  href=/songs " +
          "id=\"song\">" + thirdSong + " by " + thirdArtist +
          "</a> " + "   " +
          thirdLink + "</li>");

          String one = String.format("<li id= \"song-list\"><a name=\"suggestion\" value=\"%s\" href=/songs id=\"song\" > %s </a>  %s </li>", firstSong, firstSong, firstLink);
          System.out.println(one);
          String two = String.format("<li id= \"song-list\"><a name=\"suggestion\" value=\"%s\" href=/songs id=\"song\" > %s </a>  %s </li>", secondSong, secondSong, secondLink);
          String three = String.format("<li id= \"song-list\"><a name=\"suggestion\" value=\"%s\" href=/songs id=\"song\" > %s </a>  %s </li>", thirdSong, thirdSong, thirdLink);
          // String two = String.format("<input type=\"hidden\" name=\"suggestion\" value=\"%s\"/><li name=\"suggestion\" value=\"%s\" id= \"song-list\"><a name=\"suggestion\" value=\"%s\" href=/songs id=\"song\" > %s </a>  %s <a href=/songs id=\"song\"></li>", secondSong, secondSong, secondSong, secondSong, secondLink);
          // String three = String.format("<input type=\"hidden\" name=\"suggestion\" value=\"%s\"/><li name=\"suggestion\" value=\"%s\" id= \"song-list\"><a name=\"suggestion\" value=\"%s\" href=/songs id=\"song\" > %s </a>  %s <a href=/songs id=\"song\"></li>", thirdSong, thirdSong, thirdSong, thirdSong, thirdLink);

          songs2.add(one);
          songs2.add(two);
          songs2.add(three);


      List<String> art = new ArrayList<>();
      art.add(DatabaseConnection.getAlbumArt(firstSongID));
      art.add(DatabaseConnection.getAlbumArt(secondSongID));
      art.add(DatabaseConnection.getAlbumArt(thirdSongID));
      String songList = songListBuilder.toString();
      String welcomeMessage = "Welcome" + " " + firstName + "!";
      Map<String, Object> variables = ImmutableMap.of("status",
          welcomeMessage, "songs", songs2, "art", art);
      return new ModelAndView(variables,"new_account.ftl");
    }

    public static void setSuggest(String song_name){
      System.out.println("in setting!!!");
       String user = Login.getCurrentUser(); 
        User curUser = UserDatabase.getUserLibrary(user); 
        curUser.setSuggest(song_name);
        
    }

    public static String getSuggest(){
      String user = Login.getCurrentUser(); 
        User curUser = UserDatabase.getUserLibrary(user); 
        
      return curUser.getSuggest();
    }


  }

}
