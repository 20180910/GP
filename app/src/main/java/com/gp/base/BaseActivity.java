package com.gp.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.github.androidtools.ClickUtils;
import com.github.androidtools.SPUtils;
import com.github.baseclass.view.Loading;
import com.github.baseclass.view.MyDialog;
import com.github.rxbus.MyConsumer;
import com.github.rxbus.MyDisposable;
import com.github.rxbus.RxBus;
import com.github.rxbus.rxjava.MyFlowableSubscriber;
import com.github.rxbus.rxjava.Rx;
import com.gp.AppXml;
import com.gp.Constant;
import com.gp.GetSign;
import com.gp.R;
import com.gp.module.main.bean.GpBean;
import com.gp.module.main.network.ApiRequest;
import com.library.base.MyBaseActivity;
import com.library.base.ProgressLayout;
import com.library.base.view.MyWebViewClient;

import org.reactivestreams.Subscription;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.FlowableEmitter;
import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2017/12/18.
 */

public abstract class BaseActivity<I extends BaseDaoImp> extends MyBaseActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected final String noLoginCode = "0";
    protected I mDaoImp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        pageSize= Constant.pageSize;
        pagesize= Constant.pageSize;
        mDaoImp= getDaoImp();
        if (mDaoImp != null) {
            mDaoImp.setContext(this);
        }
        super.onCreate(savedInstanceState);
    }
    private I getDaoImp(){
        Type genericSuperclass = getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType){
            //参数化类型
            ParameterizedType parameterizedType= (ParameterizedType) genericSuperclass;
            //返回表示此类型实际类型参数的 Type 对象的数组
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            try {
                mDaoImp=((Class<I>)actualTypeArguments[0]).newInstance();
            } catch (java.lang.InstantiationException e) {
                mDaoImp=null;
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                mDaoImp=null;
                e.printStackTrace();
            }
        }else{
            mDaoImp=null;
        }
        return mDaoImp;
    }
    protected String getUserId() {
        return SPUtils.getString(mContext, AppXml.user_id, noLoginCode);
    }

    protected void clearUserId() {
        SPUtils.removeKey(mContext, AppXml.user_id);
    }

    public boolean noLogin() {
        if (noLoginCode.equals(getUserId()) || TextUtils.isEmpty(getUserId())) {
            return true;
        } else {
            return false;
        }
    }

    public void Log(String msg) {
        Log.i(TAG + "===", msg);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.app_right_iv:
                if ("share".equals(v.getTag())) {
                    Log("===分享=");
                }
                break;
        }
    }

    @Override
    public void setAppRightImg(@DrawableRes int appRightImg) {
        super.setAppRightImg(appRightImg);

    }



    protected String getSign(Map map) {
        return GetSign.getSign(map);
    }

    protected String getSign(String key, String value) {
        return GetSign.getSign(key, value);
    }


    protected void initSimpleWebViewForUrl(WebView webview, String url) {
        WebSettings webSetting = webview.getSettings();
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //此方法不支持4.4以后
        webSetting.setUseWideViewPort(true);
        webSetting.setJavaScriptEnabled(true);
        webview.loadUrl(url);
        webview.setWebViewClient(new MyWebViewClient());
    }

    protected void initWebViewForUrl(final WebView webview, String url) {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //自适应屏幕  
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webview.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        //设置Web视图
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                initWebTopView();
                imgReset(webview);
            }
        });

