package dev.anto6314.project;

public class ClassComboBoxDisplayWrapper {
    // stored class
    private Class displayClass;
    ClassComboBoxDisplayWrapper(Class content) {
        displayClass=content;
    }
    public Class get() {
        return displayClass;
    }
    // display class name
    public String toString() {
        Object extractName = null;
        try {
            extractName = displayClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return extractName.toString();
    }
}