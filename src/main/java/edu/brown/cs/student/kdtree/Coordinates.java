package edu.brown.cs.student.kdtree;

import java.util.ArrayList;
import java.util.List;

/*
 * Coordinates class that has an array of x, y, and z values. KdTree nodes all contain coordinates. 
 */
public class Coordinates {
	Double[] _c;  
	// constructor to initialize the coordinate array
	public Coordinates(Double[] c) {
		_c = c; 
	}
	// get the value at a specific dimension 
	public Double getValueAtDimension(int dim) {
		Double v1 = _c[dim]; 
		return v1; 
	}
	//getter method to access actual values  of coordinates 
	public Double[] getValues() {
		return _c; 
	}
	
}
