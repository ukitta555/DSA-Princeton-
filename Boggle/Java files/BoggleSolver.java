import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;

public class BoggleSolver
{
    private final Trie26Dictionary trieDictionary = new Trie26Dictionary();
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
       // Stopwatch stopwatch = new Stopwatch();
        for (String word : dictionary)
            trieDictionary.add(word);
      //  System.out.print (stopwatch.elapsedTime());
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        int rows = board.rows();
        int cols = board.cols();
        char [][] boggleBoard = new char [rows][cols];
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                char letter = board.getLetter(i, j);
                boggleBoard[i][j] = letter;
            }
        }

        // find all words
        trieDictionary.findAllWordsDFS(boggleBoard, rows, cols);
        // return them as an TrieSET (Iterable <String>)
        Iterable<String> result =  trieDictionary.getAllWords();
        // reset solver
        trieDictionary.resetSolverForNewBoard();
        return result;
    }



    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if (trieDictionary.contains(word))
        {
            trieDictionary.resetSolverForNewBoard();
            int length = word.length();
            if (length == 3 || length == 4) return 1;
            else if (length == 5) return 2;
            else if (length == 6) return 3;
            else if (length == 7) return 5;
            else if (length >= 8) return 11;
        }
        return 0;
    }

    public static void main(String[] args)
    {
        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        /* check whether the list of strings from the file is formed corrctly
        for (int i = 0; i < dictionary.length; i++)
            System.out.print (dictionary[i]+"\n");
        */
        // create a new solver + a new board from file
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-points777.txt");


        // test to check scoreOf()
        int counter = 0;
        for (String word : solver.getAllValidWords(board))
            counter += solver.scoreOf (word);
        System.out.println ("Score for the board: " + counter);


        board = new BoggleBoard("board-points750.txt");

        // get all valid words
        for (String word : solver.getAllValidWords(board))
            System.out.println (word);

        Stopwatch stopwatch = new Stopwatch();
        // test to check scoreOf()
        counter = 0;
        for (String word : solver.getAllValidWords(board))
            counter += solver.scoreOf (word);
        System.out.println ("Score for the board: " + counter);
        System.out.println (stopwatch.elapsedTime());
    }
}
