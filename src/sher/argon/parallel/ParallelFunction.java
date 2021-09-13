package sher.argon.parallel;

/**
 * User-defined function for use with {@link Parallel}.
 * @see Parallel
 */
public interface ParallelFunction {
    /**
     * The function is called across multiple threads.
     * Index value is passed by the {@link Parallel} instance and is typically used for iteration.
     * <br>
     * Of note: the contents of this function must be thread-safe
     * @param index iteration index
     * @see Parallel
     */
    void function(int index);
}
