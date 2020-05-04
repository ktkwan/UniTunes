package edu.brown.cs.student.brown_spotify;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.student.commands.Command;
import edu.brown.cs.student.kdtree.Coordinates;
import edu.brown.cs.student.kdtree.KdTree;
import edu.brown.cs.student.kdtree.KdTreeNode;
import edu.brown.cs.student.database.SongDatabase;


/**
 * Primary class for handling commands to do with the UniTunes program
 */
public class UniTunes {


  SongDatabase songdb;
  UserDatabase userdb; 
  UserCommand userCommand;
  DatabaseCommand dbCommand;
  SuggestCommand suggestCommand;
  ConnectCommand connectCommand;
  KdTree tree;
  static int dimensions;
  List<Song> clusters;
  List<Song> allSongs;

  public UniTunes(SongDatabase songdb, UserDatabase userdb) {
	this.songdb = songdb;
	this.userdb = userdb; 
	this.allSongs = new ArrayList<Song>();
	dimensions = 3;
	public UniTunes() {
	this.allSongs = new ArrayList<Song>();
	this.dimensions = 3;
    userCommand = new UserCommand();
    dbCommand = new DatabaseCommand();
    suggestCommand = new SuggestCommand();
    connectCommand = new ConnectCommand();
    this.allsongs = songdb.getSongs(); // get all the songs currently in the database, should this be static?
    this.clusters = this.setUpClusters();
    tree = new KdTree(this.allsongs, 3);
    clusters = this.setUpClusters();
    tree = new KdTree(this.allSongs, 3);

  }

  /*
   * Manually add the cluster coordinates based on the outputted clusters of the algorithm.
   */
  private List<Song> setUpClusters() {
	  // for now the number of clusters is set to 5 and trained on a set of 50 songs.
	  int numberC = 5;  // changeable parameter
	  double[][] algorithmOut = {
	  {58022.51851852,  58022.51851852,  58022.51851852},
	  { 71596.19047619,  71596.19047619,  71596.19047619},
	  { 96890.85185185,  96890.85185185,  96890.85185185},
      {216291.66666667, 216291.66666667, 216291.66666667},
      { 46523.53333333,  46523.53333333,  46523.53333333},
	  };
	  List<Coordinates> coordList = new ArrayList<Coordinates>();
	  // turn the outputed array in to coordinates
	  for(int i = 0; i < numberC; i++) {
		  Coordinates curr = new Coordinates(algorithmOut[i]);
		  coordList.add(curr);
	  }
	  // turn the coordinates in to a list of dummy songs that can be used by kdtree
	  List<Song> dummySongList = new ArrayList<Song>();
	  for(int j = 0; j< numberC; j++) {
		  Song s = new Song(null, null , coordList.get(j), j);
		  dummySongList.add(s);
		  allSongs.add(s);
		  this.allSongs.add(s);
	  }
	  return dummySongList;
  }

  /*
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
	  double[] vals = new double[3];
	  vals[0] = avgX;
	  vals[1]= avgY;
	  vals[2] = avgZ;
	  Coordinates c = new Coordinates(vals);
	  Song s = new Song(null, null, c, null);  // what id should we give these dummy songs?
	  return s;
  }
  /*
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

  /*
   * Suggests a song for a new user, based on a song that they chose
   * @input: Song song 
   * @output: k suggested songs. 
   */
//  public Song newUserSuggestSong(Song song) {
//	  	  // this takes a song as input and we have to retrieve the id.
//	      System.out.println(args[0]);
//	      System.out.println(args[1]);
//	      List<Song> userList = new ArrayList<Song>();
//	      double[][] testUser= {
//	    		  {5.4,7.3, 1.0},
//	    		  {3.7,2.0,5.5}
//	      };
//	      Coordinates c1 = new Coordinates(testUser[0]);
//	      Coordinates c2 = new Coordinates(testUser[1]);
//	      Song s1 = new Song(null, null, c1, 100);
//	      Song s2 = new Song(null, null, c2, 101);
//	      userList.add(s1);
//	      userList.add(s2);
//	      // generate the average song
//	      Song avg = findAverageSong(userList);
//	      // find the closest centroid to the dummy song.
//	      Song closestCentroid = findClosestCentroid(avg);
//	      // now for all the songs for a given centroid, find the closest songs to the centroid => need to set up a query from each
//	      // centroid to a list of songs with the given centroid.
//	      System.out.println("found the closest centroid" + closestCentroid);
//	      tree.neighbors(closestCentroid, 1, tree.getRoot()); // should print out a list of five recommended songs.
//	      return "running suggest command";
//
//  }
  
  /*
   * Suggests a song for an existing user based on their favorite songs. 
   * @input: userName 
   * @output: k number of recommended songs 
   */
  public List<Song> exisitingUserSuggestSong(String UserName) {
  	  // get the user based on the userName, retrieve the value from the cache 
	  User curUser = this.userdb.get(UserName); 
	  List<Song> favorites = curUser.favoriteSongs; 
      // generate the average song
      Song avg = findAverageSong(favorites); 
      // find the closest centroid to the dummy song.
      Song closestCentroid = findClosestCentroid(avg);
      // now for all the songs for a given centroid, find the closest songs to the centroid => need to set up a query from each
      // centroid to a list of songs with the given centroid.
      System.out.println("found the closest centroid" + closestCentroid);
      return tree.neighbors(closestCentroid, 1, tree.getRoot()); // should print out a list of five recommended songs.

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
      // eventually this should take in a list of songs and find the closest song and find the closest neighbors to recommend.
      // for now let's create a dummy list for a user with two songs
      // we need to make a query that takes in a user id and returns a list of songs. Maybe this part can be cached
      System.out.println(args[0]);
      System.out.println(args[1]);
      List<Song> userList = new ArrayList<Song>();
      double[][] testUser= {
    		  {5.4,7.3, 1.0},
    		  {3.7,2.0,5.5}
      };
      Coordinates c1 = new Coordinates(testUser[0]);
      Coordinates c2 = new Coordinates(testUser[1]);
      Song s1 = new Song(null, null, c1, 100);
      Song s2 = new Song(null, null, c2, 101);
      userList.add(s1);
      userList.add(s2);
      // generate the average song
      Song avg = findAverageSong(userList);
      // find the closest centroid to the dummy song.
      Song closestCentroid = findClosestCentroid(avg);
      // now for all the songs for a given centroid, find the closest songs to the centroid => need to set up a query from each
      // centroid to a list of songs with the given centroid.
      System.out.println("found the closest centroid" + closestCentroid);
      tree.neighbors(closestCentroid, 1, tree.getRoot()); // should print out a list of five recommended songs.
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
