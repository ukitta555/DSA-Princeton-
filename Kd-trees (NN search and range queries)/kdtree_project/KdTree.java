import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.Queue;
import java.util.LinkedList;
public class KdTree {
	private Node root;
	private Point2D champ;
	private double minDist;
	private static class Node {
		private Node left;
		private Node right;
		private Point2D point;
		private int size;
		public Node (Point2D point) {
			this.point = point;
			this.size = 1;
			}
		}
	private static class TraversalNode {
		private Point2D point;
		private int level;
		private double xmin;
		private double ymin;
		private double xmax;
		private double ymax;
		
		public TraversalNode  (Point2D point, int level, double xmin, 
								double ymin, double xmax, double ymax){
			this.level = level;
			this.point = point;
			this.xmin = xmin;
			this.ymin = ymin;
			this.xmax = xmax;
			this.ymax = ymax;
			} 
		}
		
	public KdTree() // construct an empty set of points 
	{
		this.root = null; 
	}
    public boolean isEmpty() // is the set empty? 
    {
	   return (root == null);
	}
    public int size() // number of points in the set
    {
		if (isEmpty ()) return 0;
		return root.size;
	} 
    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
		if (p == null) throw new IllegalArgumentException ();
		int level = 1; 
		root = insert (root, p, level);
		//System.out.println (size());
	}
	private Node insert (Node  node, Point2D point, int level)  //private recursive method for insert ()
	{ 
		if (node == null) return new Node (point);
		double nodeKey;
		double currentKey;
		double otherNodeKey;
		double otherCurrentKey;
		// define what coordinate are we using to compare points
		if (level % 2 != 0) {
			 nodeKey = node.point.x();
			 currentKey = point.x();
			 otherNodeKey = node.point.y();
			 otherCurrentKey = point.y();
			}
			else {
				nodeKey = node.point.y();
				currentKey = point.y();
				otherNodeKey = node.point.x();
				otherCurrentKey = point.x();
				}
		//go right
		if (nodeKey < currentKey || (nodeKey == currentKey && otherNodeKey < otherCurrentKey)) 
			{
				node.right = insert (node.right, point, level+1);
			}
			// go left
			else if (nodeKey > currentKey || (nodeKey == currentKey && otherNodeKey > otherCurrentKey))
				{
					 node.left = insert (node.left, point, level+1);
				}
				// or the point is already in the tree
				else return node;
		//recursively update the size of the tree
		int leftSize;
		int rightSize;
		if (node.left != null)
		{
			leftSize = node.left.size;
			} 
			else
			{
				leftSize = 0;
			}
		if (node.right != null)
		{
			rightSize = node.right.size;
		}
			else
			{
				rightSize = 0;
			}
		
		node.size = 1 + leftSize + rightSize;
		return node;
		}
    public boolean contains (Point2D p) // does the set contain point p?
	{	
		if (p == null) throw new IllegalArgumentException ();
		return contains (root, p, 1);
	}	
    private boolean contains(Node node, Point2D point, int level) // private method for contains()
    {
		if (node == null) return false;
		double nodeKey;
		double currentKey;
		double otherNodeKey;
		double otherCurrentKey;
		// define what coordinate are we using to compare points
		if (level % 2 != 0) {
			 nodeKey = node.point.x();
			 currentKey = point.x();
			 otherNodeKey = node.point.y();
			 otherCurrentKey = point.y();
			}
			else {
				nodeKey = node.point.y();
				currentKey = point.y();
				otherNodeKey = node.point.x();
				otherCurrentKey = point.x();
				}
		// if we go right
		if (nodeKey < currentKey || (nodeKey == currentKey && otherNodeKey < otherCurrentKey))
		{
			return contains (node.right, point, level+1);
		}
		//if we go left
		else if (nodeKey > currentKey || (nodeKey == currentKey && otherNodeKey > otherCurrentKey))  
		{
			return contains (node.left, point, level+1);
		}
		//if the point is already in the tree
		else if (nodeKey == currentKey && otherNodeKey == otherCurrentKey)
		{
			return true;	
		}
	return false;
	} 
    public void draw() // draw all points to standard draw
    {
		Queue <TraversalNode> q = new LinkedList <TraversalNode> ();
		inorder (root, q, 1, 0, 0, 1, 1); //node, queue, level, xmin, ymin,xmax, ymax
		double x;
		double y;
		StdDraw.setXscale (0,1);
		StdDraw.setYscale (0,1);
		for (TraversalNode node : q) 
		{
			x = node.point.x();
			y = node.point.y();
			double xmin = node.xmin;
			double ymin = node.ymin;
			double xmax = node.xmax;
			double ymax = node.ymax;
			StdDraw.setPenRadius(0.01);
			// depending on the depth of the tree, decide how to draw it
			if (node.level % 2 == 0) 
			{
				//System.out.println ("Blue!");
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.line (xmin, y, xmax, y);
			}
			else
			{	
				//System.out.println ("Red!");
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.line (x, ymin, x, ymax);
			}
			
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(0.02);
			StdDraw.point (x, y);
		}
	} 
	private void inorder (Node node, Queue <TraversalNode> q, int level, 
						  double xmin, double ymin, double xmax, double ymax) // inorder traversal of the tree 
	{ 
		if (node == null) return ;
		double coordChange;
		if (level % 2 != 0)
		{
			coordChange = node.point.x();
			inorder (node.left, q, level+1, xmin, ymin, coordChange, ymax);
			TraversalNode traversalNode = new TraversalNode (node.point, level, xmin, ymin, xmax, ymax);
			q.add (traversalNode);
			inorder (node.right, q, level+1, coordChange, ymin, xmax, ymax);
		}
		else
		{
			coordChange = node.point.y();
			inorder (node.left, q, level+1, xmin, ymin, xmax, coordChange);
			TraversalNode traversalNode = new TraversalNode (node.point, level, xmin, ymin, xmax, ymax);
			q.add (traversalNode);
			inorder (node.right, q, level+1, xmin, coordChange, xmax, ymax);
		}
		
	}
    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle (or on the boundary) 
    {
		if (rect == null) throw new IllegalArgumentException ();
		Queue <Point2D> q = new LinkedList <> ();
		RectHV currentRect = new RectHV (0, 0, 1, 1);
		searchRange (root, q, rect, currentRect, 1); 
		return q;
	}
	
	private void searchRange (Node node, Queue <Point2D> q, RectHV rect,
							  RectHV currentRect, int level)  //recursive method for range()
	{
		if (node == null) return ;
		// vertical line (red)
		if (level % 2 != 0) 
		{
			RectHV left = new RectHV (currentRect.xmin(),currentRect.ymin(), node.point.x(), currentRect.ymax());
			RectHV right = new RectHV (node.point.x(), currentRect.ymin(), currentRect.xmax(), currentRect.ymax());
			if (rect.contains(node.point))
			{
				q.add (node.point);
			}
			if (rect.intersects (left) && rect.intersects (right))
			{
				searchRange (node.left, q, rect, left, level+1);
				searchRange (node.right, q, rect, right, level+1);
			}
			else if (rect.intersects (right))
				{
					searchRange (node.right, q, rect, right, level+1);
				}
				else if (rect.intersects (left))
					{
						searchRange (node.left, q, rect, left, level+1);
					}
		}
		// horizontal line (blue)
		else
		{
			RectHV top = new RectHV (currentRect.xmin(), node.point.y(), currentRect.xmax(), currentRect.ymax());
			RectHV down = new RectHV (currentRect.xmin(), currentRect.ymin(), currentRect.xmax(), node.point.y());
			if (rect.contains (node.point)) 
			{
				q.add (node.point);
			}
			if (rect.intersects (down) && rect.intersects (top))
			{
				searchRange (node.left, q, rect, down, level+1);
				searchRange (node.right, q, rect, top, level+1);
			}
			else if (rect.intersects (top))
				{
					searchRange (node.right, q, rect, top, level+1);
				}
				else if (rect.intersects (down))
					{
						searchRange (node.left, q, rect, down, level+1);
					}

		}
	}
	
    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty 
	{
		if (p == null) throw new IllegalArgumentException();
		if (isEmpty()) return null;
		RectHV rect = new RectHV (0, 0, 1, 1);
		champ = null;
		minDist = Double.POSITIVE_INFINITY;
		int level = 1;
		nearest (root, rect, p, level);
		return champ;
	}
	
	private void nearest (Node node, RectHV rect, Point2D p, int level) 
	{
		double dist = node.point.distanceSquaredTo (p);
		if (dist < minDist)
		{
			minDist = dist;
			champ = node.point;
		} 
	    double xmin = rect.xmin (); 
	    double xmax = rect.xmax ();
	    double ymin = rect.ymin ();
	    double ymax = rect.ymax ();
	    double px = node.point.x();
	    double py = node.point.y ();
		if (level % 2 != 0) 
		{
			RectHV left = new RectHV (xmin, ymin, px, ymax);
			RectHV right = new RectHV (px, ymin, xmax, ymax);
			double lDist = left.distanceSquaredTo (p);
			double rDist = right.distanceSquaredTo (p);
			if (lDist < minDist && lDist <= rDist) 
			{
				if (node.left != null) nearest (node.left, left, p, level+1);
				if (rDist < minDist)
				{
					if  (node.right != null) nearest (node.right, right, p, level+1);
				} 
			}
			else if (rDist < minDist && rDist <= lDist)
			{
				if (node.right != null) nearest (node.right, right, p, level+1);
				if (lDist < minDist)
				{
					if (node.left != null) nearest (node.left, left, p, level+1);
				} 
			}
		}
		else
		{
			RectHV top = new RectHV (xmin, py, xmax, ymax);
			RectHV down = new RectHV (xmin, ymin, xmax, py);
			double tDist = top.distanceSquaredTo (p);
			double dDist = down.distanceSquaredTo (p);
			if (dDist < minDist && dDist <= tDist)
			{
				if (node.left != null) nearest (node.left, down, p, level+1);
				if (tDist < minDist)
				{
					if (node.right != null) nearest (node.right, top, p, level+1);
				} 
			}
			else if (tDist < minDist && tDist <= dDist)
			{
				if (node.right != null) nearest (node.right, top, p, level+1);
				if (dDist < minDist)
				{
					if (node.left != null) nearest (node.left, down, p, level+1);
				} 
			} 
		}	
	}
    public static void main(String[] args)                   // unit testing of the methods (optional) 
	{
		// creating new object
		KdTree tmp = new KdTree ();
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
		Queue <Point2D> al = (Queue <Point2D>)tmp.range (rect);
		for (Point2D point : al) {
			System.out.print (point+" ");
			}
	}
}
