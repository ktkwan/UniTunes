package edu.brown.cs.student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.student.brown_spotify.Song;
import edu.brown.cs.student.kdtree.Coordinates;
import edu.brown.cs.student.kdtree.KdTree;
import edu.brown.cs.student.kdtree.KdTreeNode;

public class KDTreeTest {
    private KdTree<Song> tree1;
    private KdTree<Song> tree2;
    private KdTree<Song> tree3;

    private KdTreeNode root1;
    private KdTreeNode root2;
    private KdTreeNode root3;
    // private KdTreeNode<String> root2;
    // private KdTreeNode<String> root3;

    @Before
    public void setUpTree() {
	ArrayList<KdTreeNode> nodes = new ArrayList<KdTreeNode>();
	 // Create set of coordinates
	    Double[] firstCoord = {
	        1.0, 2.0, 3.0
	    };
	    Double[] secondCoord = {
	        -4.0, 5.0, 6.0
	    };
	    Double[] thirdCoord = {
	        7.0, -8.0, 9.0
	    };
	    
	    Double[] fourthCoord = {
	        10.0, 11.0, -12.0
	    };
	    
	    Coordinates firstCoor = new Coordinates(thirdCoord);
	    Coordinates secondCoor = new Coordinates(thirdCoord);
	    Coordinates thirdCoor = new Coordinates(thirdCoord);
	    Coordinates fourthCoor = new Coordinates(thirdCoord);
	    
	    /*
	    KdTreeNode first = new KdTreeNode(null, null, firstCoor, null);
	    KdTreeNode second = new KdTreeNode("second", secondCoor);
	    KdTreeNode third = new KdTreeNode("third", thirdCoor);
	    KdTreeNode fourth = new KdTreeNode("fourth", fourthCoor);
	   
	    nodes.add(first);
	    nodes.add(second);
	    nodes.add(third);
	    nodes.add(fourth);
	    // Populate tree.
	    tree1 = new KdTree<Song>(nodes, 3);
	    //not sure what to do about KdComparator 
	    root1 = tree1.buildTree(null, nodes, 0);
	    
	    */

    }

}
