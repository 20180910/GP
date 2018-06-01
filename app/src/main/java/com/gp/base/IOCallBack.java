package com.gp.base;

import io.reactivex.FlowableEmitter;

/**
 * Created by Administrator on 2018/5/16.
 */

public abstract class IOCallBack<T> {
    public IOCallBack() {
    }

    public abstract void call(FlowableEmitter<T> emitter);

    public abstract void onMyNext(T obj);
    //onMyNext
    //onMyCompleted
    //onMyError
    public void onMyCompleted() {
    }

    public void onMyError(Throwable e) {
        e.printStackTrace();
    }
}

