package com.gp.base;

import android.content.Context;

/**
 * Created by windows10 on 2018/6/5.
 */

public abstract class BaseBiz {
    protected String TAG=this.getClass().getSimpleName();
    protected Context mContext;
    public void setContext(Context context){
        mContext=context;
    }
}
