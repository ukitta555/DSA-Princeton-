import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
public class SAP {
	private final Digraph G;
    // constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G)
	{
		this.G = new Digraph (G);
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w)
	{
		if (v < 0 || w < 0 || v >= G.V () || w >= G.V ()) throw new IllegalArgumentException ();
		 
		BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths (G, v);
		BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths (G, w);
		boolean f = false;
		int minDist = Integer.MAX_VALUE; 
		for (int i = 0; i < G.V(); i++)
		{
			int distV = pathV.distTo (i);
			int distW = pathW.distTo (i);
			int total = distV + distW;
			f = total < minDist;
			if (pathV.hasPathTo (i) && pathW.hasPathTo (i) && f)
			{
				minDist = total;
			}
		}
		if (minDist == Integer.MAX_VALUE) return -1;
		return minDist;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w)
	{
		if (v < 0 || w < 0 || v >= G.V () || w >= G.V ()) throw new IllegalArgumentException ();
		
		BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths (G, v);
		BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths (G, w);
		boolean f = false;
		int minDist = Integer.MAX_VALUE; 
		int ancestor = -1;
		for (int i = 0; i < G.V(); i++)
		{
			int distV = pathV.distTo (i);
			int distW = pathW.distTo (i);
			int total = distV + distW;
			f = total < minDist;
			if (pathV.hasPathTo (i) && pathW.hasPathTo (i) && f)
			{
				minDist = total;
				ancestor = i;
			}
		}
		if (minDist == Integer.MAX_VALUE) return -1;
		return ancestor;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w)
	{
		if (v == null || w == null) throw new IllegalArgumentException ();
		for (Integer tmp : v) 
		{
			if (tmp == null || tmp < 0 || tmp >= G.V ()) throw new IllegalArgumentException ();
		}
		for (Integer tmp : w) 
		{
			if (tmp == null || tmp < 0 || tmp >= G.V ()) throw new IllegalArgumentException ();
		}
		BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths (G, v);
		BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths (G, w);
		boolean f = false;
		int minDist = Integer.MAX_VALUE; 
		for (int i = 0; i < G.V(); i++)
		{
			f = (pathV.distTo (i) + pathW.distTo (i) < minDist);
			if (pathV.hasPathTo (i) && pathW.hasPathTo (i) && f)
			{
				minDist = pathV.distTo (i) + pathW.distTo (i);
			}
		}
		if (minDist == Integer.MAX_VALUE) return -1;
		return minDist;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		if (v == null || w == null) throw new IllegalArgumentException ();
		for (Integer tmp : v) 
		{
			if (tmp == null || tmp < 0 || tmp >= G.V ()) throw new IllegalArgumentException ();
		}
		for (Integer tmp : w) 
		{
			if (tmp == null || tmp < 0 || tmp >= G.V ()) throw new IllegalArgumentException ();
		}
		BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths (G, v);
		BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths (G, w);
		boolean f = false;
		int minDist = Integer.MAX_VALUE; 
		int ancestor = -1;
		for (int i = 0; i < G.V(); i++)
		{
			f = (pathV.distTo (i) + pathW.distTo (i) < minDist);
			if (pathV.hasPathTo (i) && pathW.hasPathTo (i) && f)
			{
				minDist = pathV.distTo (i) + pathW.distTo (i);
				ancestor = i;
			}
		}
		if (minDist == Integer.MAX_VALUE) return -1;
		return ancestor;
	}
	
	// do unit testing of this class
	public static void main(String[] args) 
	{
		/*
		Digraph G = new Digraph (5);
		G.addEdge (1, 2);
		G.addEdge (4, 3);
		G.addEdge (3, 4);
		G.addEdge (0, 4);
		G.addEdge (1, 4);
		SAP sap = new SAP (G);
		System.out.print (sap.length (0, 0));
        */
        /*
		In in = new In (args[0]);
		Digraph G = new Digraph (in);
		SAP sap = new SAP (G);
		SET set1 = new SET ();
		SET set2 = new SET ();
		set1.add (13);
		set1.add (23);
		set1.add (24);
		set2.add (6);
		set2.add (16);
		set2.add (17);
		System.out.print (sap.length (set1, set2)+" "+sap.ancestor (set1, set2));
	*/
	}
}
