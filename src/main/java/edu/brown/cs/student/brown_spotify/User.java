package edu.brown.cs.student.brown_spotify;

import edu.brown.cs.student.kdtree.KdTreeNode;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.student.brown_spotify.Song;
import edu.brown.cs.student.kdtree.Coordinates;

/*
 * This is a User class which also extends KdTreeNode, the coordinates in this case would be the average attributes of the songs favorited by the user. 
 */
public class User extends KdTreeNode{
	int id; 
	String name, password, email; 
	Coordinates coords; 
	KdTreeNode leftChild, rightChild; 
	List<Song> favoriteSongs; 
	public User(KdTreeNode leftChild, KdTreeNode rightChild, Coordinates coords, Integer id) {
		super(leftChild, rightChild, coords, id);
		this.name = null; 
		this.id = -1; 
		this.coords = coords; 
		this.leftChild = null;
		this.rightChild = null;
		this.favoriteSongs = new ArrayList<Song>(); 
		
	}
	/*
	 * Sets the name and id attributes. 
	 */
	public void setData(String name, int id){
		this.id = id; 
		this.name = name; 
	}
	/*
	 * set additional attributes
	 */
	public void setAdditional(String password, String email) {
		this.password = password; 
		this.email = email; 
	}
	
	/*
	 * helper function that adds a song to the favorite list. 
	 */
	public void addSong(Song s) {
		this.favoriteSongs.add(s); 
	}
	/*
	 * getter function to retrieve the users favorite songs. 
	 */
	public List<Song> getFavorites() {
		return this.favoriteSongs; 
	}

}
