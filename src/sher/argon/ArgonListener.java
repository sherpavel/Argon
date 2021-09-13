package sher.argon;

/**
 * Argon listener provides the link between framework's UI and your program.
 * Preferably use the {@link ArgonAdapter} instead.
 * @see ArgonAdapter
 */
interface ArgonListener {
    /**
     * Is called on UI Reset Button press or {@link Argon#reset()} call.
     */
    void onReset();

    /**
     * Is called on any {@link sher.argon.variable.Variable variable} value's change.
     * This function <b>will not</b> be called when the clocks are running.
     * @see Clock
     */
    void variableUpdated();

    /**
     * Is called on viewport mouse click.
     * The coordinates provided are scaled to {@link Layer layer's} dimensions, not window dimensions.
     * @param x canvas x coordinate
     * @param y canvas y coordinate
     */
    void windowMouseClick(int x, int y);

    /**
     * Is called on mouse movement inside the viewport.
     * The coordinates provided are scaled to {@link Layer layer's} dimensions, not window dimensions.
     * @param x canvas x coordinate
     * @param y canvas y coordinate
     */
    void windowMouseMoved(int x, int y);
}
