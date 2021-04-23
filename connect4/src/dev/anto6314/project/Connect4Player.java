package dev.anto6314.project;

import java.awt.Component;
import javax.swing.*;
public class Connect4Player implements ListCellRenderer{
    // board and state information
    private Connect4Board board;
    private Connect4 controlClass;
    // difficulty settings
    int difficulty = 0;
    protected int getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(int setting) {
        difficulty=setting;
    }
    // set the board to operate on
    public final void setBoard(Connect4Board setBoard) {
        board=setBoard;
    }
    // get the board to opereate on
    public final Connect4Board workingBoard() {
        return board;
    }
    // application debugging and display information
    public String fullName() {
        return "Connect 4 Interface Controller";
    }
    public String shortName() {
        return "Controller";
    }
    // action when board clicked
    public void boardClicked(int x, int y) {

    }
    // difficulty options
    public static DropDownItem[] getDifficultySettings()  {
        DropDownItem[] settings = new DropDownItem[1];
        settings[0]=new DropDownItem(0,"");
        return settings;
    }
    // release control of board
    protected final void relinquishControl() {
        controlClass.endControl(this);
    }
    // display message in status
    protected final void displayMessage(String message) {
        controlClass.setStateMessage(message);
    }
    //	 set the connect 4 application in contro
    public void setControllingClass(Connect4 caller) {
        controlClass = caller;
    }
    // player dropdown box renderer
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        return new JLabel(shortName());
    }
    // debuggin details
    public String getComponentDetails() {
        return fullName();
    }
    public String toString() {
        return shortName();
    }
    // automatic move for AI
    public void doAutomation() {

    }
}