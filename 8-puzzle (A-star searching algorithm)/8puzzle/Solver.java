import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
public class Solver {
	
	private boolean solveable; // flag for isSolvable() method;
	private int requiredMoves = 0; // for moves() method
	private ArrayList <Board> path; 
	
	// search node
	private class SearchNode {
		private Board currentBoard;
		private SearchNode previousBoard;
		private int moves;
		private int manhattan;
		private int hamming;
		
		public SearchNode (Board currentBoard, SearchNode previousBoard, int moves) {
			this.currentBoard = currentBoard;
			this.previousBoard = previousBoard;
			this.moves = moves;
			this.manhattan = currentBoard.manhattan ();
			this.hamming = currentBoard.hamming ();
			}
		
		//manhattan comparator
		private class ManhattanComparator implements Comparator <SearchNode> {
			public int compare (SearchNode one, SearchNode two) {
				int priority1 = one.manhattan + one.moves;
				int priority2 = two.manhattan + two.moves;
				return priority1-priority2; 
				} 
			}
		
		//hamming comparator
		private class HammingComparator implements Comparator <SearchNode> {
			public int compare (SearchNode one, SearchNode two) {
				int priority1 = one.hamming + one.moves;
				int priority2 = two.hamming + two.moves;
				return priority1-priority2; 
				} 
			}
		
		// create a new manhattan-based comparator
		private Comparator <SearchNode>  comparatorManhattan () {
			return new ManhattanComparator ();
			}
		
		// create a new hamming-based comparator
		private Comparator <SearchNode>  comparatorHamming () {
			return new HammingComparator ();
			}
	
		}
	
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
		if (initial == null) throw new IllegalArgumentException ();
		SearchNode min = new SearchNode (initial, null, 0); // initial board
		SearchNode minTwin = new SearchNode (initial.twin(), null, 0); // twin board
		// create a new comparator
		Comparator <SearchNode> manhattan = min.comparatorManhattan ();
		// initialize the priotity queue
		MinPQ <SearchNode> pq1 = new MinPQ <>(manhattan);
		MinPQ <SearchNode> pq2 = new MinPQ <>(manhattan);
		// insert initial board
		pq1.insert (min);
		pq2.insert (minTwin);
		// main loop (works until the goal board is found)
		while (!min.currentBoard.isGoal() && !minTwin.currentBoard.isGoal()) {
		//while (!min.currentBoard.isGoal()) {
			// save minimum
			min = pq1.min ();
			minTwin = pq2.min();
			// remove it from the priority queue
			pq1.delMin ();
			pq2.delMin ();
			// create neighbours for the minimum
			Iterable <Board> neighbors = min.currentBoard.neighbors ();
			Iterable <Board> neighborsTwin = minTwin.currentBoard.neighbors ();
			// for each neighbour:
			for (Board board : neighbors) {
				// check whether this board already was in PQ
				if (min.previousBoard == null || !board.equals (min.previousBoard.currentBoard) ) {
					// if it is fine, insert in the priority queue
					SearchNode tmp = new SearchNode (board, min, min.moves+1);
					pq1.insert (tmp);
					}
				}
				
			for (Board board : neighborsTwin) {
				// check whether this board was already in PQ
				if (minTwin.previousBoard == null || !board.equals (minTwin.previousBoard.currentBoard)) {
					// if it is fine, insert in the priority queue
					SearchNode tmp = new SearchNode (board, minTwin, minTwin.moves+1);
					pq2.insert (tmp);
					}
				}
			}
		if (min.currentBoard.isGoal()) {
			requiredMoves = min.moves;
			path = new ArrayList <> ();
			SearchNode currentStep = min;
			while (currentStep != null) {
				path.add (currentStep.currentBoard);
				currentStep = currentStep.previousBoard;
				}
			Collections.reverse (path);
			solveable = true;
			}
			else {
				requiredMoves = minTwin.moves;
				solveable = false;
				}
		}

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
		return solveable;
		}

    // min number of moves to solve initial board
    public int moves() {
		if (isSolvable()) return requiredMoves;
			else return -1; 
		}

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
		if (isSolvable ()) return path;
			else return null;
		}
		

    // test client (see below) 
    public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] tiles = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				tiles[i][j] = in.readInt();
		Board initial = new Board(tiles);	
		// solve the puzzle
		Solver solver = new Solver(initial);
	
		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
			else {
				StdOut.println("Minimum number of moves = " + solver.moves());
				for (Board board : solver.solution())
					StdOut.println(board);
				}
	}
}
