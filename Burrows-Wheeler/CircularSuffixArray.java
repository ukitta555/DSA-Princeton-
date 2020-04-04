import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
public class CircularSuffixArray {
	private CircularSuffix[] suffixes;
	private int length;
	private String s;
	
	private class CircularSuffix  
	{
		int startingCharIndex;
		
		public CircularSuffix (int startingCharIndex)
		{
			this.startingCharIndex = startingCharIndex;
		}
		
		public int charAt (int offset)
		{
			if (offset >= length) return -1;
			return s.charAt ((startingCharIndex + offset) % length);
		}
		
		
		// for debugging purposes
		public String toString()
		{
			String tmp = new String();
			for (int i = 0; i < s.length(); i++)
			{
				tmp += (char)charAt(i) ;
			}
			return tmp;
		}
		
	}
	
	
	private class Suffix3WayQuicksort
	{
		public void sort ()
		{
			sort (0, suffixes.length-1, 0);
		}
		
		private void sort (int lo, int hi, int d)
		{
			if (hi <= lo) return;
			int lt = lo;
			int gt = hi;
			int v = suffixes[lo].charAt(d);
			int i = lo + 1;
			int t;
			while (i <= gt)
			{
				t = suffixes[i].charAt(d);
				if (t < v) exch (i++, lt++);
				else if (t > v) exch (i, gt--);
				else ++i; 
			}
			sort (lo, lt-1, d);
			if (v > -1) sort (lt, gt, d+1);
			sort (gt+1, hi, d);
		}
		
		private void exch (int a, int b)
		{
			CircularSuffix tmpSuffix = suffixes[a];
			suffixes[a] = suffixes[b];
			suffixes[b] = tmpSuffix;
			tmpSuffix = null;
		}
	}
	
    // circular suffix array of s
    public CircularSuffixArray(String s)
    {
		if (s == null) throw new IllegalArgumentException();
		this.s = s;
		this.length = s.length();
		this.suffixes = new CircularSuffix[this.length];
		for (int i = 0; i < this.length; i++)
		{
			suffixes[i] = new CircularSuffix (i);
		}
		Suffix3WayQuicksort quick = new Suffix3WayQuicksort();
		quick.sort();
	}

    // length of s
    public int length()
    {
		return length;
	}

    // returns index of ith sorted suffix
    public int index(int i)
    {
		if (i < 0 || i > length-1) throw new IllegalArgumentException();
		return suffixes[i].startingCharIndex;
	}

    // unit testing (required)
    public static void main(String[] args)
    {
		CircularSuffixArray suffixes = new CircularSuffixArray("ABRACADABRA!");
		StdOut.println(suffixes.length());
		StdOut.println(suffixes.index(3));
	}
}
