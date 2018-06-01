package com.gp;

import android.app.Application;

import com.github.retrofitutil.NetWorkManager;

/**
 * Created by Administrator on 2018/5/31.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkManager.getInstance(this,"http://qt.gtimg.cn/",BuildConfig.DEBUG);
    }
}
