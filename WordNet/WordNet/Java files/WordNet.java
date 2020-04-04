import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.SET;

public class WordNet {
	private final ST <String, SET <Integer>> symbolTable;
	private final String [] synsetArray;
	private final SAP sap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
		if (synsets == null || hypernyms == null) throw new IllegalArgumentException ();
		In in_1 = new In (synsets);
		In in_2 = new In (hypernyms);
		// reading and processing data from synsets.txt 
		String [] fileInput = in_1.readAllLines();
		this.synsetArray = new String [fileInput.length];
		symbolTable = new ST <> ();
		int counter = 0;
		for (String synset : fileInput)
		{
			String [] tokens = synset.split (","); // split line in tokens
			synsetArray [counter++] = tokens [1]; // save the synset to array
			String [] words = tokens[1].split (" "); // split the synset in words 
			for (int i = 0; i < words.length; i++)
			{
				// if there is a word already in the ST, then add the number of node to its set
				if (symbolTable.contains (words[i]))
				{
					SET <Integer> tmpSet = symbolTable.get (words[i]);
					tmpSet.add (Integer.parseInt (tokens[0]));
				}
				else // create a new set, add the node to it, put it to the ST
				{
					SET <Integer> tmpSet = new SET <> ();
					tmpSet.add (Integer.parseInt (tokens[0]));
					symbolTable.put (words [i], tmpSet);
				}
				
			}
		}
		
		
		// create a Digraph
		Digraph G = new Digraph (fileInput.length);
		// reading and processing data from hypernyms.txt
		fileInput = in_2.readAllLines();
		for (String hypernym : fileInput) 
		{
			String [] tokens = hypernym.split(",");
			for (int i = 1; i < tokens.length; i++)
			{
				// add an edge to the graph (current_word -> hypernym) 
				G.addEdge (Integer.parseInt (tokens[0]), Integer.parseInt (tokens[i]));
			}
		}
		// check the input graph for cycles
		DirectedCycle dc = new DirectedCycle (G);
		// if it has one, then the input is incorrect
		if (dc.hasCycle()) throw new IllegalArgumentException ();
		int countOfRoots = 0;
		for (int i = 0; i < G.V(); i++)
		{
			if (G.outdegree (i) == 0)
			{
				countOfRoots++;
			}
		}
		if (countOfRoots != 1) throw new IllegalArgumentException ();
		//initialize SAP for the graph
		this.sap = new SAP (G);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns()
    {
		return symbolTable.keys();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word)
	{
		if (word == null) throw new IllegalArgumentException ();
		return symbolTable.contains(word);
	}
 
	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) 
	{
		if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun (nounB)) throw new IllegalArgumentException ();
		return sap.length (symbolTable.get (nounA),  symbolTable.get (nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) 
    {
		if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun (nounB)) throw new IllegalArgumentException ();
		return synsetArray[sap.ancestor (symbolTable.get (nounA),  symbolTable.get (nounB))];
	}

	// do unit testing of this class
	public static void main(String[] args)
    {
		String synset = "synsets.txt";
		String hypernym = "hypernyms6InvalidCycle+Path.txt";
		WordNet wordNet = new WordNet (synset, hypernym);
		
	}
}
