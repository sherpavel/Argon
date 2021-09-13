package sher.argon.variable;

/**
 * Extended class for numerical variables.
 * The class cannot be initiated by the user. The raw usage is permitted, but not preferred.
 * @param <T> variable type
 * @see DoubleVar
 * @see IntVar
 */
public abstract class NumVariable<T> extends Variable<T> {
    T value, min, max;

    protected NumVariable(String name, T value, T min, T max) {
        super();
        if (name != null) this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    // Getters
    /**
     * Returns the variable's current value
     * @return value of the variable
     */
    public T value() {
        return value;
    }

    /**
     * Returns the UI slider minimum value
     * @return minimum value of the UI slider
     */
    public T getMin() {
        return min;
    }

    /**
     * Returns the UI slider maximum value
     * @return maximum value of the UI slider
     */
    public T getMax() {
        return max;
    }

    // Setters
    /**
     * Sets the variable's value and calls the change listeners.
     * @param value new value
     */
    public void setVal(T value) {
        if (value == this.value) return;
        T oldValue = this.value;
        this.value = value;
        for (VariableListener<T> variableListener : variableListeners)
            variableListener.valueChanged(oldValue, this.value);
    }

    /**
     * Sets the the UI slider minimum value
     * @param min minimum value
     */
    public void setMin(T min) {
        this.min = min;
    }

    /**
     * Sets the the UI slider maximum value
     * @param max maximum value
     */
    public void setMax(T max) {
        this.max = max;
    }

    /**
     * Adds to the current variable's value.
     * @param n value to add
     */
    public abstract void add(T n);

    /**
     * Subtracts from the current variable's value.
     * @param n value to subtract
     */
    public abstract void sub(T n);

    /**
     * Multiples the current variable's value by the amount.
     * @param n multiplier
     */
    public abstract void mlp(T n);

    /**
     * Divides the current variable's value by the amount.
     * @param n divider
     */
    public abstract void div(T n);

    @Override
    public String toString() {
        return "Variable{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
