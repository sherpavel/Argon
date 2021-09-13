package sher.argon.core.window;

public interface WindowListener {
    boolean startChronos();
    void stopChronos();
    void reset();
    void capture();
    void startRecording();
    void stopRecording();
    void mouseClick(int x, int y);
    void mouseMoved(int x, int y);
}
