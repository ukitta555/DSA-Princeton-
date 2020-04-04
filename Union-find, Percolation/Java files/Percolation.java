import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
   private byte [][] grid=null; // grid itself
   private int size; // stands for n
   private int numberOfOpenSites; // the number of sites that are open
   private WeightedQuickUnionUF u=null;
   private WeightedQuickUnionUF startOnly=null; // WQU data structure
   
   public Percolation (int n) { // create n-by-n grid, with all sites blocked
	   if (n <= 0) throw new IllegalArgumentException(); 
	   this.grid = new byte [n][n];
	   this.size = n;
	   // 0 stands for blocked, 1 for open
	   for (int i = 0;i < n;i++){
		   for (int j = 0;j < n;j++) {
			    grid[i][j] = 0;
			   }
		   }
		// initialize the WQU data structure with 2 invisible objects
	  this.u = new WeightedQuickUnionUF((size*size)+2);
	 this.startOnly = new WeightedQuickUnionUF((size*size)+1);
   }          
     
   public void open (int row, int col) { // open site (row, col) if it is not open already
	     if ( (row <= 0) || (row > size) || (col <= 0) || (col > size)) throw new IllegalArgumentException();
	     
	   if (grid [row-1][col-1] == 0) {
	      grid [row-1][col-1] = 1;
	      numberOfOpenSites++;
	    
	    if (col != size && isOpen(row,col+1) == true) {
			u.union (to1D (row, col+1),to1D (row, col)); // union with the right oblect
			startOnly.union (to1D (row, col+1), to1D (row, col));
	    }
	    
	    if (col != 1 && isOpen(row,col-1) == true) {
			u.union (to1D (row, col-1), to1D (row, col)); // union with the left oblect
			startOnly.union (to1D (row, col-1), to1D (row, col));
	    }
	     
	    if (row == size){
			    u.union ((size*size+1), to1D (row, col)); // if it is row 20, then we connect it to the hidden lower obect
			   
			}
        else {
			 if (isOpen(row+1,col) == true) {
				 u.union (to1D (row+1, col), to1D (row, col));
				 startOnly.union (to1D (row+1, col), to1D (row, col));  // union with the lower oblect
				}
		    }
		    
	    if (row == 1){
			   u.union (0,to1D (row, col));  // if it is row 1, then we connect it to the hidden upper obect
			   startOnly.union (0, to1D (row, col) ) ;
			}
        else {
				if (isOpen (row-1,col) == true) {
					 u.union (to1D (row-1,col),to1D (row,col)); // union with the upper object
					 startOnly.union (to1D (row-1,col),to1D (row,col)); 
					}	
		    }

    }
 }
 
   public boolean isOpen (int row, int col) { // is site (row, col) open?
	    if ( (row <= 0) || (row > size) || (col <= 0) || (col > size)) throw new IllegalArgumentException(); 
	    if (grid [row-1][col-1] == 0) return false;
	    return true;
	   } 
	
   public boolean isFull (int row, int col) {// is site (row, col) full?
	   if ( (row <= 0) || (row > size) || (col <= 0) || (col > size)) throw new IllegalArgumentException(); 
	   //if both roots are the same, reutrn true
	   if (startOnly.connected (to1D (row, col), 0))
	     return true;
	   return false;
	   }
 
   public  int numberOfOpenSites ()  {// number of open sites
	    return numberOfOpenSites;
	   }     
	   
   public boolean percolates ()   {// does the system percolate?
	  // if two invisible objects are in the same component, the system percolates.
	    if (u.connected (0, (size*size+1)))
	     return true;
	    return false;
	   }           

   private int to1D (int row, int col) {
	   return ((row-1) * size + col);
	   }

   public static void main(String[] args){
	    Percolation p=new Percolation(5);
	    p.open (1, 1);
	    p.open (1, 2);
	    p.open (2, 2);
	    p.open (3, 2);
	    p.open (4, 2);
	    //p.open (5,2);
	    System.out.println (p.percolates ());
	    for (int i = 0;i < p.size; i++){
		   for (int j = 0;j < p.size; j++) {
			    System.out.print (p.grid [i][j]);
			   }
			System.out.println ();
		   } 

	   }   // test client (optional)
}
