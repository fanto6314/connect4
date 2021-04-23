package dev.anto6314.project;

public class DropDownItem {
    int index;
    String displayName;
    DropDownItem(int setIndex, String display) {
        index = setIndex;
        displayName = display;
    }
    // get stored value
    public int getIndex() {
        return index;
    }
    // get name
    public String toString() {
        return displayName;
    }
}