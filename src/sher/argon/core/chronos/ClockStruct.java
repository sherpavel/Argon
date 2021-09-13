package sher.argon.core.chronos;

import sher.argon.Clock;

class ClockStruct {
    String name;
    Clock clock;
    int frequency;
    float relativeTime;
    int callCounter;
    long execTimeAccumulator;

    ClockStruct(String name, Clock clock, int frequency) {
        this.name = name;
        this.clock = clock;
        this.frequency = frequency;
        relativeTime = 1;
        callCounter = 0;
        execTimeAccumulator = 0;
    }

    void reset() {
        callCounter = 0;
        execTimeAccumulator = 0;
    }
}
