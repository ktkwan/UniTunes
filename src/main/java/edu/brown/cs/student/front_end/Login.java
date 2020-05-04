package edu.brown.cs.student.front_end;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.student.brown_spotify.DatabaseConnection;
import edu.brown.cs.student.database.SongDatabase;
import edu.brown.cs.student.database.UserDatabase;
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

  public Login(){

  }

  public static TemplateViewRoute getLoginHandler(){
    return new FrontHandler();
  }

  public static TemplateViewRoute getCreateHandler() { return new CreateHandler(); }

  /**
   * Handler that opens the login page
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "uniTunes", "status", "coming soon");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handles the creation of a new account
   */
  private static class CreateHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) throws SQLException {
      String firstName = req.queryParams("firstName");
      String lastName = req.queryParams("lastName");
      String email = req.queryParams("email");
      String password = req.queryParams("password");
      String confirmPassword = req.queryParams("confirm-password");
      String selectedSong = req.queryParams("song");
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
      songListBuilder.append("<li id= \"song-list\">" + "<a href=/create-account id=\"song\">" + firstSong +
          " by " + firstArtist +
          "</a> " + "   " +
          firstLink + "<a href=/create-account id=\"song\">" + "</li>" + "<li id= \"song-list\">" + secondSong + " by " + secondArtist +
          "</a> " + "   " +
          secondLink + "</li>" + "<li id= \"song-list\">" + "<a href=/create-account " +
          "id=\"song\">" + thirdSong + " by " + thirdArtist +
          "</a> " + "   " +
          thirdLink + "</li>");
      String songList = songListBuilder.toString();
      String welcomeMessage = "Welcome" + " " + firstName + "!";
      Map<String, Object> variables = ImmutableMap.of("status",
          welcomeMessage, "songs", songList);
      return new ModelAndView(variables,"new_account.ftl");
    }

  }


}
