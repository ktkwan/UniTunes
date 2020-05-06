package edu.brown.cs.student.kdtree;


public abstract class KdTreeNode {
	//declare fileds declare nonabstract methods 
	KdTreeNode leftChild; 
	KdTreeNode rightChild; 
	Coordinates coords; 
	String id; 
	int level; 
	
	public KdTreeNode(KdTreeNode leftChild, KdTreeNode rightChild, Coordinates coord, String id) {
		this.leftChild= null; 
		this.rightChild = null; 
		this.coords = coord; 
		this.level = -1;
		this.id = id; 
	}
	// setter for left child 
//	abstract void setLeft(KdTreeNode left);
	public String getId() { 
		return id; 
	}
	
	public void setRight(KdTreeNode right) {
		this.rightChild = right; 
	}
	
	public void setLeft(KdTreeNode left) {
		this.leftChild = left; 
	}
	public void setLevel(int level) {
		this.level = level; 
	}
	
	public KdTreeNode getRight() {
		return this.rightChild;
	}
	
	public KdTreeNode getLeft() {
		return this.leftChild;
	}
	
	public boolean hasRight() {
		if (this.getRight() != null) {
			return true;
		}
		
		return false;
	}
	
	public boolean hasLeft() {
		if (this.getLeft() != null) {
			return true;
		}
		
		return false;
	}
}
