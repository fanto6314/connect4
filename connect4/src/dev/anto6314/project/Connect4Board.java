package dev.anto6314.project;

public abstract class Connect4Board  {
    // constants and turn markers
    public final static byte EMPTY = 0;
    public final static byte BLACK = 1;
    public final static byte RED = 2;
    byte turn = 0;
    public abstract boolean drop(int column);
    // piece names
    private String[] pieceNames = {"Empty", "Black", "Red"};
    // set custom piece names
    public void setPieceNames(String[] names) {
        pieceNames = names;
    }
    // returns the piece names for a piece byte
    public String getPieceName(int piece) {
        return pieceNames[piece];
    }
    // undoes last move and returns true on success
    public abstract boolean undo();
    // checks if turn holder is a winner
    public abstract boolean isWinner();
    // sets the turn
    public void setTurn(byte newTurn) {
        turn = newTurn;
    }
    // checks who's turn it is
    public byte getTurn() {
        return turn;
    }
    // returns the board as a byte array
    public abstract byte[][] getBoard();
    // resets the board
    public void reset() {
        turn = 0;
    }
    // converts the board to a string representation (debug)
    public String toStringBoard() {
        byte[][] board = getBoard();
        StringBuilder boardString = new StringBuilder();
        for (int y = board[0].length-1; y >=0 ; y--) {
            for (byte[] bytes : board) {
                switch (bytes[y]) {
                    case BLACK -> boardString.append("X");
                    case RED -> boardString.append("O");
                    default -> boardString.append(".");
                }
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }
    // returns the name of the board for diagnostics
    public String name() {
        return "Board system";
    }
    // returns details of the board
    public String getComponentDetails() {
        return name();
    }
    // returns details of the board
    public String toString() {
        return getComponentDetails();
    }
    // returns the number of free spaces on the board
    public int freeSpaces() {
        return -1;
    }
    // returns the number of moves
    public abstract int moves();

}