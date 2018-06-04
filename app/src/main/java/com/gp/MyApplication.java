package com.gp;

import android.app.Application;

import com.github.baseclass.view.Loading;
import com.github.retrofitutil.NetWorkManager;
import com.library.base.ProgressLayout;

/**
 * Created by Administrator on 2018/5/31.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkManager.getInstance(this,"http://qt.gtimg.cn/",BuildConfig.DEBUG);
        ProgressLayout.setLoadView(R.layout.app_loading_view);
        Loading.setLoadView(R.layout.app_loading_view);
    }
}
