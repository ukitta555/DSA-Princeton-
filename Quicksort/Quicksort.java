import edu.princeton.cs.algs4.StdRandom;

public class Quicksort {
	public static void sort (int [] a) {
		StdRandom.shuffle (a);
		sort (a, 0, a.length-1);
		}
		
	private static void sort (int [] a, int lo, int hi) {
		if (hi <= lo) return ;
		int j = partition (a, lo, hi);
		sort (a, lo, j-1);
		sort (a, j+1, hi);
		} 
	
	private static int partition (int [] a, int lo, int hi) {
		int i=lo;
		int j=hi+1;
		while (true) {
			
			while (a[lo] > a[++i]) {
				if (i==hi) break; // break the while loop
				}
			while (a[lo] < a[--j]) {
				if (j==lo) break; // break the while loop
				}
			
			if (i>=j) break; //pointers crossed
			
			//swap a[i] with  a[j]
			int swap = a[i];
			a[i]=a[j];
			a[j]=swap;
			
			}
		//swap the partitioning element with the a[j]
		int swap = a[lo];
		a[lo]=a[j];
		a[j]=swap;
		
		return j;
		}
		
	public static void main (String [] argv) {
		int [] a= new int [5];
		for (int i = 0; i < 5; i++) {
			a[i]=i;
			}
		a[0]=12;
		sort (a);
		
		for (int i = 0; i < 5; i++) {
			System.out.print (a[i]+" ");
			}
		}
	}
