package edu.brown.cs.student.brown_spotify;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.brown.cs.student.commands.Command;
import edu.brown.cs.student.kdtree.Coordinates;
import edu.brown.cs.student.kdtree.KdTree;
import edu.brown.cs.student.kdtree.KdTreeNode;
import edu.brown.cs.student.database.SongDatabase;
import edu.brown.cs.student.database.UserDatabase;


/**
 * 
 * Primary class for handling commands to do with the UniTunes program
 */
public class UniTunes {
  /*
   * Song database and user database need to be static and initialized like this or there will be a null pointer 
   */
  private static SongDatabase songdb;
  private static UserDatabase userdb; 
  private UserCommand userCommand;
  private DatabaseCommand dbCommand;
  private SuggestCommand suggestCommand;
  private ConnectCommand connectCommand;
  private KdTree<Song> tree;
  private static int dimensions;
  private List<Song> clusters;
  private static List<Song> allSongs;
  private SentimentAnalysis sent; 
  public List<String> suggestedSongs;
  

  public UniTunes(SongDatabase sdb, UserDatabase udb) {
	songdb = sdb;
	userdb = udb; 
	allSongs = new ArrayList<Song>();
	clusters = new ArrayList<Song>(); 
  suggestedSongs = new ArrayList<>();
  this.sent = new SentimentAnalysis(); 

	this.dimensions = 14;
    userCommand = new UserCommand();
    dbCommand = new DatabaseCommand();
    suggestCommand = new SuggestCommand();
    connectCommand = new ConnectCommand();
    // System.out.println(sdb.getSongs().size());
    // this.songdb.getSongs(); 
	   try { 
		   for(int i = 0; i < sdb.getSongs().size(); i ++) { 
		 	  allSongs.add(sdb.getSongs().get(i)); 
       }
		 	  this.clusters = this.setUpClusters();
		 	  tree = new KdTree(allSongs, this.dimensions);

	   }catch(SQLException e) { 
		   System.out.println("ERROR: Empty song list in database"); 
	   }
  
  }

  /**
   * Manually add the cluster coordinates based on the outputted clusters of the algorithm.
   */
  private List<Song> setUpClusters() {
	  // for now the number of clusters is set to 5 and trained on a set of 50 songs.
	  int numberC = 2;  // changeable parameter for number of clusters 
	   Double[][] algorithmOut = {
			   { 0.0, 0.00,  4.00, 271032.0, .62700, 
		      .37700, -6.47200,  .0364000,  .77700,  .119000, 
		      131.333,  0.00,  0.00,  0.00}, 
			   { 0.00, 0.00,  2.00,  265959.0,  .579000,
		      .451000, -7.12200,  .0598000,  .537000,  .120000,
		      139.853,  0.00000, 0.0000,  0.000},
		      { 0.000,  0.00, 0.000, 0.00, 0.000,
		      0.00,  0.000,  0.000, 0.000, 0.00,
		      0.000,  0.00,  0.00,  0.00},
		      { 0.00,  0.000, 37.000, 81564.0,  .74200, 
		      .506000,  -12.1650,   .0376000,  .0188000, .0114000,
		      117.967,  0.000,  0.000,  0.0000}, 
		      { 0.00, 0.0000,  31.0000,  300306.0,  .501000, 
		      .372000, -12.1430, .0299000, .581000, .108000, 
		      117.008,  0.00, 0.00,  0.00}
	   };
//    Double[][] testClust = {
//      {58022.51851852,  58022.51851852}, 
//      { 71596.19047619,  71596.19047619}
//    }; 
	  List<Coordinates> coordList = new ArrayList<Coordinates>();
	  // turn the outputed array in to coordinates
	  for(int i = 0; i < numberC; i++) {
		  Coordinates curr = new Coordinates(algorithmOut[i]); 
		  coordList.add(curr);
	  }
	  // turn the coordinates in to a list of dummy songs that can be used by kdtree
	  List<Song> dummySongList = new ArrayList<Song>();

	  for(int j = 0; j< numberC; j++) {
		  String centroidId = "centroid-" + j; 
		  Song s = new Song(null, null , coordList.get(j), centroidId);
		  dummySongList.add(s);
		  allSongs.add(s);
		  this.allSongs.add(s);
	  }
	  return dummySongList;
  }

  /**
   * Helper function to create an average array vector for a user's list of favorite songs
   */
  public Song findAverageSong(List<Song> userList) {
	  double avgX = 0;
	  double avgY = 0;
	  double avgZ = 0;
	  double size = userList.size();
	  for (int i = 0; i< userList.size(); i++) {
		  Song cur = userList.get(i);
		  Coordinates curCoord = cur.coords;
		  avgX += curCoord.getValueAtDimension(0);
		  avgY += curCoord.getValueAtDimension(1);
		  avgZ += curCoord.getValueAtDimension(2);
	  }
	  avgX = avgX/size;
	  avgY = avgY/size;
	  avgZ = avgZ/size;
    Double[] vals = new Double[3];
	  vals[0] = avgX;
	  vals[1]= avgY;
	  vals[2] = avgZ;
	  Coordinates c = new Coordinates(vals);
	  Song s = new Song(null, null, c, "dummy");  // what id should we give these dummy songs?
	  return s;
  }
  /**
   * helper function to find the closest cluster given a dummy user song
   */
  public Song findClosestCentroid(Song s) {
	  double closestDist = Integer.MAX_VALUE;
	  Song closestCentroid = null;
	  
	  for(int i = 0; i< this.clusters.size(); i++) {
		  Song cur = this.clusters.get(i);
		  double curDist = tree.findistance(s.coords, cur.coords);
		  if(curDist < closestDist) {
			  closestDist = curDist;
			  closestCentroid = cur;
		  }
	  }
	  return closestCentroid;
  }
  

