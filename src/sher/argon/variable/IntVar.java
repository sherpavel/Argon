package sher.argon.variable;

import sher.argon.core.window.panels.IntVarPanel;

import javax.swing.JPanel;

/**
 * Integer dynamic variable.
 * Is used as intermediary between user's code and framework's UI.
 * @see Variable
 * @see NumVariable
 */
public class IntVar extends NumVariable<Integer> {
    /**
     * Initiates the variable
     * @param name variable's name
     * @param value value
     * @param min UI slider minimum value
     * @param max UI slider maximum value
     */
    public IntVar(String name, int value, int min, int max) {
        super(name, value, Math.min(min, value), Math.max(max, value));
    }

    /**
     * Initiates the variable
     * @param name variable's name
     * @param value value
     * @param minmax is determined to be either as UI slider min or max value
     */
    public IntVar(String name, int value, int minmax) {
        this(name, value, Math.min(value, minmax), Math.max(value, minmax));
    }

    /**
     * Initiates the variable
     * @param name variable's name
     * @param value value
     */
    public IntVar(String name, int value) {
        this(name, value, (int) (value*0.8), (int) (value*1.2));
    }

    @Override
    public void add(Integer n) {
        setVal(value + n);
    }

    @Override
    public void sub(Integer n) {
        setVal(value - n);
    }

    @Override
    public void mlp(Integer n) {
        setVal(value * n);
    }

    @Override
    public void div(Integer n) {
        setVal(value / n);
    }

    @Override
    public JPanel createPanel() {
        return new IntVarPanel(this);
    }
}
