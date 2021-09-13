package sher.argon.variable;

import sher.argon.core.window.panels.DoubleVarPanel;

import javax.swing.JPanel;

/**
 * Double precision floating point dynamic variable.
 * Is used as intermediary between user's code and framework's UI.
 * @see Variable
 * @see NumVariable
 */
public class DoubleVar extends NumVariable<Double> {
    /**
     * Initiates the variable
     * @param name variable's name
     * @param value value
     * @param min UI slider minimum value
     * @param max UI slider maximum value
     */
    public DoubleVar(String name, double value, double min, double max) {
        super(name, value, Math.min(min, value), Math.max(max, value));
    }

    /**
     * Initiates the variable
     * @param name variable's name
     * @param value value
     * @param minmax is determined to be either as UI slider min or max value
     */
    public DoubleVar(String name, double value, double minmax) {
        this(name, value, Math.min(value, minmax), Math.max(value, minmax));
    }

    /**
     * Initiates the variable
     * @param name variable's name
     * @param value value
     */
    public DoubleVar(String name, double value) {
        this(name, value, value*0.8, value*1.2);
    }

    /**
     * Returns current value, converted to integer
     * @return integer current value
     */
    public int intVal() {
        return value.intValue();
    }

    @Override
    public void add(Double n) {
        setVal(value + n);
    }

    @Override
    public void sub(Double n) {
        setVal(value - n);
    }

    @Override
    public void mlp(Double n) {
        setVal(value * n);
    }

    @Override
    public void div(Double n) {
        setVal(value / n);
    }

    @Override
    public JPanel createPanel() {
        return new DoubleVarPanel(this);
    }
}
