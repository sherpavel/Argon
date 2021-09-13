package sher.argon.variable;

import sher.argon.core.window.panels.TogglePanel;

import javax.swing.JPanel;

/**
 * Boolean dynamic variable.
 * Is used as intermediary between user's code and framework's UI.
 * @see Variable
 */
public class Flag extends Variable<Boolean> {
    boolean flag;

    /**
     * Initiates the variable
     * @param name variable's name
     * @param flag flag's state
     */
    public Flag(String name, boolean flag) {
        super();
        if (name != null) this.name = name;
        this.flag = flag;
    }

    /**
     * Initiates the variable.
     * Default flag's state is {@code false}.
     * @param name variable's name
     */
    public Flag(String name) {
        this(name, false);
    }

    /**
     * Returns flag's state
     * @return flag's state
     */
    public boolean isSet() {
        return flag;
    }

    /**
     * Sets the flag's sate and calls change listeners.
     * @param flag new state
     */
    public void set(boolean flag) {
        if (flag == this.flag) return;
        boolean oldValue = this.flag;
        this.flag = flag;
        for (VariableListener<Boolean> variableListener : variableListeners)
            variableListener.valueChanged(oldValue, this.flag);
    }

    /**
     * Toggles current sate.
     */
    public void toggle() {
        set(!flag);
    }

    @Override
    public String toString() {
        return "Flag{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", flag=" + flag +
                '}';
    }

    @Override
    public JPanel createPanel() {
        return new TogglePanel(this);
    }
}