//        webview.loadDataWithBaseURL(null, getNewContent(url), "text/html", "utf-8",null);
        webview.loadUrl(url);
        // 设置WevView要显示的网页
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
        webview.requestFocus(); //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void imgReset(WebView webview) {
        webview.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                " img.style.maxWidth = '100%';img.style.height='auto';" +
                "}" +
                "})()");
    }



    Subscription subscription;

    public int getAppVersionCode() {
        Context context = mContext;
        int versioncode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = pi.versionName;
            versioncode = pi.versionCode;
            return versioncode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    public String getAppVersionName() {
        Context context = mContext;
        int versioncode = 1;
        String versionName = "V1.0.0";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            return versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


    BottomSheetDialog fenXiangDialog;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        MyQQActivityResult.onActivityResult(requestCode, resultCode, data);
    }


    public interface UploadImgCallback {
        void result(String imgUrl);
    }
    /*******************************************Rx*************************************************/
    protected Set eventSet,ioSet;

    public <T> void RXStart(final IOCallBack<T> callBack) {
        RXStart(null,false,callBack);
    }
    public <T> void RXStart(boolean hiddenLoading,final IOCallBack<T> callBack) {
        RXStart(null,hiddenLoading,callBack);
    }
    public <T> void RXStart(ProgressLayout progressLayout, final IOCallBack<T> callBack) {
        RXStart(progressLayout,false,callBack);
    }
    public <T> void RXStart(final ProgressLayout progressLayout,final boolean hiddenLoading, final IOCallBack<T> callBack) {
        org.reactivestreams.Subscription start = Rx.start(new MyFlowableSubscriber<T>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<T> emitter) {
                callBack.call(emitter);
            }
            @Override
            public void onNext(T obj) {
                callBack.onMyNext(obj);
            }
            @Override
            public void onComplete() {
                super.onComplete();
                if(progressLayout!=null){
                    progressLayout.showContent();
                }
                if(!hiddenLoading){
                    Loading.dismissLoading();
                }
                callBack.onMyCompleted();
            }
            @Override
            public void onError(Throwable t) {
                super.onError(t);
                if(progressLayout!=null){
                    progressLayout.showErrorText();
                }
                if(!hiddenLoading){
                    Loading.dismissLoading();
                }
                callBack.onMyError(t);
            }
        });
        addSubscription(start);
    }
    public void addSubscription(MyDisposable disposable){
        if(eventSet==null){
            eventSet=new HashSet();
        }
        eventSet.add(disposable);
    }
    public void addSubscription(Subscription subscription){
        if(ioSet==null){
            ioSet=new HashSet();
        }
        ioSet.add(subscription);
    }

    public <T>void getEvent(Class<T> clazz,final EventCallback<T> callback){
        MyDisposable event = RxBus.getInstance().getEvent(clazz, new MyConsumer<T>() {
            @Override
            public void onAccept(T event) {
                callback.accept(event);
            }
        });
        addSubscription(event);
    }
    public <T>void getEventReplay(Class<T> clazz,final EventCallback<T> callback){
        MyDisposable event = RxBus.getInstance().getEventReplay(clazz, new MyConsumer<T>() {
            @Override
            public void onAccept(T event) {
                callback.accept(event);
            }
        });
        addSubscription(event);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClickUtils.clearLastClickTime();
        if(eventSet!=null){
            RxBus.getInstance().dispose(eventSet);
        }
        if (ioSet != null) {
            Rx.cancelSubscription(ioSet);
        }
    }
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected TextView tv_adddata_progress;
    protected TextView tv_updatedata_progress;
    protected MyDialog myDialog;
    protected void initDialog(){
        final MyDialog.Builder mDialog = new MyDialog.Builder(mContext);
        mDialog.setTitle("添加数据进度");
        View view = getLayoutInflater().inflate(R.layout.adddata_popu, null);
        tv_adddata_progress = view.findViewById(R.id.tv_adddata_progress);
        tv_adddata_progress.setVisibility(View.INVISIBLE);
        tv_updatedata_progress = view.findViewById(R.id.tv_updatedata_progress);
        tv_updatedata_progress.setVisibility(View.INVISIBLE);
        mDialog.setContentView(view);
        mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
            }
        });
        myDialog = mDialog.create();
        myDialog.show();
    }

    public GpBean requestForCode(String code,int type){
        String obj = ApiRequest.getDataTongBu(code,type);
        if (obj != null && obj.indexOf("v_pv_none_match") == -1) {
            GpBean bean = BaseGP.formatStr(obj);
            return  bean;
        }
        return null;
    }

}

