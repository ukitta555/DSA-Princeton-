import edu.princeton.cs.algs4.Queue;

public class Trie26Dictionary
{
    private TrieNode root = null;
    private static final int R = 26;
    private Queue<String> wordsFromBoard;
    private Queue<TrieNode> nodesToReset;
    private Pair[][][] adjacentCubes;
    private char [][] boggleBoard;
    private boolean[][] visited;

    public Trie26Dictionary()
    {
        wordsFromBoard = new Queue<String>();
        nodesToReset = new Queue<TrieNode>();
    }
    private static class TrieNode
    {
        private int value = -1;
        private TrieNode[] next = new TrieNode [R];
    }

    private class Pair
    {
        private int i;
        private int j;
        public Pair(int i, int j)
        {
            this.i = i;
            this.j = j;
        }

        public String toString()
        {
            return "Pair{" +
                    "i=" + i +
                    ", j=" + j +
                    '}';
        }
    }

    public void add(String key)
    {
        root = add(root, key, 0);
    }

    private TrieNode add(TrieNode x, String key, int d)
    {
        if (x == null) x = new TrieNode();
        if (d == key.length())
        {
            x.value = 0;
            return x;
        }
        char c = key.charAt(d);
        x.next[c - 65] = add(x.next[c - 65], key, d+1);
        return x;
    }

    // we "delete" a key if get or contains was called at least once for this key
    // kinda "monkeyish", but it radically speeds up the BoggleSolver as we
    // don't add duplicates to the  wordsFromBoard (Queue).
    // Moreover, we can actually use Queue instead of ST or SET, and it
    // also speeds up the process of adding / retrieving stuff
    public boolean contains(String key)
    {
        return (get(key) == 1);
    }

    // value of a key = the number of times it was retrieved using get()
    // in order to avoid duplicates, we return false in contains() so that
    // we don't add a duplicate to the wordsFromBoard
    public int get(String key)
    {
        TrieNode x = get(root, key, 0);
        if (x == null || x.value == -1) return -1;
        x.value += 1;
        if (x.value == 1)  nodesToReset.enqueue(x);
        return x.value;
    }

    private TrieNode get(TrieNode x, String key, int d)
    {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 65], key, d+1);
    }

    // helper method to cut off rudimentary branches of DFS
    private boolean checkNextNode(TrieNode x, char nextChar)
    {
        if (nextChar != 'Q')
        {
            if (x != null && x.next[nextChar - 65] != null) return true;
            return false;
        }
        else
        {
            TrieNode qNode = x.next[nextChar - 65];
            TrieNode uNode = null;
            if (qNode != null) uNode = qNode.next[20];
            if (qNode != null && uNode != null) return true;
            return false;
        }

    }

    // run modified DFS to find all words
    public void findAllWordsDFS(char[][] boggleBoard, int rows, int cols)
    {
        // form a matrix that will indicate whether we visited the node or not
        // remove the mark from the node when we visited all 8 possible neighbour nodes
        this.boggleBoard = boggleBoard;
        visited = new boolean[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                visited[i][j] = false;
            }
        }

        computeAdjacentCubes();
        TrieNode currentNode = root;

       // Stopwatch watch = new Stopwatch();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                dfs(i, j, "", currentNode);
            }
        }
        //System.out.println("Elapsed time: " + watch.elapsedTime());
    }

    // modified recursive DFS itself
    private void dfs(int i, int j, String prefix, TrieNode currentNode)
    {
        // mark the visited node
        visited[i][j] = true;
        if (checkNextNode(currentNode, boggleBoard[i][j]))
        {

            if (boggleBoard[i][j] == 'Q')
            {
                prefix += "QU";
                currentNode = currentNode.next[boggleBoard[i][j] - 65];
                currentNode = currentNode.next[20];
            }
            else
            {
                currentNode = currentNode.next[boggleBoard[i][j] - 65];
                prefix +=  boggleBoard[i][j];
            }

            if (prefix.length() > 2 && contains(prefix))
            {
                wordsFromBoard.enqueue(prefix);
            }

            for (Pair pair : adjacentCubes[i][j])
            {
                if (pair != null)
                {
                    int nextI = pair.i;
                    int nextJ = pair.j;
                    if (!visited [nextI][nextJ])
                        dfs(nextI, nextJ, prefix, currentNode);
                }
            }
        }
        // uncheck the node
        visited[i][j] = false;
    }

    public Iterable<String> getAllWords() { return wordsFromBoard; }

    public void resetSolverForNewBoard()
    {
        wordsFromBoard = new Queue<String>();
        for (TrieNode x : nodesToReset)
            x.value = 0;
        nodesToReset = new Queue<TrieNode>();
    }

    // precomputes cubes that are adjacent to each other
    // in other words, computes adjacency list for Boggle board
    private void computeAdjacentCubes()
    {
        int rows = boggleBoard.length;
        int cols = boggleBoard[0].length;

        Pair indices;
        adjacentCubes =  new Pair[rows][cols][9];
        int k;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                k = 0;
                if (i >= 1)
                {
                    if (j >= 1)
                    {
                        indices = new Pair(i - 1, j - 1);
                        adjacentCubes[i][j][k] = indices;
                        k++;
                    }
                    indices = new Pair (i - 1, j);
                    adjacentCubes[i][j][k] = indices;
                    k++;
                    if (j < cols-1)
                    {
                        indices = new Pair (i - 1, j + 1);
                        adjacentCubes[i][j][k] = indices;
                        k++;
                    }
                }

                if (i < rows - 1)
                {
                    if (j >= 1)
                    {
                        indices = new Pair (i + 1, j - 1);
                        adjacentCubes[i][j][k] = indices;
                        k++;
                    }
                    indices = new Pair (i + 1, j);
                    adjacentCubes[i][j][k] = indices;
                    k++;
                    if (j < cols-1)
                    {
                        indices = new Pair(i + 1, j + 1);
                        adjacentCubes[i][j][k] = indices;
                        k++;
                    }
                }

                if (j >= 1)
                {
                    indices = new Pair(i, j - 1);
                    adjacentCubes[i][j][k] = indices;
                    k++;
                }
                if (j < cols-1)
                {
                    indices = new Pair(i, j + 1);
                    adjacentCubes[i][j][k] = indices;
                    k++;
                }
            }
        }
    }

    public static void main(String[] args) {
        Trie26Dictionary trie = new Trie26Dictionary ();
        trie.add("HEAD");
        trie.add("AMBULA");
        trie.add("AMB");
        System.out.println(trie.contains("HEAD"));
        System.out.println(trie.contains("HEAD"));
        System.out.println(trie.contains("HEADER"));
        System.out.println(trie.contains("XD"));

        System.out.println ('X'-1);
        for (char c = 0; c < 10; c++)
        {
            System.out.println (c+1);
        }

    }
}
