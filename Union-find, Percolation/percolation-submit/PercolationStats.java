import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.lang.Math;
public class PercolationStats {
   private int numberOfTrials;
   private int sizeOfGrid;
   private double [] arrayOfResults;
   private double mean;
   private  double std;
   public PercolationStats(int n, int trials) {// perform trials independent experiments on an n-by-n grid
	   if (n<=0 || trials<=0) throw new IllegalArgumentException();
	   this.sizeOfGrid=n;
	   this.numberOfTrials=trials;
	   this.arrayOfResults=new double [trials];
	   int row;
	   int column;
	    for (int i=0;i<trials;i++) { 
		   Percolation p =new Percolation(n);
		   while (p.percolates()==false) {
	         row=StdRandom.uniform(1,n+1);
	         column=StdRandom.uniform(1,n+1);
             p.open(row,column);
             //System.out.println (row+" "+column);
           }
           arrayOfResults [i] =(double) p.numberOfOpenSites()/(double)(n*n);
	     }
	    this.mean=StdStats.mean (arrayOfResults);
	    this.std=StdStats.stddev (arrayOfResults);
	   }  
   public double mean(){   // sample mean of percolation threshold
	   return mean;
	   }                    
   public double stddev() {// sample standard deviation of percolation threshold
	   	return std;
	   
	   }                        
   public double confidenceLo() { // low  endpoint of 95% confidence interval
	   	   return mean-(1.96*std/Math.sqrt(numberOfTrials));
	   
	   }                  
   public double confidenceHi()  { // high endpoint of 95% confidence interval
	   	   return mean+(1.96*std/Math.sqrt(numberOfTrials));
	   }               

   public static void main(String[] args) {  // test client (described below)
	     int n=Integer.parseInt(args[0]);
	     int trials=Integer.parseInt(args[1]);
	     PercolationStats ps=new PercolationStats (n,trials);
	     
	     System.out.print (ps.confidenceLo()+" "+ps.confidenceHi());
	   }       
}
