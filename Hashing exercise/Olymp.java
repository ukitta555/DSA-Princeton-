/*Hashing with wrong hashCode() or equals(). 
 * Suppose that you implement a data type OlympicAthlete for use in a 
 * java.util.HashMap.

Describe what happens if you override hashCode() but not equals().
* 
Describe what happens if you override equals() but not hashCode().
* 
Describe what happens if you override hashCode() but implement 
* public boolean equals(OlympicAthlete that)
*  instead of 
* public boolean equals(Object that)
*/

import java.util.HashMap;

public  class Olymp 
{
	private int speed;
	
	public Olymp (int speed) 
	{
	 this.speed = speed;
	}
	
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int hash = 17;
		hash = prime * hash + speed;
		return hash;
	}
	
	
	@Override
	public boolean equals (Object that) 
	{
		if (that == this) return true;
		if (that == null || that.getClass() != this.getClass ()) return false;
		
		Olymp newThat = (Olymp) that;
		if (newThat.speed == this.speed) return true;
		return false;
	}
	
	public static void main (String [] args)
	{
		HashMap <Olymp, Integer> h = new HashMap <> ();
		Olymp runner = new Olymp (12);
		Olymp runner1 = new Olymp (12);
		h.put (runner, 12);
		
		System.out.print (h.get (runner1));
	}
}

