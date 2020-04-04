import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.SET;
import java.util.ArrayList;

public class PointSET {
	private SET <Point2D> BST;
	 
	public PointSET() { // construct an empty set of points
		this.BST = new SET <>();
		}                                
    public boolean isEmpty() { // is the set empty?
		return BST.isEmpty();
		} // works                      
    public int size() { // number of points in the set
		return BST.size();
		}//works                     
    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		if (p == null) throw new IllegalArgumentException ();
		BST.add (p);
		} //works              
    public boolean contains(Point2D p) { // does the set contain point p?
		if (p == null) throw new IllegalArgumentException ();
		return BST.contains (p); 
		} //works
    public void draw() { // draw all points to standard draw 
		StdDraw.setXscale (0, 1);
		StdDraw.setYscale (0, 1);
		for (Point2D p : BST) {
			StdDraw.point(p.x(), p.y());
			}
		} // works
    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
		if (rect == null) throw new IllegalArgumentException ();
		ArrayList <Point2D> al = new ArrayList <> ();
		for (Point2D p : BST) {
			if (rect.contains (p)) {
				al.add(p);
				}
			}
		return al;
		}// works             
    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
		if (p == null) throw new IllegalArgumentException ();
		if (isEmpty()) return null;
		Point2D min = BST.min ();
		for (Point2D currentPoint : BST) {
			if (currentPoint.distanceSquaredTo (p) < min.distanceSquaredTo (p)) {
				min = currentPoint ;
				}
			}
		return min;
		}// works          
    public static void main(String[] args) { // unit testing of the methods (optional)
		// creating new object
		PointSET tmp = new PointSET ();
		// isEmpty test - should return true
		System.out.println (tmp.isEmpty());
		// inserting 6 test points
		Point2D pts = new Point2D (0.1, 0.10); 
		tmp.insert (pts);
		Point2D pts1 = new Point2D (0.20, 0.20); 
		tmp.insert (pts1);
		Point2D pts2 = new Point2D (0.05, 0.05);
		tmp.insert (pts2);
		Point2D pts3 = new Point2D (0.9, 0.05);
		tmp.insert (pts3);
		Point2D pts4 = new Point2D (0.4, 0.4);
		tmp.insert (pts4);
		Point2D pts5 = new Point2D (0.4, 0.7);
		tmp.insert (pts5);
		// draw () test - should display 6 points
		tmp.draw();
		// insert () + contains () tests - if works properly, contatins returns true true false
		Point2D not = new Point2D (0.30, 0.30);
		System.out.println (tmp.contains (pts)+" "+tmp.contains (pts1)+" "+tmp.contains (not));
		// size() test - should write 6
		System.out.println (tmp.size());
		// isEmpty test - should return false
		System.out.println (tmp.isEmpty());
		
		
		// nearest () test - should return (0.1 0.1)
		Point2D p2 = new Point2D (0.15, 0.15);
		System.out.println (tmp.nearest (p2));
		// range () test - should return   (0.05 0.05)(0.1 0.1)(0.2 0.2)
		RectHV rect = new RectHV (0, 0, 0.25, 0.25);
		ArrayList <Point2D> al = (ArrayList <Point2D>)tmp.range (rect);
		for (Point2D point : al) {
			System.out.print (point+" ");
			}
		}                  
}

