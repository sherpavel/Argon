package sher.argon.core.Profiler;

public class Profiler {
    public static long nanoTime(Function function) {
        long time = System.nanoTime();
        function.exec();
        return System.nanoTime() - time;
    }

    public static double millis(Function function) {
        return nanoTime(function) / 1000000f;
    }

    public static void printMillisTime(long time) {
        System.out.print(String.format("%.1f", time / 1000000f) + "\r");
    }
}
