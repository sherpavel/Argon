package sher.argon.core.chronos;

public interface ChronosListener {
    void secondsTimer(String[] clockNames, int[] callCounters, float[] avgExecTimes);
}
