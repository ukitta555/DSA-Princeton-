/** Write a program BruteCollinearPoints.java that examines 4 points
     * at a time and checks whether they all lie on the same line segment, 
     * returning all such line segments. To check whether the 4 points
     * p, q, r, and s are collinear, 
     * check whether the three slopes between p and q,
     *  between p and r, and between p and s are all equal.*/

import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
    
    private int counter=0;
    private ArrayList <LineSegment> allSegments;
    private Point[] fourPoints;
        
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
		
		if (points==null) throw new IllegalArgumentException ();
		
		for (int i=0; i < points.length; i++) {
			if (points [i] == null) throw new IllegalArgumentException();
			}
		
		this.allSegments=new ArrayList<> (); // array for storing segments
		Point [] pts4 = new Point [4]; // array for points
		Point [] pts_copy = new Point [4]; //copy for sorting 
		
		
		if (points.length == 3) {
			if (points[0].compareTo (points[1]) == 0 || points[1].compareTo (points[2]) == 0 || points[0].compareTo (points[2]) == 0) {
				throw new IllegalArgumentException ();
				}
			}
		
		
		if (points.length == 2) {
			if (points[0].compareTo (points[1]) == 0) {
				throw new IllegalArgumentException ();
				}
			}
		
		for (int p = 0; p < points.length-3; p++) {
			pts4[0]=points[p]; 
			for (int q = p+1; q < points.length-2; q++) {
				pts4[1]=points[q];
				if (pts4[0].compareTo (pts4[1]) == 0) {
					throw new IllegalArgumentException ();
					}	
				for (int r = q+1; r < points.length-1; r++) {
					
					pts4[2]=points[r];
					
					if (pts4[0].compareTo (pts4[2]) == 0 || pts4[1].compareTo (pts4[2]) == 0) {
						throw new IllegalArgumentException ();
						}
						for (int s = r+1; s < points.length; s++) {
							pts4[3]=points[s];
							
							
							
							if (pts4[0].compareTo (pts4[3]) == 0 || pts4[1].compareTo (pts4[3]) == 0 || pts4[2].compareTo (pts4[3]) == 0) {
								throw new IllegalArgumentException ();
								}
							
							if (pts4[0].slopeTo(pts4[1]) != pts4[0].slopeTo (pts4[2])) {
							  break;
							}
								
							for (int i = 0; i < 4; i++) {
								pts_copy[i]=pts4[i];
								}
		
							Arrays.sort (pts_copy);
						
							if ( pts_copy[0].slopeTo(pts_copy[1]) == pts_copy[0].slopeTo (pts_copy[2]) && pts_copy[0].slopeTo(pts_copy[1]) == pts_copy[0].slopeTo (pts_copy[3]) ) {
								this.counter++;
								this.allSegments.add(new LineSegment(pts_copy[0],pts_copy[3]));
								}
							}
						}			
				}
			}
	    }  
    
    

	
		
    // the number of line segments
    public int numberOfSegments()  {
		return counter;
	    }      
   
   
    // the line segments
    public LineSegment[] segments() {
		return allSegments.toArray (new LineSegment [allSegments.size()]);
	    }
}
