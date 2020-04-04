import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
	
	private Node first,last;
	private int capacity;
	
    private class Node {
        Node next,prev;
		Item item;
		}
	
	// construct an empty deque
    public Deque() { 
		this.first = null;
		this.last = null;
		this.capacity = 0;
		}                   
   
    // is the deque empty?   
    public boolean isEmpty () {
		if (capacity == 0)
			return true;
		return false;
		}                 
   
    // return the number of items on the deque
    public int size () {
		return capacity;
		}
    
    // add the item to the front
    public void addFirst (Item item) {
		
		// check the for valid input
		if (item == null) {
			throw new IllegalArgumentException ();
			}
		
		//save the previous first element in the deque
		Node oldfirst = first;
		
		//create a new node which points to the old first element
		first = new Node ();
		first.item = item;
		first.next = oldfirst;
		first.prev = null;
		
		if (oldfirst != null) {
			oldfirst.prev = first;
			}
		
		//increse the capacity of the deque
		capacity++;
		
		//check if deque consists of 1 element only
		if (capacity == 1) {
			last = first;
			}
		
		return ;
		}          
   
    // add the item to the end
    public void addLast(Item item) {
		
		// check the for valid input
		if (item == null) {
			throw new IllegalArgumentException ();
			}
		
		// save the previous last element in deque
		Node oldlast = last;
		
		// create a new node, which is the new last element in deque
		last = new Node ();
		last.item = item;
		last.next = null;
		last.prev = oldlast;
		
		// make the old last element point to the new last element
		if (oldlast != null) {
			oldlast.next = last;
			}
		
		
		//increse the capacity of the deque
		capacity++;
		
		//check if deque consists of 1 element only
		if (capacity == 1) {
			first = last;
			}
			
		return ;
		}           
		
    // remove and return the item from the front
    public Item removeFirst ()  {
		 
		/* check if there is an element which can be deleted; 
		 * otherwise throw exception */
		 
		if (isEmpty ()) {
			throw new NoSuchElementException ();
			}
			
		// save the item
		Item item = first.item;
		 
		// remove the first element from deque
		if (capacity > 1) { 
			Node temp=first.next;
			first=null;
			first=temp;
			temp=null;
			first.prev=null;
			}
			else {
				first=null;
				last=null;
				}
		//decrease the capacity
		capacity--;
		
		//return the item
		return item;
		}
    
    // remove and return the item from the end
    public Item removeLast()  {
		
		/* check if there is an element which can be deleted; 
		 * otherwise throw exception */
		
		if (isEmpty ()) {
			 throw new NoSuchElementException ();
			}
		
		//save the item
		Item item = last.item;
		
		//remove the last element in deque
		if (capacity > 1) {
			Node temp=last.prev;
			last = null;
			last=temp;
			temp=null;
			last.next=null;
			}
			else {
				first=null;
				last=null;
				}
		
		//decrease the capacity
		capacity--;
		
		//return the item
		return item;
		}               
    
	
	private class DequeIterator implements Iterator <Item> {
		private Node current = first;
		
		// check whether we have an element to continue the iteration process
		public boolean hasNext () { return current!=null; }
		
		// remove the element, unsupported
		public void remove () { throw new UnsupportedOperationException (); }
		
		// iteration over the collection
		public Item next () {
			
			if (!hasNext()) { throw new NoSuchElementException (); }
			// save the item, iterate to the next item
			Item item = current.item;
			current = current.next;
			
			// return the item
			return item;
			}
		} 
	
	
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator () {
		return  new DequeIterator ();
		}
	
    // unit testing (optional)
    public static void main(String[] args) {
		 Deque <Integer> deque = new Deque<Integer>();
         deque.addFirst(1);
         deque.addLast(2);
         deque.removeFirst();
         deque.removeFirst();  
         deque.addLast(4);
         deque.addFirst(5);
         deque.removeLast();
         
         Iterator <Integer> it = deque.iterator();
         while (it.hasNext()) {
			  StdOut.println (it.next());
			 }    
		}  
   
}
