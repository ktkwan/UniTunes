package edu.brown.cs.student.brown_spotify;
import edu.brown.cs.student.kdtree.KdTreeNode;
import edu.brown.cs.student.kdtree.Coordinates;

//needs to be able to read a file and create all the stars out of the line, contains a kdtree object to store the stars. 
public class Song extends KdTreeNode {
	public String name;
	public String genre; 
	public String genVal; 
	public Integer duration; 
	public Integer popularity; 
	public String id; 
	public KdTreeNode leftChild, rightChild; 
	public Coordinates coords; 
	public Song(KdTreeNode leftChild, KdTreeNode rightChild, Coordinates coords, String id) {
		super(leftChild, rightChild, coords, id);
		this.name = null; 
		this.id = ""; 
		this.coords = coords; 
		this.leftChild = null;
		this.rightChild = null;
	}
	public void setData(String name, String id){
		this.id = id; 
		this.name = name; 
	}
	public void setClassifiable(String genre, Integer duration, Integer popularity) {
		this.genre = genre; 
		this.duration = duration; 
		this.popularity = popularity; 
	}
	
	/*
	 * Uses jaccard similarity of a normalized song to turn the genre in to a value. 
	 */
//	public void turnGenreIntoVal() {
//		CharSequence left = "adult standards"; // change this based on new training set each time, should be set by song universe 
//	    CharSequence right= (CharSequence) this.genre; 
//		Set<String> intersectionSet = new HashSet<String>();
//	    Set<String> unionSet = new HashSet<String>();
//	    boolean unionFilled = false;
//	    int leftLength = left.length();
//	    int rightLength = right.length();
//	    if (leftLength == 0 || rightLength == 0) {
//	        return 0d;
//	    }
//	
//	    for (int leftIndex = 0; leftIndex < leftLength; leftIndex++) {
//	        unionSet.add(String.valueOf(left.charAt(leftIndex)));
//	        for (int rightIndex = 0; rightIndex < rightLength; rightIndex++) {
//	            if (!unionFilled) {
//	                unionSet.add(String.valueOf(right.charAt(rightIndex)));
//	            }
//	            if (left.charAt(leftIndex) == right.charAt(rightIndex)) {
//	                intersectionSet.add(String.valueOf(left.charAt(leftIndex)));
//	            }
//	        }
//	        unionFilled = true;
//	    }
//	    this.genVal = Double.valueOf(intersectionSet.size()) / Double.valueOf(unionSet.size());
//	}
}
