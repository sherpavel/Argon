package sher.argon.variable;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Dynamic variable class.
 * Is used as intermediary between user's code and framework's UI.
 * The class cannot be initiated by the user. The raw usage is permitted, but not preferred.
 * @param <T> variable type
 */
public abstract class Variable<T> {
    static int GLOBAL_INDEX = 0;
    int index;
    String name;

    ArrayList<VariableListener<T>> variableListeners;

    protected Variable() {
        index = GLOBAL_INDEX++;
        name = "Variable " + index;
        variableListeners = new ArrayList<>();
    }

    /**
     * Returns variable's id.
     * @return id of the variable
     */
    public int getID() {
        return index;
    }

    /**
     * Returns variable's name
     * @return name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * Adds change listener to the variable
     * @param l change variable
     */
    public void addListener(VariableListener<T> l) {
        variableListeners.add(l);
    }

    /**
     * Initiates and returns the UI-compatible {@link JPanel}.
     * @return {@link JPanel}
     */
    public abstract JPanel createPanel();
}
