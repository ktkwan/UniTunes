package edu.brown.cs.student.brown_spotify;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.commands.Command;
import edu.brown.cs.student.commands.REPL;
import edu.brown.cs.student.database.SongDatabase;
import edu.brown.cs.student.database.UserDatabase;
import edu.brown.cs.student.front_end.Login;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.eclipse.jetty.server.Authentication;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {

  private static final int DEFAULT_PORT = 4566;
  private static final String DEFAULT_USER_DB = "users.sqlite3";
  private static final String DEFAULT_SONG_DB = "data/songs_today.sqlite3";
  private static UserDatabase userDb = null;
  private static SongDatabase songDb = null ;
  private static UniTunes uniTunesProgram;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
    userDb = null;
    songDb = null;
    uniTunesProgram = null;
  }

  public static HashMap<String, String> song_hashmap = new HashMap<>();

  private void run(){
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSpec<String> dataSpec =
        parser.accepts("data").withRequiredArg().ofType(String.class);

    OptionSpec<String> databaseSpec =
        parser.accepts("database").withRequiredArg().ofType(String.class).defaultsTo(DEFAULT_SONG_DB);
    OptionSpec<String> userDataSpec =
        parser.accepts("users").withRequiredArg().ofType(String.class).defaultsTo(DEFAULT_USER_DB);
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
    .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    if (options.has("data") || options.has("database")) {


      if (options.has("database")) {

        try {
          songDb = new SongDatabase(options.valueOf(databaseSpec));

          if (options.has("data")) {

            String files = options.valueOf(dataSpec);
            System.out.println(files);
            List<String> fileNames = new ArrayList<String>(Arrays.asList(files.split(",")));

            for (String file : fileNames) {
              songDb.read_csv(file);
            }
          } else {
        	  String files = options.valueOf(databaseSpec);
        	  List<String> fileNames = new ArrayList<String>(Arrays.asList(files.split(",")));

              for (String file : fileNames) {
            	  DatabaseConnection.createConnection(file);
              }
          }
        } catch (SQLException | ClassNotFoundException e) {
          System.err.println("SQLite error: " + e.getMessage());
          System.exit(1);
        }
      } else {
        String files = options.valueOf(dataSpec);
      }
    }

    if (options.has("users")) {
      try {
        String filename = options.valueOf(userDataSpec);
        userDb = new UserDatabase(filename);
      } catch(SQLException | ClassNotFoundException e) {
        System.out.println("ERROR: User Database cannot be made");
        System.exit(1);
      }
    }
    // Creating a hashmap of commands that corresponds to different classes that implement the
    // Command interface
    HashMap<String, Command> map = new HashMap<String, Command>();
    //create a unitunes object

    try {
		    uniTunesProgram = new UniTunes(songDb, userDb);
		    map.put("user", uniTunesProgram.getUserCommand());
		    map.put("db", uniTunesProgram.getDatabaseCommand());
		    map.put("suggest", uniTunesProgram.getSuggestCommand());
		    map.put("connect", uniTunesProgram.getConnectCommand());
		    REPL repl = new REPL(map);
		    repl.runRepl();
        throw new SQLException("throw");
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();

	}

  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/unitunes", Login.getLoginHandler() , freeMarker);
    Spark.post("/create-account", Login.getCreateHandler(), freeMarker);
    Spark.get("/library", Login.getLoadLibraryHandler(), freeMarker);
    Spark.get("/library", Login.getVerifyLoginHandler(), freeMarker);
    Spark.get("/songs", new SongHandler(), freeMarker);
    Spark.get("/song/:songID", new SongInfoHandler(), freeMarker);
    Spark.post("/songs", new AddToLibraryHandler(), freeMarker);
  }

  private static class AddToLibraryHandler implements TemplateViewRoute {

	@Override
	public ModelAndView handle(Request request, Response response) throws Exception {
		QueryParamsMap qm = request.queryMap();
		String songToAdd = qm.value("add");

		// need to fix this later =====> design issues
		//UserDatabase.addSongToLibrary(Login.getCurrentUser(), songToAdd);
    String user = Login.getCurrentUser();
    User curUser = UserDatabase.getUserLibrary(user);
    String name = DatabaseConnection.getNameFromID(songToAdd);
    if (!curUser.favoriteSongs.contains(SongDatabase.getSongFromName(name))){
      curUser.addSong(SongDatabase.getSongFromName(name));
    }

		HashMap<String, String> song_names = DatabaseConnection.getAllSongNames();
		song_hashmap = song_names;
		String songs = "";
		HashMap<String, String> map = new HashMap<>();
		List<String> n = new ArrayList<>();
		List<String> song_list = new ArrayList<>();
		List<String> art_list = new ArrayList<>();
		HashMap<String, String> song_to_button = new HashMap<>();
		for (Map.Entry<String, String> entry: song_names.entrySet()){

			 String songLink = String.format("<a href=\"song/%s\"> %s </a>", entry.getKey(), entry.getValue());

			 songs += songLink;
			 String likeButton = String.format(
				 		"<button class=\"btn-secondary like-review\" name=\"add\" value=\"%s\">\n" +
				 		"    <i class=\"fa fa-heart\" aria-hidden=\"false\"></i> Like\n" +
				 		"  </button>", entry.getKey());
			 n.add(songLink);
			 n.add(likeButton);
			 song_to_button.put(songLink, likeButton);
			 String album = String.format("<a href=\"song/%s\"> <img src=%s> </a>", entry.getKey(), DatabaseConnection.getAlbumArt(entry.getKey()));
			 map.put(songLink, album);
		}

		Map<String, HashMap<String, String>> variables = ImmutableMap.of("display", song_to_button, "songs", map);
		return new ModelAndView(variables, "song_query.ftl");
	}

  }

  private static class SongHandler implements TemplateViewRoute {

	@Override
	public ModelAndView handle(Request request, Response response) throws Exception {
		HashMap<String, String> song_names = DatabaseConnection.getAllSongNames();
		song_hashmap = song_names;
    QueryParamsMap qm = request.queryMap();
    String suggestion_song = qm.value("add");
    String s = request.queryParams("suggestion");
    String[] command = new String[3];
    command[0] = "suggest";
    command[1] = "song";
    command[2] = suggestion_song;
    String user = Login.getCurrentUser();
    User curUser = UserDatabase.getUserLibrary(user);
    // curUser.suggestSong = suggest;
    System.out.println("song: " + curUser.getSuggest());
    System.out.println("song: " + s);
    System.out.println("entire command "+ command);
    //UniTunes uni = new UniTunes(songDb, userDb);
    // uniTunesProgram.getSuggestCommand().runCommand(command);
    // System.out.println("Here: " + uniTunesProgram.suggestedSongs);

		String songs = "";
		HashMap<String, String> map = new HashMap<>();
		List<String> n = new ArrayList<>();
		List<String> song_list = new ArrayList<>();
		List<String> art_list = new ArrayList<>();


		HashMap<String, String> song_to_button = new HashMap<>();
		for (Map.Entry<String, String> entry: song_names.entrySet()){


			 String songLink = String.format("<a href=\"song/%s\"> %s </a>",
					 entry.getKey(), entry.getValue());
			 songs += songLink;
//			 String likeButton = String.format(
//				 		"<button  class=like type=\"submit\" name=\"add\" value=\"%s\"> <3 </button>", entry.getKey());
			 String likeButton = String.format(
				 		"<button class=\"btn-secondary like-review\" value=\"%s\" name=\"add\">\n" +
				 		"    <i class=\"fa fa-heart\" aria-hidden=\"true\"></i> Like\n" +
				 		"  </button>", entry.getKey());
			 n.add(songLink);
			 n.add(likeButton);

			 song_to_button.put(songLink, likeButton);
			 art_list.add(DatabaseConnection.getAlbumArt(entry.getKey()));
			 String album = String.format("<a href=\"song/%s\"> <img src=%s> </a>", entry.getKey(), DatabaseConnection.getAlbumArt(entry.getKey()));
			 map.put(songLink, album);

		}
		Map<String, HashMap<String, String>> variables = ImmutableMap.of("display", song_to_button, "songs", map);
		return new ModelAndView(variables, "song_query.ftl");
	}
  }

  private static class SongInfoHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) throws SQLException {
		  String songID = req.params(":songID");
		  System.out.println("ID: " + songID);
		  HashMap<String, String> song_names = DatabaseConnection.getAllSongNames();

		String l = DatabaseConnection.getSpotifyLinkFromID(songID);
		System.out.println("ID: " + DatabaseConnection.song_hashmap);
		String link = String.format("href=\"http://%s\" target=\"_blank\"", l);
		  Map<String, String> variables = ImmutableMap.of("title",  "uniTunes", "song_name", song_names.get(songID), "display", link, "artist_name", DatabaseConnection.getArtistFromSongID(songID), "album_art", DatabaseConnection.getAlbumArt(songID));
		  return new ModelAndView(variables, "song.ftl");
	  }
  }


  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
