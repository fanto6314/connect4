package dev.anto6314.project;

public interface VerticalBitBoard {
    public long getBitBoard(byte turn); //get the board in binary
    public int getWrapLength(); // get the binary board wrap lengths
}