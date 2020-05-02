package edu.brown.cs.student.brown_spotify;
import edu.brown.cs.student.kdtree.KdTreeNode;
import edu.brown.cs.student.kdtree.Coordinates;

//needs to be able to read a file and create all the stars out of the line, contains a kdtree object to store the stars. 
public class Song extends KdTreeNode {
	String name;
	String genre; 
	Integer duration; 
	Integer popularity; 
	Integer id; 
	KdTreeNode leftChild, rightChild; 
	Coordinates coords; 
	public Song(KdTreeNode leftChild, KdTreeNode rightChild, Coordinates coords, Integer id) {
		super(leftChild, rightChild, coords, id);
		this.name = null; 
		this.id = -1; 
		this.coords = coords; 
		this.leftChild = null;
		this.rightChild = null;

	}
	public void setData(String name, int id){
		this.id = id; 
		this.name = name; 
	}
	public void setClassifiable(String genre, Integer duration, Integer popularity) {
		this.genre = genre; 
		this.duration = duration; 
		this.popularity = popularity; 
	}
	
}
