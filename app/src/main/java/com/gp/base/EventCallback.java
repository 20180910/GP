package com.gp.base;

/**
 * Created by Administrator on 2018/5/16.
 */

public abstract class EventCallback<T> {
    public abstract void accept(T event);
    public void error(){};
}
