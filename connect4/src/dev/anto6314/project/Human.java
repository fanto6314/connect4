package dev.anto6314.project;

public class Human extends Connect4Player {
    // debug notes
    public String shortName() {
        return "Umano";
    }
    public String fullName() {
        return "Connect 4 Human Interface";
    }
    public String getComponentDetails() {
        return fullName() + " [v1.0]";
    }
    // response to board click
    public void boardClicked(int x, int y) {
        if (workingBoard().drop(y)) {
            relinquishControl();
        } else {
            displayMessage("Cannot drop " + workingBoard().getPieceName(workingBoard().getTurn()) + " piece here");
        }
    }
}