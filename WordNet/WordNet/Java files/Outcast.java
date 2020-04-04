import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public class Outcast {
	private final WordNet wordnet;
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) 
	{
		this.wordnet = wordnet;
	}
	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns)
	{
		int [] distance = new int [nouns.length];
		int maxDist = Integer.MIN_VALUE; 
		int maxIndex = -1;
		for (int i = 0; i < distance.length; i++)
		{
			distance [i] = 0;
		}
		
		for (int i = 0; i < nouns.length; i++)
		{
			for (int j = 0; j < nouns.length; j++)
			{
				distance [i] += wordnet.distance (nouns [i], nouns [j]); 
			}
			if (maxDist < distance[i])
			{
				maxDist = distance [i];
				maxIndex = i;
			}
		}
	return nouns [maxIndex];
	}   
	
	public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
}
}
