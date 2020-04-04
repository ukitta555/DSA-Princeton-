import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private int capacity;
	private int numOfCurrentItem;
	private Item [] randomizedQueue;

	
    // construct an empty randomized queue	
    public RandomizedQueue() {
		this.capacity = 1;
		this.numOfCurrentItem = 0;
		this.randomizedQueue = (Item [])new Object [1];
	   }
	   
	// is the randomized queue empty?  
    public boolean isEmpty() {
		if (numOfCurrentItem == 0) return true;
		return false;
		}                
		
	// return the number of items on the randomized queue
    public int size() {
		return numOfCurrentItem;
		}   
    
    //change the size of the queue
    private void resize(int size) {
		Item [] copy = (Item []) new Object [size];
		capacity = size;
		for (int i=0;i<numOfCurrentItem;i++) {
			 copy[i] = randomizedQueue[i];
			}
		randomizedQueue = copy;
		}
    
    
    // add the item                       
    public void enqueue(Item item)  {
		if (item == null) throw new IllegalArgumentException ();
		if (capacity == numOfCurrentItem) {
			 resize (capacity*2);
			}
		randomizedQueue[numOfCurrentItem] = item;
        numOfCurrentItem++;
		}
	
    // remove and return a random item	       
    public Item dequeue() {
		
		if (isEmpty()) throw new NoSuchElementException ();
		
		// check if we need to resize the queue
		if (capacity/4 == numOfCurrentItem) {
			 resize (capacity/4);
			}
		
		//find a random index and save the item
		int randomIndex = StdRandom.uniform (0,numOfCurrentItem);
		Item item = randomizedQueue[randomIndex];
		
		
		//put the last element in the random index
		randomizedQueue[randomIndex] = randomizedQueue[--numOfCurrentItem];
		
		//avoid loitering
		randomizedQueue[numOfCurrentItem] = null;
		
		return item;
		}
     
    // return a random item (but do not remove it)         
    public Item sample() {
		if (isEmpty()) throw new NoSuchElementException ();
		
		int numOfRandomItem = StdRandom.uniform (0, numOfCurrentItem);
		return randomizedQueue[numOfRandomItem];
		}
	
	
	//nested class for iterator
	
	private class RandomizedQueueIterator implements Iterator <Item> {
		private int curItem;
		private Item [] shuffledRandomizedQueue;
		
		public RandomizedQueueIterator (Item [] randomizedQueue,int n) {
			this.curItem = 0;
			this.shuffledRandomizedQueue = (Item [])new Object [capacity]; 
			
			for (int i = 0; i < n; i++) {
				this.shuffledRandomizedQueue [i] = randomizedQueue [i];
				}
				
			StdRandom.shuffle (this.shuffledRandomizedQueue,0, n);
			}
			
		public  boolean hasNext() {
			if (curItem==capacity || shuffledRandomizedQueue[curItem]==null) {
				 return false;
				}
			else return true;
			} 
		
		public void remove () {
			 throw new UnsupportedOperationException ();
			}
		
		public Item next () {
			if (!hasNext()) throw new NoSuchElementException ();
			Item item = shuffledRandomizedQueue [curItem];
			curItem++;
			return item;
			}
		
		}
	
	
    // return an independent iterator over items in random order                
    public Iterator<Item> iterator() {
		return new RandomizedQueueIterator (randomizedQueue,numOfCurrentItem);
		}
		
    // unit testing (optional)   
    public static void main(String[] args) {
		RandomizedQueue <String> rq = new RandomizedQueue <String> ();
		StdOut.println (rq.size());
		
		rq.enqueue ("I ");
		rq.enqueue ("like ");
		rq.enqueue ("memes ");
			
			
		Iterator <String> it=rq.iterator ();
		Iterator <String> it_copy=rq.iterator ();
		
		StdOut.println ("Iterator1 output:");
		while (it.hasNext ()) {
			StdOut.println(it.next());
			}
			
		StdOut.println ("Iterator2 output:");
		while (it_copy.hasNext ()) {
			StdOut.println(it_copy.next());
			}
					
		
		
		rq.dequeue();
		rq.dequeue();
		
		it=rq.iterator ();
		while (it.hasNext ()) {
			StdOut.println(it.next());
			}
				
		StdOut.println (rq.size());
		} 
}
