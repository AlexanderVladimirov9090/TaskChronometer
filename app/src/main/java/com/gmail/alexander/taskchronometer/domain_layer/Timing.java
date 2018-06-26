package com.gmail.alexander.taskchronometer.domain_layer;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 * This is timing object and xalculates elapsed time.
 */
public class Timing implements Serializable {
    private static final long serialVersionUID = 20161120L;
    private static final String TAG = Timing.class.getSimpleName();
    private long id;
    private Task task;
    private long startTime;
    private long duration;

    public Timing(Task task) {
        this.task = task;
        Date currentTime = new Date();
        startTime = currentTime.getTime() / 1000;
        duration = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration() {
        Date currentTime = new Date();
       this.duration = (currentTime.getTime()/1000) - startTime;
        Log.d(TAG, "setDuration: Duration: "+ duration);
    }
}
