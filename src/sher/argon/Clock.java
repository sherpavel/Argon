package sher.argon;

/**
 * Clock contains user-created function that runs repeatedly with the set call rate (frequency).
 */
public interface Clock {
    /**
     * Function call method.
     */
    void tick();
}
