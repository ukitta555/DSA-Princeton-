import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Permutation {
    public static void main(String[] args) {
	    int n =  Integer.parseInt(args[0]);
	    // In in=new In (args[1]);
	    // System.out.print (args[1]);
	    RandomizedQueue <String> rq = new RandomizedQueue <>();
	    while (!StdIn.isEmpty()) {
		   rq.enqueue (StdIn.readString());
		    }
	    for (int i = 0; i < n; i++) {
		    StdOut.println(rq.dequeue());
		    } 
		}
	}
