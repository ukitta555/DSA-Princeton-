import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FastCollinearPoints {
	
    private int numOfSegments=0;
    private ArrayList <LineSegment> allSegments; 
    private Point [] pts_copy;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
		
		if (points == null) throw new IllegalArgumentException ();
	    
	    this.pts_copy = new Point [points.length+1];
	
	    this.allSegments= new ArrayList <> ();
	    
	    
	    
	    Point prev = new Point (-1, -1);
	    
	    for (int i = 0; i < points.length; i++ ) {
			if (points[i]==null) throw new IllegalArgumentException ();
		    this.pts_copy[i] = points[i];
		   }
		
		Arrays.sort (this.pts_copy, 0, points.length);
		
		for (int i = 0; i < points.length; i++ ) {
			if (prev.compareTo (this.pts_copy[i]) == 0) throw new IllegalArgumentException ();
			else prev=this.pts_copy[i];
		   }
		
			
		for (int i = 0; i < points.length; i++) {
			
			//dummy element; created in order to display lines with biggest slopes  
			this.pts_copy[points.length] = points[i];  
			
			/*sort the array so that it 
			* stays sorted by coordinates 
			* after sorting by slopes 
			* (uses mergesort)*/
					
			Arrays.sort (this.pts_copy, 0, points.length); 
			Arrays.sort (this.pts_copy, 0, points.length, points[i].slopeOrder());
				    
          
			
			double val = Double.POSITIVE_INFINITY;
			int counter=1;
			int startingIndex=0;
			
			for (int j = 0; j < this.pts_copy.length; j++ ) {
				if (val==points[i].slopeTo (this.pts_copy[j])) {
					counter++;
					}
					else {
						if (counter>=4){
							if (pts_copy[0].compareTo (pts_copy[startingIndex]) < 0 ) {
								 //add sgement
								LineSegment temp=new LineSegment (pts_copy[0], pts_copy[j-1]);
									this.allSegments.add (new LineSegment (pts_copy[0], pts_copy[j-1]));
									this.numOfSegments++; // increment the number of segments
								}
							}   			 
						counter=2;
						startingIndex = j;
						val=points[i].slopeTo (this.pts_copy[j]);
						}
			
			}
	    }
	}
	   
	   
	 
	
    public int numberOfSegments()  { // the number of line segments
	    return numOfSegments;
	    }       
	
	 // the line segments

    public LineSegment[] segments()   {
		return allSegments.toArray (new LineSegment [allSegments.size()]);
		}            
}
