package com.gp.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.DrawableRes;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.androidtools.ClickUtils;
import com.github.androidtools.SPUtils;
import com.gp.AppXml;
import com.gp.GetSign;
import com.gp.R;
import com.library.base.MyBaseActivity;
import com.library.base.view.MyWebViewClient;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18.
 */

public abstract class BaseActivity extends MyBaseActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected final String noLoginCode = "0";

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClickUtils.clearLastClickTime();
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

}

