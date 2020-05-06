package edu.brown.cs.student.brown_spotify;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
  private SongDatabase songdb;
  private UserDatabase userdb; 
  private UserCommand userCommand;
  private DatabaseCommand dbCommand;
  private SuggestCommand suggestCommand;
  private ConnectCommand connectCommand;
  private KdTree<Song> tree;
  private static int dimensions;
  private List<Song> clusters;
  private List<Song> allSongs;
  

  public UniTunes(SongDatabase songdb, UserDatabase userdb) {
	this.songdb = songdb;
	this.userdb = userdb; 
	this.allSongs = new ArrayList<Song>();
	//this.dimensions = 3;
    userCommand = new UserCommand();
    dbCommand = new DatabaseCommand();
    suggestCommand = new SuggestCommand();
    connectCommand = new ConnectCommand();

	  try { 
		  for(int i = 0; i < songdb.getSongs().size(); i ++) { 
			  allSongs.add(songdb.getSongs().get(i)); 
      }
        System.out.println(allSongs);
			  this.clusters = this.setUpClusters();
			  tree = new KdTree(allSongs, 2);

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
	  // Double[][] algorithmOut = {
	  // {58022.51851852,  58022.51851852,  58022.51851852},
	  // { 71596.19047619,  71596.19047619,  71596.19047619},
	  // { 96890.85185185,  96890.85185185,  96890.85185185},
    //   {216291.66666667, 216291.66666667, 216291.66666667},
    //   { 46523.53333333,  46523.53333333,  46523.53333333},
	  // };
    Double[][] testClust = {
      {58022.51851852,  58022.51851852}, 
      { 71596.19047619,  71596.19047619}
    }; 
	  List<Coordinates> coordList = new ArrayList<Coordinates>();
	  // turn the outputed array in to coordinates
	  for(int i = 0; i < numberC; i++) {
		  Coordinates curr = new Coordinates(testClust[i]); 
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
  public Song findClosestCentroid(Song dummy) {
	  double closestDist = Integer.MAX_VALUE;
	  Song closestCentroid = null;
	  for(int i = 0; i< this.clusters.size(); i++) {
		  Song cur = this.clusters.get(i);
		  double curDist = tree.findistance(dummy.coords, cur.coords);
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
	  	  Song curSong = this.songdb.getSongFromName(songName); 
        System.out.println(curSong.name); 
	      Song closestCentroid = findClosestCentroid(curSong);
	      // now for all the songs for a given centroid, find the closest songs to the centroid => need to set up a query from each
	      // centroid to a list of songs with the given centroid.
	      System.out.println("found the closest centroid" + closestCentroid);
	      int count = 0; 
	      List<String> recommendations = new ArrayList<String>(); 
        // just to test 
        System.out.println("rootNode" + ((Song)tree.getRoot()).name); 
        List<String> allNodes = tree.neighbors(curSong, 1, tree.getRoot()); 
        //List<KdTreeNode> allNodes = tree.neighbors(closestCentroid, 1, tree.getRoot()); 
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
      System.out.println("found the closest centroid" + closestCentroid);
      int count = 0; 
      List<String> recommendations = new ArrayList<String>(); 
      while(count<k) {
    	  String curId = tree.neighbors(closestCentroid, 1, tree.getRoot()).get(0); 
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
      int k = 1; 
      // this is the case where it is an existing user, run suggest user userId 
      if(args.length ==3) {
    	  String type = args[1]; 
    	  String id = args[2]; 
    	  if(type.equals("user")) {
          existingUserSuggestSong(id, k); 
    	  }
    	  else if(type.equals("song")){
    		 newUserSuggestSong(id, k); 
    	  }
    	  else {
    		  System.out.println("invalid command"); 
    	  }
      }else {
    	  System.out.println("wrong number of arguments"); 
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
