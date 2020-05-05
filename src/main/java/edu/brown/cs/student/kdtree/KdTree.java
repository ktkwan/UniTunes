package edu.brown.cs.student.kdtree;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
 

// this class contains stars and builds the initial KdTree 
public class KdTree<T extends KdTreeNode>{
	List<KdTreeNode> _data; 
	int _dimensions; 
	int _middle; 
	KdComparator<KdTreeNode> _k; 
	DistanceComparator<KdTreeNode> _distComparator; 
	KdTreeNode _root; 
	// this recursively adds the nodes starting at the root(the median), alternates between the dimensions. 
	public KdTree(List<KdTreeNode> data, int dimensions) {
		 _data = data; 
		_dimensions = dimensions; 
		_k = new <KdTreeNode> KdComparator();
		 _distComparator = new DistanceComparator<KdTreeNode>();
		//start building the tree starting at the root 
		this.buildTree(_k, _data,0);
	}
	public KdTreeNode getRoot() {
		return _root; 
	}
	
	//helper function to find the medium element for a given dimension
	public KdTreeNode findMiddle(List<KdTreeNode> nodes, KdComparator<KdTreeNode> k) {
		//edge case where there is only one node left to consider 
		if(nodes.size() == 1) {
			return nodes.get(0);
		}
		Collections.sort(nodes, k);
		// I'm not sure if this is doing the right thing with casting?
		_middle = (int)Math.ceil(nodes.size()/2); 
		KdTreeNode root = nodes.get(_middle);
		return root; 
	}
	public List<KdTreeNode> getData(){
		return _data; 
	}
	
	//alternate splitting the tree based on the dimensions
	//make sure that you update the nodes levels every time   
	public KdTreeNode buildTree(KdComparator<KdTreeNode> c, List<KdTreeNode> data, int level) {
		//set the current dimension based on the level 
		int curDim = level % _dimensions; 
		c.setDimension(curDim);
		//invalid case 
		if(data.size()==0) {
			throw new IllegalArgumentException("there is no data"); 
		}
		// a leaf is found 
		if(data.size()==1) {
			data.get(0).level = level; 
			return data.get(0); 
		}
		// sort the nodes based on the current dimension using the comparator 
		Collections.sort(data, c);
		_middle = data.size()/2; 
		KdTreeNode newMid = data.get(_middle);
		System.out.println("mid" + newMid); 
		//assign the root instance if it doesn't yet exist 
		if(_root == null && level==0) {
			if(_root == null) {
				_root = newMid; 
				_root.level = level;
			}
		}	
		List<KdTreeNode> newLeft =  new ArrayList<KdTreeNode>(data.subList(0, _middle)); 
		List<KdTreeNode> newRight =   new ArrayList<KdTreeNode>(data.subList(_middle+1, data.size())); 
		// use traditional binary tree construction instead, don't pass in nodes 
		// recursively pass up the array of data, and get middle element instead
		// set middle element 
		KdTreeNode leftNode = null; 
		KdTreeNode rightNode = null; 
		if(newLeft.size()>0) {
			leftNode = this.buildTree(c, newLeft, level+1); 
			System.out.println("left" + leftNode) ; 
		}
		//visit current node 
		newMid.setLeft(leftNode); 
		if(newRight.size()>0) {
			rightNode = this.buildTree(c, newRight,level+1);
			System.out.println("right" + rightNode); 
		}
		//visit current node 
		newMid.setRight(rightNode);
		// assign the level of the current node 
		newMid.level = level;
		return newMid; 
	}
	
	//helper method that returns double to find the euclidean distance between two coordinates 
	public double findistance(Coordinates first, Coordinates second) {
		double dist_d = 0; 
		double dist = 0; 
		for(int i= 0; i<first.getValues().length; i++) {
			dist_d = Math.abs(first.getValueAtDimension(i)-second.getValueAtDimension(i)); 
			dist += (dist_d*dist_d);
		}
		return Math.sqrt(dist);
	 }
	
	//helper function regular axis distance that returns double 
	public double axisDistance(Coordinates n1, Coordinates n2, int d) {
		double a = n1.getValueAtDimension(d); 
		double b = n2.getValueAtDimension(d); 
		return Math.abs(a-b); 
	}
	
