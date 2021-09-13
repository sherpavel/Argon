package sher.argon.core.chronos;

import sher.argon.Clock;

import java.util.ArrayList;

public class Chronos {
    Thread chronosThread;
    boolean RUNNING;
    ArrayList<ClockStruct> clockStructs;
    ArrayList<ChronosListener> chronosListeners;

    public Chronos() {
        clockStructs = new ArrayList<>();
        chronosListeners = new ArrayList<>();
        RUNNING = false;
    }

    public void addChronosListener(ChronosListener chronosListener) {
        chronosListeners.add(chronosListener);
    }

    public void addClock(String name, Clock clock, int frequency) {
        if (name == null)
            name = "Clock " + (clockStructs.size()+1);
        clockStructs.add(new ClockStruct(name, clock, frequency));
    }
    public void removeClock(int index) {
        clockStructs.remove(index);
    }

    public int getClocksCount() {
        return clockStructs.size();
    }
    public String[] getClockNames() {
        String[] names = new String[clockStructs.size()];
        int i = 0;
        for (ClockStruct clockStruct : clockStructs)
            names[i++] = clockStruct.name;
        return names;
    }

    public boolean isRunning() {
        return RUNNING;
    }

    public boolean start() {
        if (clockStructs.size() == 0) return false;
        if (RUNNING) return false;
        RUNNING = true;
        chronosThread = new Thread(this::realtime);
        chronosThread.start();
        return true;
    }
    void realtime() {
        ClockStruct baseClock = clockStructs.get(0);
        for (ClockStruct clockStruct : clockStructs) {
            if (clockStruct.frequency > baseClock.frequency)
                baseClock = clockStruct;
        }

        for (ClockStruct clockStruct : clockStructs)
            clockStruct.relativeTime = (float) baseClock.frequency / clockStruct.frequency;

        long baseFreqTime = 1000000000 / baseClock.frequency;

        long sleepOffset = 0;
        long secondsTimer = System.nanoTime();
        long baseTimer = System.nanoTime();
        while (RUNNING) {
            if (System.nanoTime() - baseTimer >= baseFreqTime) {
                baseTimer = System.nanoTime();

                for (ClockStruct clockStruct : clockStructs) {
                    if (baseClock.callCounter - (int) (clockStruct.relativeTime * clockStruct.callCounter) >= 0) {
                        clockStruct.callCounter++;
                        long execStartTime = System.nanoTime();
                        clockStruct.clock.tick();
                        clockStruct.execTimeAccumulator += System.nanoTime() - execStartTime;
                    }
                }
            }

            if (System.nanoTime() - secondsTimer >= 1000000000) {
                secondsTimer = System.nanoTime();

                String[] names = new String[clockStructs.size()];
                int[] callCounters = new int[clockStructs.size()];
                float[] avgExecTimes = new float[clockStructs.size()];
                int i = 0;
                for (ClockStruct clockStruct : clockStructs) {
                    names[i] = clockStruct.name;
                    callCounters[i] = clockStruct.callCounter;
                    avgExecTimes[i] = clockStruct.execTimeAccumulator / (clockStruct.callCounter * 1000000f);
                    i++;
                    clockStruct.reset();
                }

                for (ChronosListener listener : chronosListeners)
                    listener.secondsTimer(names, callCounters, avgExecTimes);
            }

            /* Low idle usage, but useless for non-realtime application */
//            long execTime = System.nanoTime() - baseTimer;
//            if (execTime < baseFreqTime) {
//                long sleepTime = System.nanoTime();
//                try {
////                    TimeUnit.NANOSECONDS.sleep(relTargetTimes[baseFreqIndex] - execTime - sleepOffset);
//                    Thread.sleep((baseFreqTime - execTime - sleepOffset) / 1000000);
//                } catch (Exception ignore) {}
//                sleepTime = System.nanoTime() - sleepTime;
//                sleepOffset = sleepTime - (baseFreqTime - execTime - sleepOffset);
//            }
        }
    }
//    void fastLoop() {
//        ClockStruct baseClock = clockStructs.get(0);
//        for (ClockStruct clockStruct : clockStructs) {
//            if (clockStruct.frequency > baseClock.frequency)
//                baseClock = clockStruct;
//        }
//
//        for (ClockStruct clockStruct : clockStructs)
//            clockStruct.relativeTime = (float) baseClock.frequency / clockStruct.frequency;
//
//        long secondsTimer = System.nanoTime();
//        while (LOOP) {
//            for (ClockStruct clockStruct : clockStructs) {
//                if (baseClock.callCounter - (int) (clockStruct.relativeTime * clockStruct.callCounter) >= 0) {
//                    clockStruct.callCounter++;
//                    long execStartTime = System.nanoTime();
//                    clockStruct.clock.tick();
//                    clockStruct.execTimeAccumulator += System.nanoTime() - execStartTime;
//                }
//            }
//
//            if (System.nanoTime() - secondsTimer >= 1000000000) {
//                secondsTimer = System.nanoTime();
//
//                int[] callCounters = new int[clockStructs.size()];
//                float[] avgExecTimes = new float[clockStructs.size()];
//                int i = 0;
//                for (ClockStruct clockStruct : clockStructs) {
//                    callCounters[i] = clockStruct.callCounter;
//                    avgExecTimes[i] = clockStruct.execTimeAccumulator / (clockStruct.callCounter * 1000000f);
//                    i++;
//                    clockStruct.reset();
//                }
//
//                for (ChronosListener listener : chronosListeners)
//                    listener.secondsTimer(callCounters, avgExecTimes);
//            }
//        }
//    }

    public void stop() {
        if (!RUNNING) return;
        RUNNING = false;
        try {
            chronosThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
