package sher.argon.parallel;

import sher.argon.math.Calc;

/**
 * Parallel is a tool to simplify the utilization of parallel computing and iteration.
 * The class uses Java {@link Thread threads}.
 * @see ParallelFunction
 */
public class Parallel {
    ParallelFunction parallelFunction;

    /**
     * Creates the parallel instance.
     * The {@link ParallelFunction parallel function} is user-created object that is called across multiple threads.
     * @param parallelFunction parallel function
     * @throws IllegalArgumentException if function is null or the task length is less than 1
     * @see ParallelFunction
     */
    public Parallel(ParallelFunction parallelFunction) {
        if (parallelFunction == null)
            throw new IllegalArgumentException("Function is null");
        this.parallelFunction = parallelFunction;
    }

    /**
     * Initiates, starts and joins the threads.
     * The size variable defines the upper index bound for multithreaded iteration.
     * @param size {@link ParallelFunction function's} index upper bound
     * @param threadsCount number of utilized threads.
     * @throws IllegalArgumentException if size or threads count is less than 1
     */
    public void start(int size, int threadsCount) {
        if (size < 1 || threadsCount < 1)
            throw new IllegalArgumentException("Size or Threads count less than 1");

        Thread[] threads = new Thread[threadsCount];
        for (int t = 0; t < threads.length; t++) {
            int startIndex = size * t / threads.length;
            int endIndex = size * (t+1) / threads.length;
            threads[t] = new Thread(() -> {
                for (int index = startIndex; index < endIndex; index++) {
                    parallelFunction.function(index);
                }
            });
        }

        for (Thread thread : threads)
            thread.start();

        try {
            for (Thread thread : threads)
                thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void startAndLog(int size, int threadsCount, int threadToLog) {
        if (size < 1 || threadsCount < 1)
            throw new IllegalArgumentException("Size or Threads count less than 1");

        Thread[] threads = new Thread[threadsCount];
        for (int t = 0; t < threads.length; t++) {
            int startIndex = size * t / threads.length;
            int endIndex = size * (t+1) / threads.length;
            int finalT = t;
            threads[t] = new Thread(() -> {
                if (finalT != threadToLog) {
                    for (int index = startIndex; index < endIndex; index++)
                        parallelFunction.function(index);
                } else {
                    long time = System.currentTimeMillis();
                    for (int index = startIndex; index < endIndex; index++) {
                        if (System.currentTimeMillis() - time >= 1000) {
                            time = System.currentTimeMillis();
                            System.out.println(String.format("%.1f", Calc.map(index, startIndex, endIndex, 0d, 1d) * 100d) + "% complete");
                        }
                        parallelFunction.function(index);
                    }
                }
            });
        }

        for (Thread thread : threads)
            thread.start();

        try {
            for (Thread thread : threads)
                thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
