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
	String id; 
	String name, password, email; 
	Coordinates coords; 
	KdTreeNode leftChild, rightChild; 
	List<Song> favoriteSongs; 
	String suggestSong;
	public User(KdTreeNode leftChild, KdTreeNode rightChild, Coordinates coords, String id) {
		super(leftChild, rightChild, coords, id);
		this.name = null; 
		this.id = ""; 
		this.coords = coords; 
		this.leftChild = null;
		this.rightChild = null;
		this.favoriteSongs = new ArrayList<Song>(); 
		this.suggestSong = "";

		
	}
	/*
	 * Sets the name and id attributes. 
	 */
	public void setData(String name, String id){
		this.id = id; 
		this.name = name; 
	}

	public void setSuggest(String song){
		this.suggestSong = song;
	}

	public String getSuggest(){
		return this.suggestSong;
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
