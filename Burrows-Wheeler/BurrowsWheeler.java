import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler 
{
	private static final int R = 256;
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform()
    {
		String s = BinaryStdIn.readString();
		CircularSuffixArray arrayOfSuffixes = new CircularSuffixArray(s);
		
		int suffixIndex;
		
		for (int i = 0; i < s.length(); i++)
		{
			suffixIndex = arrayOfSuffixes.index(i);
			if (suffixIndex == 0) BinaryStdOut.write (i, 32);
		}
		
		for (int i = 0; i < s.length(); i++)
		{
			suffixIndex = arrayOfSuffixes.index(i);
			int charIndex = (suffixIndex-1 < 0) ? s.length()-1 : suffixIndex-1;
			BinaryStdOut.write (s.charAt(charIndex));
		}
			
		BinaryStdOut.close();
	}

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform()
    {
		int first = BinaryStdIn.readInt();
		// ASCII radix = 256
		// assocNext[] array for reconstructing next[] array (see assignment page for more info) 
		int[] assocNext = new int [R+1];
		String str = BinaryStdIn.readString();
		int messageLength = str.length();
		/*
		// assocSort[] array to sort chars (key-indexed counting algorithm)
		int[] assocSort = new int [R+1];
		
		* */
		for (int i = 0; i < messageLength; i++)
		{
			assocNext[str.charAt(i)+1]++;
		//	assocSort[input+1]++;
		}
		// calculate cumulatives
		for (int i = 1; i < R+1; i++)
		{
			assocNext[i] += assocNext[i-1];
		//	assocSort[i] += assocSort[i-1];
		}
		
		
		
		// sorted chars array
		char[] sortedChars = new char [messageLength];
		// next[] array is used to decode the message 
		int[] next = new int[messageLength];
		char c;
		// current char index in message string 
		int encodedMessageCharIndex = 0;
		int assocValue;
		// use key-indexed counting algorithm to construct next[] array
		for (int i = 0; i < messageLength; i++)
		{
			c = str.charAt(i);
			sortedChars[assocNext[c]] = c;
			next[assocNext[c]++] = encodedMessageCharIndex; 
			encodedMessageCharIndex++;
		}
		
		StringBuilder message = new StringBuilder();
		for (int i = 0; i < messageLength; i++)
		{
			message.append (sortedChars[first]);
			first = next[first];
		}
		BinaryStdOut.write(message.toString());
		BinaryStdOut.close();
		/*
		for (int i = 0; i < encodedMessageCharIndex; i++)
		{
			System.out.println (sortedChars[i]);
		}*/
	}

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args)
    {
		if (args[0].compareTo("-") == 0) BurrowsWheeler.transform();
		if (args[0].compareTo("+") == 0) BurrowsWheeler.inverseTransform();
	}
}
