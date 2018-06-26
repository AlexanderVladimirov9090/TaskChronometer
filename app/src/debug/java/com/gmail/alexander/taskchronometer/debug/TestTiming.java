package com.gmail.alexander.taskchronometer.debug;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 *     This is Timing test data class.
 */
public class TestTiming {
    long taskId;
    long startTime;
    long duration;

    public TestTiming(long taskId, long startTime, long duration) {
        this.taskId = taskId;
        this.startTime = startTime / 1000; //convert to seconds.
        this.duration = duration;
    }
}