	// recursive function to find the closest neighbors, returns a priority queue 
	public PriorityQueue<KdTreeNode> getClosest2(PriorityQueue<KdTreeNode> neigh, DistanceComparator<KdTreeNode> d, 
			int num_neighbors, KdTreeNode targ, KdTreeNode curr) {
		if (curr==null) {
			return neigh;
		}
		double currToTarget = d.distance(curr.coords, targ.coords); 
		double maxDistance = (!neigh.isEmpty()) ? d.distance(neigh.peek().coords, targ.coords) : 
			Double.MAX_VALUE;
		//if (neigh.size() < num_neighbors && !curr.equals(targ) && !neigh.contains(curr)) {
		if (neigh.size() <= num_neighbors) {
			neigh.add(curr);
		} 
		//else if (neigh.size() >= num_neighbors && currToTarget < maxDistance && !curr.equals(targ)&& !neigh.contains(curr)) {
		else if(currToTarget < maxDistance){
			neigh.poll();
			neigh.add(curr);
		}
		int dim = curr.level % _dimensions; // set this to the number of dimensions 
		double axis_d = axisDistance(curr.coords, targ.coords,dim);
		//get the euclidean distance of furthest node from the target 
		maxDistance = d.distance(neigh.peek().coords, targ.coords);
		if (maxDistance >= axis_d) {
			//recur on both 
			this.getClosest2(neigh, d, num_neighbors, targ, curr.getLeft());
			this.getClosest2(neigh, d, num_neighbors, targ, curr.getRight());
				
		} else if (curr.coords.getValueAtDimension(dim) < targ.coords.getValueAtDimension(dim)) {
			this.getClosest2(neigh, d, num_neighbors, targ, curr.getRight());
		} else {
			this.getClosest2(neigh,  d, num_neighbors, targ, curr.getLeft());
		}
		return neigh;
		
	}
	
	/*
	 * neighbors function that calls the recursive function
	 * returns an ArrayList of strings of the IDs of the closest neighbors 
	 */
	public ArrayList<String> neighbors(KdTreeNode target, int k, KdTreeNode root) {
		if(k==0) {
			return null; 
		}
		if(k==1 && target!=_data.get(0)) {
			System.out.println((_data.get(0)).id); 
		}
		PriorityQueue<KdTreeNode> pq = new PriorityQueue<KdTreeNode>(k,_distComparator.reversed());
		_distComparator.setTarg(target);
		PriorityQueue<KdTreeNode> results = this.getClosest2(pq, _distComparator, k, target, root);
		ArrayList<KdTreeNode> closest = new ArrayList<KdTreeNode>(results); 
		ArrayList<String> output =  new ArrayList<String>(); 
		Collections.sort(closest, _distComparator);
		for (KdTreeNode r: closest) {
			if(r!=target) {
				System.out.println(r.id);
				output.add(r.id); 
			}
		}
		return output; 
	}

		
	private class KdComparator<T extends KdTreeNode> implements Comparator<T>{
		int _dimension;
		public void setDimension(int dim) {
			_dimension = dim; 
		}
		@Override
		public int compare(KdTreeNode n1, KdTreeNode n2) {
			// System.out.println("first node" + n1); 
			// System.out.println("second node" + n2); 
			// System.out.println("dimension" + _dimension); 
			Coordinates first = n1.coords; 
			double val1 = first.getValueAtDimension(_dimension); 
			Coordinates second = n2.coords; 
			double val2 = second.getValueAtDimension(_dimension); 
			return Double.compare(val1 , val2);
		}
	}
	
	private class DistanceComparator<T extends KdTreeNode> implements Comparator<T>{
		KdTreeNode _comparatorTarget; 
		public void setTarg(KdTreeNode target) {
			_comparatorTarget = target; 
		}
		//euclidean distance (?)
		public double distance(Coordinates first, Coordinates second) {
			double dist_d = 0; 
			double dist = 0; 
			for(int i= 0; i<first.getValues().length; i++) {
				dist_d = first.getValueAtDimension(i)-second.getValueAtDimension(i); 
				dist += (dist_d*dist_d);
			}
			return Math.sqrt(dist);
		 }
		@Override
		public int compare(KdTreeNode n1, KdTreeNode n2) {
			Coordinates first = n1.coords; 
			Coordinates second = n2.coords; 
			Coordinates targ_c = _comparatorTarget.coords; 
			double dist_1 = this.distance(first,targ_c);
			double dist_2 = this.distance(second,targ_c);
			return Double.compare(dist_1,dist_2); 
		}
	}
	
		
}
		
	
	
	


