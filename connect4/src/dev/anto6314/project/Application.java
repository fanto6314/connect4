package dev.anto6314.project;

public class Application {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        // set board class
        Connect4 gameApp = new Connect4(Board.class);
        // add players
        gameApp.addPlayerInterface(Human.class);
        gameApp.addPlayerInterface(Max.class);
        // show program
        gameApp.show();

    }
}