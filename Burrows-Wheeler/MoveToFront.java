import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class MoveToFront {
	private static int R = 256; // ASCII radix
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {	
		// ASCII char array
		char [] charArray = new char [256];	
		for (char c = 0; c < R; c++)
			charArray[c] = c;
		while (!BinaryStdIn.isEmpty())
		{
			char currentChar = BinaryStdIn.readChar();
			int i = -1;
			// find the needed character
			while (charArray[++i] != currentChar);
			BinaryStdOut.write (i, 8);
			for (int j = i-1; j >= 0; j--)
				charArray[j+1] = charArray [j];
			charArray[0] = currentChar;
		}
		BinaryStdOut.close ();
	}

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
		// ASCII char array
		char [] charArray = new char [256];	
		for (char c = 0; c < R; c++)
			charArray[c] = c;
		
		
		while (!BinaryStdIn.isEmpty())
		{
			int currentIndex = BinaryStdIn.readInt(8);
			char currentChar = charArray[currentIndex];
			BinaryStdOut.write (currentChar, 8);
			for (int j = currentIndex-1; j >= 0; j--)
				charArray[j+1] = charArray [j];
			charArray[0] = currentChar;
		}
		BinaryStdOut.close ();
	}
	
    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args)
    {
		if (args[0].compareTo("-") == 0)	MoveToFront.encode();
		if (args[0].compareTo("+") == 0)	MoveToFront.decode();
	}
}