  /**
   * Suggests a song for a new user, based on a song that they chose
   * @input: Song song 
   * @output: k suggested songs. 
   */
  public List<String> newUserSuggestSong(String songName, int k) {
	  	  // retrieve song from hashmap from songname 
	  	  Song curSong = SongDatabase.getSongFromName(songName); 
	      Song closestCentroid = findClosestCentroid(curSong);
//	       now for all the songs for a given centroid, find the closest songs to the centroid => need to set up a query from each
	      // centroid to a list of songs with the given centroid.
	      int count = 0; 
	      List<String> recommendations = new ArrayList<String>(); 
        List<String> allNodes = tree.neighbors(curSong, 10, tree.getRoot()); 
	      while(count<k) {
	    	  String curId = allNodes.get(count); 
	    	  try {
          String song_name = DatabaseConnection.getSongNameFromID(curId); 
          System.out.println(song_name); 
	    	  if ((!curId.contains("centroid-")) && (!curId.equals("dummy"))) {
              recommendations.add(song_name); 
              Song recSong = SongDatabase.getSongFromName(song_name); 
              System.out.println("name: " + song_name + " genre: " + recSong.genre + " Danceability: " + recSong.attribute);
	        	  count += 1; 
	    	  }
	    	  }catch(SQLException e) { 
	    		  System.out.println("ERROR: Could not find song name in database"); 
	    	  }
	      }
        this.suggestedSongs = recommendations;
	      return recommendations; // should print out a list of five recommended songs.
  }
  
  /**
   * Suggests a song for an existing user based on their favorite songs. 
   * @input: userName, k 
   * @output: k number of recommended songs 
   */
  public List<String> existingUserSuggestSong(String UserName, int k) {
  	  // get the user based on the userName, retrieve the value from the cache 
	  User curUser = this.userdb.getUserLibrary(UserName); 
	  List<Song> favorites = curUser.favoriteSongs; 
      // generate the average song
      Song avg = findAverageSong(favorites); 
      // find the closest centroid to the dummy song.
      Song closestCentroid = findClosestCentroid(avg);
      // now for all the songs for a given centroid, find the closest songs to the centroid => need to set up a query from each
      // centroid to a list of songs with the given centroid.
//      System.out.println("found the closest centroid" + closestCentroid);
      int count = 0; 
      List<String> recommendations = new ArrayList<String>(); 
      List<String> allNodes = tree.neighbors(avg, 10, tree.getRoot()); 
      while(count<k) {
    	  String curId = allNodes.get(count); 
    	  try {
          String song_name = DatabaseConnection.getSongNameFromID(curId); 
	    	  if (curId.contains("centroid-") && (!curId.equals("dummy"))) {
	        	  recommendations.add(song_name); 
	        	  count += 1; 
	    	  }
	    	  }catch(SQLException e) { 
	    		  System.out.println("ERROR: Could not find song name in database"); 
	    	  }
      }
      return recommendations; // should print out a list of five recommended songs.

  }
  
  public Command getUserCommand(){
    return userCommand;
  }

  public Command getDatabaseCommand() {
    return dbCommand;
  }

  public Command getSuggestCommand() {
    return suggestCommand;
  }

  public Command getConnectCommand() {
    return connectCommand;
  }

  /**
   * Nested class for the "User Command"
   * This allows users to create new profiles for themselves.
   */
  class UserCommand implements Command {

      public UserCommand() {

      }

      @Override
      public String runCommand(String[] args) {
        /*
        TO DO: Add user to the database (name, brownu email, profile picture)
         */
        System.out.println("running user command");
        return "Running user command";
      }
    }

  /**
   * Nested class for the "Database Command"
   * This allows users to load the song database.
   */
  class DatabaseCommand implements Command {
    public DatabaseCommand(){

    }

    @Override
    public String runCommand(String[] args) {
      /*
      TO DO: Load the database
       */
      System.out.println("running database command");
      return "running database command";
    }
  }

  /**
   * Nested class for the "Suggest Command"
   * This suggests student made songs to the user based on a non-student-produced song they like. This command calls the kdtree, which has 3 dimensions.
   */
  class SuggestCommand implements Command {

    @Override
    public String runCommand(String[] args) {
      /*
      TO DO: Implement kNN and algorithm lol
       */
      System.out.println("running suggest command");
      int k = 3; 
      // this is the case where it is an existing user, run suggest user userId 

    	  String type = args[1]; 
    	  String id = args[2]; 
    	  if(type.equals("user")) {
          existingUserSuggestSong(id, k); 
        }
    	  else if(type.equals("song")){
        System.out.println("Enters here");
        String song_name = ""; 
        for (int i=2; i < args.length; i++){
          song_name+=args[i] + " ";
        }
        String newString = song_name.strip(); 
    		newUserSuggestSong(newString, k); 
        }else{
          System.out.println("not a valid command"); 
        }
    	 
      return "running suggest command";
    
      }
  }

  /**
   * Nested class for the "Connect Command"
   * This allows student artists to connect with other student artists.
   */
  class ConnectCommand implements Command {

    @Override
    public String runCommand(String[] args) {
      /*
      TO DO: Implement a feature where users can send a message to other student artists wanting
      to collaborate (like linkedin)
       */
      System.out.println("running connect command");
      return "running connect command";
    }
  }

}

