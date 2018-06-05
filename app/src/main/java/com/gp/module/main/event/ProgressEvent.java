package com.gp.module.main.event;

/**
 * Created by windows10 on 2018/6/5.
 */

public class ProgressEvent {
    public int progress;
    public int count;

    public ProgressEvent(int progress, int count) {
        this.progress = progress;
        this.count = count;
    }
}
