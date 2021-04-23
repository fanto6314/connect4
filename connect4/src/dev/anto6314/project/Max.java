package dev.anto6314.project;

import java.util.ArrayList;
import java.util.Random;

public class Max extends Connect4Player {
    // create warp constants and even/odd masks
    static final int WRAP = 7;
    static final int WIDTH = 7;
    static final int HEIGHT = WRAP-1;
    static final long ODDMASK;
    static final long EVENMASK;
    static {
        long oddMaskTemp = 0;
        long evenMaskTemp = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WRAP; j++) {
                if (j%2==0) {
                    if (j< WRAP-1)
                        oddMaskTemp |= 1L<<(WRAP*i+j);
                } else {
                    evenMaskTemp |= 1L<<(WRAP*i+j);
                }
                if (j < WRAP-1) {

                }
            }
        }
        ODDMASK = oddMaskTemp;
        EVENMASK = evenMaskTemp;
    }
    // name
    public String shortName() {
        return "Computer";
    }
    public String fullName() {
        return "AI Controller";
    }
    // difficulty options
    public static DropDownItem[] getDifficultySettings()  {
        DropDownItem[] settings = new DropDownItem[3];
        settings[0]=new DropDownItem(1,"Easy");
        settings[1]=new DropDownItem(2,"Medio");
        settings[2]=new DropDownItem(0,"Difficile");
        return settings;
    }
    // add threats for each wrap using bitwise algorithms
    private static long addThreats(long board, long spacemap, int wrap) {
        long threatSignature = board&board>>wrap; // wraps board over to detect 2 in a rows
        long threatMap;
        // Basic 3-way threat detection engine
        long threeRowThreat = threatSignature&threatSignature>>wrap; // wraps 2 in arows further right to detect 3 in a rows
        threatMap = threeRowThreat<<wrap*3|threeRowThreat>>wrap; // add blank spaces next to three in a rows as threats
        // Separate state-class signature detection
        threatMap |= (threatSignature>>2*wrap&board)<<wrap; // check for skip-pieces to detect piece-piece-blank-piece threats
        threatMap |= (threatSignature<<3*wrap&board)>>wrap; // checks for piece-blank-piece-piece threats
        return threatMap&spacemap; // merges threats with empty spaces
    }
    // score a board
    private static int evaluate(Connect4Board evalBoard) {

        long board[] = new long[3];
        if (evalBoard instanceof VerticalBitBoard) {
            VerticalBitBoard castBoard = (VerticalBitBoard) evalBoard;
            board[1] = castBoard.getBitBoard((byte)1);
            board[2] = castBoard.getBitBoard((byte)2);
        } else {
            // convert non-bitboards into bitboards
            byte[][] byteBoard = evalBoard.getBoard();
            int i = 0;
            for (int y = 0; y < byteBoard[0].length; y++) {
                for (int x = 0; x < byteBoard.length; x++) {
                    if (byteBoard[x][y]!=0) {
                        board[byteBoard[x][y]] = 1 << i;
                    }
                }
                i++;
            }
        }
        board[0] = ~(board[1]|board[2]);
        long threatMap[] = new long[3];
        for (int i = Connect4Board.BLACK; i <= Connect4Board.RED; i++) {
            threatMap[i]=0;
            threatMap[i]|=addThreats(board[i],board[0],1);
            threatMap[i]|=addThreats(board[i],board[0],WRAP);
            threatMap[i]|=addThreats(board[i],board[0],WRAP+1);
            threatMap[i]|=addThreats(board[i],board[0],WRAP-1);
        }
        return (2 * Long.bitCount(ODDMASK&threatMap[1]) + Long.bitCount(EVENMASK&threatMap[1]))-(2 * Long.bitCount(EVENMASK&threatMap[2]) + Long.bitCount(ODDMASK&threatMap[2]));
    }
    int depth = 12;
    public void doAutomation() {
        if (depth<12) {
            depth = 12;
        }
        if (getDifficulty() == 1) {
            depth = 4;
        }
        if (getDifficulty() == 2) {
            depth = 10;
        }
        // prepare to start
        displayMessage("AI is thinking...");
        Connect4Board board = workingBoard();
        long startTime = System.currentTimeMillis();
        byte originalTurn = workingBoard().getTurn();
        int[] score = new int[WIDTH];
        int topscore = 0;
        //cutoff
        if (board.getTurn()==Connect4Board.BLACK){
            topscore = -10000000;
        }
        if (board.getTurn()==Connect4Board.RED) {
            topscore=10000000;
        }
        boolean[] eligible = new boolean[WIDTH]; //eligibility array for whehter or not move is legal
        for (int i = 0; i < WIDTH; i++) {
            if (board.drop(i)) {
                // run minimax
                if (board.getTurn()==Connect4Board.BLACK)
                    score[i]=min(board,depth-1,topscore-1);
                if (board.getTurn()==Connect4Board.RED)
                    score[i]=max(board,depth-1,topscore+1);
                topscore=score[i];
                board.undo();
                eligible[i] = true;
            } else
                eligible[i] = false;

        }
        // the best score guaranteed in depth moves
        int bestGuaranteedScore = 0;
        // get list of moves that are equally good
        ArrayList tiedMoves = new ArrayList();
        if (board.getTurn()==Connect4Board.BLACK) {
            int bestScore = -100000;
            for (int i = 0; i < WIDTH; i++) {
                if (score[i] > bestScore & eligible[i]) {
                    tiedMoves.clear();
                    tiedMoves.add(new Integer(i));
                    bestScore = score[i];
                    bestGuaranteedScore = score[i];
                }
                if (score[i] == bestScore & eligible[i]) {
                    tiedMoves.add(new Integer(i));
                }
            }
        }
        if (board.getTurn()==Connect4Board.RED) {
            int bestScore = 100000;
            for (int i = 0; i < WIDTH; i++) {
                if (score[i] < bestScore & eligible[i]) {
                    tiedMoves.clear();
                    tiedMoves.add(new Integer(i));
                    bestScore = score[i];
                    bestGuaranteedScore = score[i];
                }
                if (score[i] == bestScore & eligible[i]) {
                    tiedMoves.add(new Integer(i));
                }
            }
        }
        // move randomizer (to add unpredictability)
        Random  rand = new Random();
        int bestSlot = ((Integer)tiedMoves.get(((int)(tiedMoves.size()*rand.nextDouble())))).intValue();
        int deathScore = 0;

        if (board.getTurn()==Connect4Board.BLACK&&bestGuaranteedScore>=100 || board.getTurn()==Connect4Board.RED&&bestGuaranteedScore<=-100) {
            if (board.getTurn()==Connect4Board.BLACK) {
                deathScore = depth - (bestGuaranteedScore-200)/10-2;
            }
            if (board.getTurn()==Connect4Board.RED) {
                deathScore = depth + (bestGuaranteedScore+200)/10-2;
            }
        }
        workingBoard().drop(bestSlot);
        workingBoard().setTurn(originalTurn);
        long runTime = (System.currentTimeMillis()-startTime)/(bestSlot+1);
        // depth adjuster
        if (runTime>3000L) {
            depth--;
        }

        if (runTime < 330L) {
            depth += 2;
        }
        relinquishControl();
    }
    //minimum node
    private static int min(Connect4Board board, int placeDepth, int minSearchLimit) {
        board.setTurn(Connect4Board.RED);
        if (board.isWinner()){
            board.setTurn(Connect4Board.BLACK);
            // killmove
            return -200-10*placeDepth;
        }
        if (placeDepth <= 0)
        {
            board.setTurn(Connect4Board.BLACK);
            return evaluate(board);
        }
        int best = 11848;
        for (int i = 0; i < WIDTH; i++) {
            if (board.drop(i)) {
                best = Math.min(best, max(board,placeDepth-1,best));
                board.undo();
            }
            //cutoff
            if (best<=minSearchLimit) {
                board.setTurn(Connect4Board.BLACK);
                if (best == 11848) {
                    return 0;
                }
                return best;
            }
        }
        board.setTurn(Connect4Board.BLACK);
        if (best == 11848) {
            return 0;
        }

        return best;
    }
    // maximum node
    private static int max(Connect4Board board, int placeDepth, int maxSearchLimit) {
        board.setTurn(Connect4Board.BLACK);
        if (board.isWinner()){
            board.setTurn(Connect4Board.RED);
            //killmove
            return 200+10*placeDepth;
        }
        if (placeDepth <= 0) {
            board.setTurn(Connect4Board.RED);
            return evaluate(board);
        }
        int best = -11848;
        for (int i = 0; i < WIDTH; i++) {
            if (board.drop(i)) {
                best = Math.max(best, min(board,placeDepth-1,best));
                board.undo();
            }
            //cutoff
            if (best>=maxSearchLimit) {
                board.setTurn(Connect4Board.RED);
                if (best == -11848) {
                    return 0;
                }
                return best;
            }
        }
        board.setTurn(Connect4Board.RED);
        if (best == -11848) {
            return 0;
        }

        return best;
    }
}