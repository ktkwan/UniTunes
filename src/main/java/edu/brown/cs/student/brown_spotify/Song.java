package edu.brown.cs.student.brown_spotify;

//needs to be able to read a file and create all the stars out of the line, contains a kdtree object to store the stars. 
public class Song extends KdTreeNode {
	String name;
	int id; 
	KdTreeNode leftChild, rightChild; 
	Coordinates coords; 
	public Song(KdTreeNode leftChild, KdTreeNode rightChild, Coordinates coords) {
		super(leftChild, rightChild, coords);
		this.name = null; 
		this.id = -1; 
//		this.leftChild = null;
//		this.rightChild = null;
//		this.coords = new Coordinates();
	}
	public void setData(String name, int id){
		this.id = id; 
		this.name = name; 
	}
	public void setClassifiable() {
		
	}
	
}
