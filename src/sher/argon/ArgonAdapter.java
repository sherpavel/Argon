package sher.argon;

/**
 * Argon interface wrapper class.
 * Argon adapter provides the link between framework's UI and your program.
 */
public abstract class ArgonAdapter implements ArgonListener {
    @Override
    public void onReset() {}
    @Override
    public void variableUpdated() {}
    @Override
    public void windowMouseClick(int x, int y) {}
    @Override
    public void windowMouseMoved(int x, int y) {}
}
