package dev.anto6314.project;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.awt.event.*;

public class Connect4 implements ActionListener, MouseListener{
    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;
    public int NPlay = 0;
    public int NMoves = 1;
    public int NWin1 = 0;
    public int NWin2 = 0;
    public int NTie = 0;

    ArrayList playerInterfaces = new ArrayList();

    Connect4Board board;

    private static final Dimension dropDownPreferredSize = new Dimension(150,14);
    private JComboBox player1Drop;
    private JComboBox player1Difficulty;
    private JComboBox player2Drop;
    private JComboBox player2Difficulty;

    private JButton startButton;
    private JButton statsButton;

    private Connect4Player[] player;

    private JFrame frame;

    private PieceDisplay displayUnits[][];

    private JLabel stateLabel;
    private JFrame boardFrame;
    ImageIcon img = new ImageIcon("logo.PNG");

    Connect4(Class boardClass) {
        try {
            board = (Connect4Board) boardClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        stateLabel = new JLabel();
    }
    // show the program
    public void show() {
        buildGui();
    }
    // add a player to the program
    public void addPlayerInterface(Class newInterface) {
        if (newInterface.isInstance(Connect4Player.class));
        playerInterfaces.add(new ClassComboBoxDisplayWrapper(newInterface));
    }
    // build the GUI
    void buildGui() {
        System.out.println(Connect4.getComponentDetails());
        System.out.println("Loading...");
        frame = new JFrame ("Connect 4 [Antonio]");
        //layout
        GridBagLayout layout = new GridBagLayout();
        frame.setLayout(layout);
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill=GridBagConstraints.HORIZONTAL;
        constraint.anchor=GridBagConstraints.NORTHWEST;
        frame.setIconImage(img.getImage());
        frame.setSize(450,350);
        frame.getContentPane().setBackground(Color.WHITE);
        constraint.gridy=0;
        constraint.gridx=0;
        constraint.gridwidth=2;
        constraint.ipadx=4;
        constraint.ipady=8;
        // title and labels
        frame.add(new JLabel("<html><head></head><body><div style=\"text-align:left;font-family:'Segoe UI',Tahoma;font-weight:normal;font-size:1.3em;color:#003399;font-weight:normal\">Connect 4 [Antonio]</div></body></html>"),constraint);
        constraint.gridwidth=1;
        constraint.gridy=1;
        frame.add(new JLabel("<html><head></head><body><div style=\"font-family:'Segoe UI',Tahoma;font-weight:normal;font-size:1.05em;color:#003399;\">Player options</div></body></html>"),constraint);
        constraint.gridheight=1;
        constraint.gridx=0;
        constraint.gridwidth=1;
        constraint.gridy++;
        frame.add(new JLabel("<html><head></head><body><div style=\"font-family:'Segoe UI',Tahoma;font-weight:normal;font-size:1em;\">Player 1: </div></body></html>"),constraint);
        constraint.gridx++;
        // difficulty options and labels
        player1Drop = new JComboBox();
        player1Difficulty = new JComboBox();
        for(int i = 0; i < playerInterfaces.size();i++) {
            player1Drop.addItem(playerInterfaces.get(i));
        }
        player1Drop.setPreferredSize(dropDownPreferredSize);
        player1Drop.setActionCommand("player1dropmenu");
        player1Drop.addActionListener(this);
        player1Difficulty.setPreferredSize(dropDownPreferredSize);
        frame.add(player1Drop,constraint);
        constraint.gridx--;
        constraint.gridy++;
        frame.add(new JLabel("<html><head></head><body><div style=\"font-family:'Segoe UI',Tahoma;font-weight:normal;font-size:1em;\">Difficulty: </div></body></html>"),constraint);
        constraint.gridx++;
        frame.add(player1Difficulty,constraint);
        constraint.gridx--;
        constraint.gridy++;
        frame.add(new JLabel("<html><head></head><body><div style=\"font-family:'Segoe UI',Tahoma;font-weight:normal;font-size:1em;\">Player 2: </div></body></html>"),constraint);
        constraint.gridx++;
        player2Drop = new JComboBox();
        player2Difficulty = new JComboBox();
        for(int i = 0; i < playerInterfaces.size();i++) {
            player2Drop.addItem(playerInterfaces.get(i));
        }
        player2Drop.setPreferredSize(dropDownPreferredSize);
        player2Drop.setActionCommand("player2dropmenu");
        player2Drop.addActionListener(this);
        player2Difficulty.setPreferredSize(dropDownPreferredSize);
        frame.add(player2Drop,constraint);
        constraint.gridx--;
        constraint.gridy++;
        frame.add(new JLabel("<html><head></head><body><div style=\"font-family:'Segoe UI',Tahoma;font-weight:normal;font-size:1em;\">Difficulty: </div></body></html>"),constraint);
        constraint.gridx++;
        frame.add(player2Difficulty,constraint);
        constraint.gridy++;
        constraint.gridx=0;
        constraint.gridwidth=2;
        // start buttons
        startButton = new JButton("Inizia");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        frame.add(startButton,constraint);
        constraint.gridy++;

        constraint.fill=GridBagConstraints.HORIZONTAL;
        constraint.gridx=2;
        constraint.gridy=1;
        setDifficultyOptions(player1Drop,player1Difficulty);
        setDifficultyOptions(player2Drop,player2Difficulty);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        System.out.println("Ready. Waiting for game");
    }
    // button commands
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command == "player1dropmenu") {
            setDifficultyOptions(player1Drop,player1Difficulty);
        }
        if (command == "player2dropmenu") {
            setDifficultyOptions(player2Drop,player2Difficulty);
        }
        if (command == "start") {
            startGame();
        }
    }
    // set the diffculty options available
    static void setDifficultyOptions(JComboBox interfaceSelector, JComboBox difficultySelector) {
        difficultySelector.removeAllItems();

        DropDownItem[] difficultyOptions = null;
        try {
            Class Connect4Player = ((ClassComboBoxDisplayWrapper)interfaceSelector.getSelectedItem()).get();
            difficultyOptions = (DropDownItem[]) Connect4Player.getMethod("getDifficultySettings",null).invoke(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < difficultyOptions.length; i++)
            difficultySelector.addItem(difficultyOptions[i]);
    }
    // mouse controls
    public void mouseEntered(MouseEvent arg0) {
        // No action needed
    }

    public void mouseExited(MouseEvent arg0) {
        // No action needed
    }

    public void mousePressed(MouseEvent event) {
        if (event.getSource() instanceof PieceDisplay) {
            PieceDisplay clickedPiece = (PieceDisplay)event.getSource();
            sendClickToPlayer(clickedPiece.getPieceGridLocation());
        }
    }

    public void mouseReleased(MouseEvent arg0) {
        // No action needed
    }
    public void mouseClicked(MouseEvent event) {
        //No action needed
    }
    // application name and details
    public static String name() {
        return "Connect4 Antonio";
    }
    public static String getComponentDetails() {
        return  name() + " [v1.0]\n";
    }
    // application debugger output
    public String toString() {
        String state = getComponentDetails() + "\n State:\n";
        if (board instanceof Connect4Board) {
            state += "Controlling Board:\n" + board;
        } else {
            state += "Board not recognized.";
        }
        return state;
    }
    // update the board display
    void updateDisplayPanel() {
        byte[][] boardSet = board.getBoard();

        for (int x = 0; x  < boardSet.length; x++) {
            for (int y = 0; y < boardSet[x].length;y++) {
                displayUnits[x][y].setPiece(boardSet[x][y]);
            }
        }
    }
    // send the click to the controlling player
    void sendClickToPlayer(Point piece) {
        if (board.getTurn()!=Connect4Board.EMPTY) {
            player[board.getTurn()].boardClicked((int)piece.getY(),(int)piece.getX());
            NMoves += 1;
            updateDisplayPanel();
        }
    }
    // switch the player
    void switchPlayer() {
        switch (board.getTurn()) {
            case Connect4Board.BLACK:
                initializePlayer(Connect4Board.RED);
                break;
            case Connect4Board.RED:
                initializePlayer(Connect4Board.BLACK);
                break;
            default:
                break;
        }
    }
    // start a player
    void initializePlayer (byte initPlayer) {
        board.setTurn(initPlayer);
        setStateMessage("It is " + board.getPieceName(board.getTurn()).toLowerCase() + "'s turn. Waiting for move... ");
        player[initPlayer].doAutomation();
    }
    // display status message
    public void setStateMessage(String message) {
        stateLabel.setText("<html><head></head><body><div style=\"font-weight:normal;font-family:'Segoe UI',Tahoma,sans-serif\">" + message + "</div></body></html>");
    }
    // return control to next player
    protected void endControl(Connect4Player controllingPlayer) {
        updateDisplayPanel();
        if (board.isWinner()) {
            if(board.getPieceName(board.getTurn()) == "Black"){
                NWin1 += 1;
            }
            else if(board.getPieceName(board.getTurn()) == "Red"){
                NWin2 += 1;
            }
            NPlay += 1;
            JOptionPane.showMessageDialog(boardFrame,board.getPieceName(board.getTurn()) + " won!");
            System.out.println("\nIl giocatore: "+board.getPieceName(board.getTurn()) + " ha vinto");
            System.out.println("Ritornando al menù principale...");
            System.out.println("\nAlcune Statistiche: ");
            System.out.println("Numero di partite giocate: "+NPlay);
            System.out.println("Numero di mosse (Player 1 e 2): "+NMoves);
            System.out.println("Numero di vittorie (Player 1): "+NWin1);
            System.out.println("Numero di vittorie (Player 2): "+NWin2);
            System.out.println("Numero di pareggi : "+NTie);
            board.setTurn(Connect4Board.EMPTY);
            boardFrame.dispose();

        } else if(board.freeSpaces()==0) {
            NTie += 1;
            setStateMessage("Board full.");
            JOptionPane.showMessageDialog(boardFrame, "Board tied!");
            board.setTurn(Connect4Board.EMPTY);
            System.out.println("Ritornando al menù principale...");
            boardFrame.dispose();
        } else if (player[board.getTurn()].equals(controllingPlayer)) {
            switchPlayer();
        }

    }
    // start the game
    void startGame() {
        board.reset();
        if (boardFrame!= null) {
            boardFrame.dispose();
        }
        // board frame
        boardFrame = new JFrame("Connect 4 Board");
        boardFrame.setIconImage(img.getImage());
        boardFrame.setSize(720,660);
        boardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        boardFrame.setResizable(false);
        boardFrame.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        // layout and generate display
        byte[][] boardSet = board.getBoard();
        displayUnits = new PieceDisplay[boardSet.length][boardSet[0].length];
        for (int x = 0; x < boardSet.length; x++) {
            for (int y = 0; y < boardSet[x].length; y++) {
                constraint.gridx=x;
                constraint.gridy=boardSet[x].length-y-1;
                displayUnits[x][y] = new PieceDisplay(boardSet[x][y], new Point(x,y));
                boardFrame.add(displayUnits[x][y],constraint);
                displayUnits[x][y].addMouseListener(this);
            }
        }
        constraint.gridy = boardSet[0].length;
        constraint.gridx=0;
        constraint.anchor=GridBagConstraints.WEST;
        // format board and status bar
        constraint.gridwidth=GridBagConstraints.REMAINDER;
        constraint.gridheight=GridBagConstraints.REMAINDER;
        stateLabel.setSize(boardFrame.getWidth(),30);
        boardFrame.add(stateLabel,constraint);
        boardFrame.setVisible(true);;
        updateDisplayPanel();
        // create player classes
        player = new Connect4Player[3];
        try {
            player[1] = (Connect4Player)((ClassComboBoxDisplayWrapper)player1Drop.getSelectedItem()).get().newInstance();
            player[2] = (Connect4Player)((ClassComboBoxDisplayWrapper)player2Drop.getSelectedItem()).get().newInstance();
            player[1].setControllingClass(this);
            player[1].setBoard(board);
            player[2].setControllingClass(this);
            player[2].setBoard(board);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        player[1].setDifficulty(((DropDownItem)(player1Difficulty.getSelectedItem())).getIndex());
        player[2].setDifficulty(((DropDownItem)(player2Difficulty.getSelectedItem())).getIndex());
        initializePlayer(Connect4Board.BLACK);
        boardFrame.setBackground(Color.WHITE);

    }

}
class PieceDisplay extends Canvas {
    // Displays the piece as a canvas
    private static final long serialVersionUID = 1L;
    byte pieceType;
    private Point location;
    public static int drawSize = 100;
    PieceDisplay (byte setPieceType, Point setLocation) {

        pieceType = setPieceType;
        super.setPreferredSize(new Dimension(drawSize,drawSize));
        location = setLocation;
    }
    // redraw the piece
    public void paint(Graphics g) {

        super.paint(g); // Clears the canvas
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, drawSize,drawSize);
        switch (pieceType) {
            case Connect4Board.BLACK:
                g.setColor(Color.BLACK);
                break;
            case Connect4Board.RED:
                g.setColor(Color.RED);
                break;
            default:
                g.setColor(Color.LIGHT_GRAY);

                break;
        }
        g.fillOval((int)(0.05*drawSize), (int)(0.05*drawSize), (int)(0.9*drawSize), (int)(0.9*drawSize));

    }
    public void update(Graphics g) {
        paint(g);
    }
    // get the location of the piece
    public Point getPieceGridLocation() {
        return location;
    }
    // get the piece type
    public byte getPiece() {
        return pieceType;
    }
    public void setPiece(byte piece) {
        pieceType = piece;
        repaint();
    }
}
