package sher.argon.variable;

/**
 * Variable change listener.
 * @param <T> listener of appropriate type
 */
public interface VariableListener<T> {
    /**
     * Is called once the value has been changed.
     * @param oldValue old value
     * @param currentValue current new value
     */
    void valueChanged(T oldValue, T currentValue);
}
