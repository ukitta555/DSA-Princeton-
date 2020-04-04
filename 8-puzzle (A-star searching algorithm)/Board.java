import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {

	private final int [][] board;
	private final int size;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
		this.size = tiles[0].length;
		this.board = new int [this.size][this.size];
		// generating the board from passed argument
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.board [i][j] =  tiles[i][j];
				}
			}
		}  // works
                                           
    // string representation of this board
    public String toString() {
		String s = "";
		s=s+size+"\n";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				s=s+ Integer.toString(board[i][j]) + " ";
				}
			s=s+"\n";
			}
		return s; 
		}     // works

    // board dimension n
    public int dimension() {
		return size;
		} // works

    // number of tiles out of place
    public int hamming() {
		int distance = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board [i][j] != (i*size)+j+1 && board [i][j] != 0) {
					distance++;
					}
				}
			}
		return distance;
		} // works

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
		int distance = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				 distance+=calculateManhattan (i, j, board [i][j]);
				 //System.out.print (calculateManhattan (i, j, board [i][j])+ " ");
				}
			}
		return distance;
		} // works
	
	// helper method to calculate manh. distance for selected tile
	private int calculateManhattan (int i, int j, int tile) {
		int xPos;
		int yPos;
		if (tile != 0) {
			if (tile % size != 0) {
				yPos = tile / size;
				xPos = (tile % size)-1;
				}
				else {
					yPos = (tile / size)-1;
					xPos = size-1;
					}
			return Math.abs (yPos-i) + Math.abs (xPos-j);
			}
		return 0;
		} // works
	

    // is this board the goal board?
    public boolean isGoal() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board [i][j] != 0) {
					if (board [i][j] != (i * size) + j + 1) {
						return false;
						}
					}
				}
			}
		return true;
		} // works
	
	@Override
    // does this board equal y?
    public boolean equals(Object y) {
		
		if (this == y) return true; // shallow comparison
		// check whether y = null or it isn't the class we want it to be
		if (y == null || y.getClass () != this.getClass ()) 
			return false;
		// casting y into Board
		Board that = (Board) y; 
		/* "deep" comparison 
		 * (since the only public method is toString(), we define equality
		 * by this method)
		*/
		
		if (this.dimension() == that.dimension ()) {
		
		boolean f = true;
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (this.board [i][j] != that.board [i][j]) {
					f=false;
					} 
			 	}
			}
	
			if (f)  
				return true;

			}
			return false;
		}  // works (?)

	
	//helper method, creates a new board for neighbors () method 
	private Board createNeighbor (int iPos, int jPos, int changeI, int changeJ) {
		int [][] neighborMatrix = new int [size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				neighborMatrix [i][j] = board [i][j];
			 	}
			}
		
		// swap to create a neighbour
		int swap = neighborMatrix [iPos][jPos];
		neighborMatrix [iPos][jPos] = neighborMatrix [iPos + changeI][jPos + changeJ];
		neighborMatrix [iPos + changeI][jPos + changeJ] = swap;
		
		Board newBoard = new Board (neighborMatrix);
		return newBoard;
		}

    // all neighboring boards
    public Iterable<Board> neighbors() {
		boolean f = false;
		int iPos = -1;
		int jPos = -1;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board [i][j] == 0) {
					f = true;
					iPos=i;
					jPos=j;
					break;
					}
				}
			if (f) {
				break;
				}
			}
		
		Board [] neighbourBoards  = new Board [4]; 
		int index = 0;
		
		if (iPos != 0) {
			Board tmp = createNeighbor (iPos, jPos, -1, 0);
			neighbourBoards [index++] = tmp;
			}
		
		if (jPos != 0) {
			Board tmp = createNeighbor (iPos, jPos, 0, -1);
			neighbourBoards [index++] = tmp;
			}
		
		if (iPos != size-1) {
			Board tmp = createNeighbor (iPos, jPos, 1, 0);
			neighbourBoards [index++] = tmp;
			}
		
		if (jPos != size-1) {
			Board tmp = createNeighbor (iPos, jPos, 0, 1);
			neighbourBoards [index++] = tmp;
			}
			
		Iterable <Board> bc = new BoardCollection <> (neighbourBoards,index);
		return bc;
		} //works
	
		
	// class for collection of boards
	private class BoardCollection <Board> implements Iterable <Board> {
		private Board [] boards;
		private int size;
		public BoardCollection (Board [] boards, int size) {
			this.boards = boards;
			this.size = size;
			}
			
		// class for iterator for the collection of boards
		private class BoardCollectionIterator implements Iterator <Board> {
			private int size;
			private int index=0;
			private Board [] boards;
			
			public BoardCollectionIterator (Board [] boards, int size) {
				this.size = size;
				this.boards = boards;
				}
			
			public boolean hasNext () {
				if (index == size || boards [index] == null)  {
					return false;
					}
				return true;
				}
				
			public Board next () {
				if (!hasNext ()) throw new NoSuchElementException ();
					else {
						Board item = boards [index];
						index++;
						return item;
						}
				}
				
			public void remove () {
				 throw new UnsupportedOperationException ();
				}
			}
			
		public Iterator <Board> iterator () {
			 return new BoardCollectionIterator (boards, size);
			}
		}

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
		int [][] twinMatrix = new int [size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				twinMatrix [i][j] = board [i][j];
				}
			}
		if (twinMatrix [0][0] != 0 && twinMatrix [0][1] != 0) {
			int swap = twinMatrix [0][0];
			twinMatrix [0][0] = twinMatrix [0][1];
			twinMatrix [0][1] = swap;
			}
			else {
				int swap = twinMatrix [1][1];
				twinMatrix [1][1] = twinMatrix [1][0];
				twinMatrix [1][0] = swap;
				}
		Board twin = new Board (twinMatrix);
		return twin;
		} // works

    // unit testing (not graded)
    public static void main(String[] args)  {
		int [][] board = new int [3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				 if (i != 3 || j != 3)
					board [i][j] = (i*3)+j+1;
				}
			}
		board [2][2]=0;
		
		

		int [][] board1 = new int [3][3];
		
		board1 [0][0] = 2; 
		board1 [0][1] = 1;
		board1 [0][2] = 3;
		board1 [1][0] = 5; 
		board1 [1][1] = 6;
		board1 [1][2] = 4;
		board1 [2][0] = 7; 
		board1 [2][1] = 0;
		board1 [2][2] = 8;  
		

		
		/* 
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				 if (i != 3 || j != 3)
					board1 [i][j] = (i*3)+j+1;
				}
			}
		board1 [2][2]=4;
		*/
		
		Board b = new Board (board);
		Board b1= new Board (board1);
		
		System.out.print (b1);
		//isGoal() unittest 
		System.out.println ("isGoal() test:");
		System.out.println (b.isGoal());
		//equals() unittest
		System.out.println ("equals() test:");
		System.out.println (b.equals(b1));
		//hamming() unittest
		System.out.println ("hamming() test:");
		System.out.println (b1.hamming());
		//manhattan() unittest
		System.out.println ("manhattan() test:");
		System.out.println (b1.manhattan());
		//twin() unittest
		System.out.println ("twin() test:");
		System.out.println (b1.twin());
		//neighbors() unittest
		System.out.println ("Neighbours test:");
		Iterable <Board> bc =  b1.neighbors();
		for (Board b123 : bc) {
			System.out.println (b123);
			}
		}
		
}
