package dev.anto6314.project;

public class Board extends Connect4Board implements VerticalBitBoard {
    // constants
    private final static int WIDTH = 7;
    private final static int HEIGHT = 6;
    private final static int HEIGHTWITHBUFFER = HEIGHT+1;
    private static long BOTTOM;
    private static long TOP;
    // move storage and height pointers
    private int[] move = new int[200];
    private int moves = 0;
    public int getWrapLength() {
        return HEIGHT+1;
    }
    static {
        for (int i = 0; i < WIDTH; i++) {
            BOTTOM |= 1L<<i*HEIGHTWITHBUFFER;
        }
        TOP = BOTTOM<<HEIGHT;
    }
    private long board[] = new long[3];
    private byte height[] = new byte[WIDTH]; // height is a pointer to the free bit space for given column

    Board() {
        reset();
    }

    // Resets the board
    public void reset() {
        for (int i = 0; i < WIDTH; i++)
            height[i] = (byte)((HEIGHTWITHBUFFER)*i);
        for (int i = 0; i < board.length; i++)
            board[i]=0;
        moves = 0;
        super.reset();
    }
    // Returns number of free spaces on board
    public int freeSpaces() {
        int free = 0;
        for (int i = 0; i < WIDTH; i++) {
            if (drop(i)) {
                undo();
                free++;
            }
        }
        return free;
    }
    // returns number of moves since start of game
    public int moves() {
        return moves;
    }
    // drops a piece and returns whether drop was successful
    public boolean drop (int column) {
        if (turn == EMPTY)
            return false;
        board[turn]|=1L<<height[column]++;
        if ((TOP & board[turn]) == 0) {
            move[moves++]=column;
            return true;
        }
        board[turn]^=1L<<--height[column];
        return false;
    }
    // undoes last drop
    public boolean undo() {
        if (moves == 0)
            return false;
        long q =1L<<--height[move[--moves]];
        board[BLACK]&= ~q;
        board[RED]&= ~q;
        return true;
    }
    // check if current player is a winner
    public boolean isWinner() {
        return boardIsWinner(board[turn]);
    }
    // check for connect 4's in a board.
    static boolean boardIsWinner(long board) {
        long displacementBoard;
        // check verticals
        displacementBoard = board&board>>1;
        if ((displacementBoard&displacementBoard>>2)!=0)
            return true;

        // check horizontals
        displacementBoard = board&board>>HEIGHTWITHBUFFER;
        if ((displacementBoard&displacementBoard>>2*HEIGHTWITHBUFFER)!=0)
            return true;

        // check left-diagonals
        displacementBoard = board&board>>HEIGHT;
        if ((displacementBoard&displacementBoard>>2*HEIGHT)!=0)
            return true;

        // check right-diagonals
        displacementBoard = board&board>>HEIGHTWITHBUFFER+1;
        if ((displacementBoard&displacementBoard>>2*(HEIGHTWITHBUFFER+1))!=0)
            return true;

        return false;
    }
    // return the board as a byte array
    public byte[][] getBoard() {
        byte[][] output = new byte[WIDTH][HEIGHT];
        for (byte color = BLACK; color <= RED; color++) {
            for (int i = 0; i < WIDTH*HEIGHTWITHBUFFER; i++) {
                if (i%HEIGHTWITHBUFFER!=HEIGHT&&(board[color]>>i&1)==1)
                    output[i/HEIGHTWITHBUFFER][i%HEIGHTWITHBUFFER] = color;
            }
        }
        return output;
    }
    public String name() {
        return "Connect4 Antonio";
    }
    public String getComponentDetails() {
        return name() + " [v1.0]\n";
    }
    // returns the bitboard for a player
    public long getBitBoard(byte turn) {
        return board[turn];
    }
}