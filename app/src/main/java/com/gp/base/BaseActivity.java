package com.gp.base;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.androidtools.ClickUtils;
import com.github.androidtools.PhoneUtils;
import com.github.androidtools.SPUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.permission.PermissionCallback;
import com.github.rxbus.MyRxBus;
import com.github.rxbus.rxjava.MyFlowableSubscriber;
import com.library.base.BaseObj;
import com.library.base.MyBaseActivity;
import com.library.base.tools.has.AndroidUtils;
import com.library.base.tools.has.BitmapUtils;
import com.library.base.view.MyWebViewClient;
import com.sdklibrary.base.ShareParam;
import com.sdklibrary.base.ali.pay.MyAliOrderBean;
import com.sdklibrary.base.ali.pay.MyAliPay;
import com.sdklibrary.base.ali.pay.MyAliPayCallback;
import com.sdklibrary.base.ali.pay.PayResult;
import com.sdklibrary.base.qq.share.MyQQActivityResult;
import com.sdklibrary.base.qq.share.MyQQShare;
import com.sdklibrary.base.qq.share.MyQQShareListener;
import com.sdklibrary.base.qq.share.bean.MyQQWebHelper;
import com.sdklibrary.base.wx.inter.MyWXCallback;
import com.sdklibrary.base.wx.pay.MyWXOrderBean;
import com.sdklibrary.base.wx.pay.MyWXPay;
import com.sdklibrary.base.wx.share.MyWXShare;
import com.sdklibrary.base.wx.share.bean.MyWXWebHelper;
import com.sk.maiqian.AppXml;
import com.sk.maiqian.BuildConfig;
import com.sk.maiqian.Config;
import com.sk.maiqian.GetSign;
import com.sk.maiqian.R;
import com.sk.maiqian.module.home.event.RefreshOrderEvent;
import com.sk.maiqian.module.home.network.ApiRequest;
import com.sk.maiqian.module.home.network.response.ZiXunObj;
import com.sk.maiqian.module.order.activity.OrderDetailActivity;
import com.sk.maiqian.network.NetApiRequest;
import com.sk.maiqian.network.request.UploadImgBody;
import com.sk.maiqian.network.response.ShareObj;
import com.sk.maiqian.tools.FileUtils;
import com.tencent.tauth.UiError;
import com.youth.banner.Banner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

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
                    showFenXiangDialog();
                }
                break;
        }
    }

    @Override
    public void setAppRightImg(@DrawableRes int appRightImg) {
        super.setAppRightImg(appRightImg);
        if (appRightImg == R.drawable.share) {
            isShareImg = true;
        }
    }

    @Override
    protected void setClickListener() {
        super.setClickListener();
        if (BuildConfig.DEBUG && app_title != null) {
            app_title.setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    Log.i(TAG + "===", "userId===" + getUserId());
                }
            });
        }
    }

    protected String getSign(Map map) {
        return GetSign.getSign(map);
    }

    protected String getSign(String key, String value) {
        return GetSign.getSign(key, value);
    }

    protected void initWebViewForContent(WebView webview, String content) {
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
            }
        });

        webview.loadDataWithBaseURL(null, getNewContent(content), "text/html", "utf-8", null);
//        webview.loadUrl(url);
        // 设置WevView要显示的网页
//        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8",null);
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


    protected static String getNewContent(String htmltext) {
        try {
            Document doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%").attr("height", "auto");
            }
            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }

    Subscription subscription;

    public void countDown(TextView textView) {
        textView.setEnabled(false);
        final long count = 30;
        Flowable.interval(1, TimeUnit.SECONDS)
                .take(31)
                .map(integer -> count - integer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {
                        subscription = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        textView.setText("剩下" + aLong + "s");
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        textView.setEnabled(true);
                        textView.setText("获取验证码");
                    }
                });
        addSubscription(subscription);
    }

    protected void setBannerList(Banner bn_home, List bannerList) {
        if (notEmpty(bannerList)) {
            bn_home.setLayoutParams(ImageSizeUtils.getImageSizeLayoutParams(mContext));
            bn_home.setImages(bannerList);
            bn_home.setImageLoader(new GlideLoader());

            /*bn_home.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    HomeBannerObj.ShufflingListBean item = shufflingList.get(position);
                    //(0商品不存在 1普通商品 2限时抢购 3团购),status商品状态(0商品不存在或者活动已结束 1商品存在活动没结束
                    Intent intent=new Intent();
                    intent.putExtra(com.sk.yangyu.module.orderclass.Constant.IParam.goodsId,item.getGoods_id()+"");
                    if(item.getCode()==1&&item.getStatus()==1){
                        STActivity(intent,GoodsDetailActivity.class);
                    }else if(item.getCode()==2&&item.getStatus()==1){
                        intent.setAction(Config.IParam.xianShiQiangGou);
                        STActivity(intent,GoodsDetailXianShiActivity.class);
                    }else if(item.getCode()==3&&item.getStatus()==1){
                        STActivity(intent,GoodsDetailTuanGouActivity.class);
                    }
                }
            });*/
            bn_home.start();
        }
    }

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

    public void showFenXiangDialog() {
        if (fenXiangDialog == null) {
            View sexView = LayoutInflater.from(mContext).inflate(R.layout.popu_fen_xiang, null);
            sexView.findViewById(R.id.iv_yaoqing_wx).setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    fenXiang(ShareParam.friend);
                    fenXiangDialog.dismiss();
                }
            });
            sexView.findViewById(R.id.iv_yaoqing_friend).setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    fenXiang(ShareParam.friendCircle);
                    fenXiangDialog.dismiss();

                }
            });
            sexView.findViewById(R.id.iv_yaoqing_qq).setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    fenXiang(ShareParam.QQ);
                    fenXiangDialog.dismiss();
                }
            });
            sexView.findViewById(R.id.iv_yaoqing_qzone).setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    fenXiang(ShareParam.QZONE);
                    fenXiangDialog.dismiss();
                }
            });
            /*sexView.findViewById(R.id.iv_yaoqing_sina).setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    showMsg("正在开发中");
                    fenXiangDialog.dismiss();
                }
            });*/
            sexView.findViewById(R.id.tv_fenxiang_cancle).setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    fenXiangDialog.dismiss();
                }
            });
            fenXiangDialog = new BottomSheetDialog(mContext);
            fenXiangDialog.setCanceledOnTouchOutside(true);
            fenXiangDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fenXiangDialog.setContentView(sexView);
        }
        fenXiangDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyQQActivityResult.onActivityResult(requestCode, resultCode, data);
    }

    protected void fenXiang(@ShareParam.MyShareType int platform) {
        showLoading();
        Map<String, String> map = new HashMap<String, String>();
        map.put("rnd", getRnd());
        map.put("sign", GetSign.getSign(map));
        NetApiRequest.getShareInformation(map, new MyCallBack<ShareObj>(mContext, true) {
            @Override
            public void onSuccess(ShareObj obj, int errorCode, String msg) {
                if (platform == ShareParam.QQ || platform == ShareParam.QZONE) {
                    MyQQWebHelper helper = new MyQQWebHelper(platform);
                    helper.setTitle(obj.getTitle());
                    helper.setDescription(obj.getContent());
                    helper.setImagePath(obj.getImg());
                    helper.setUrl(obj.getShare_link());
                    MyQQShare.newInstance(mContext).shareWeb(helper, new MyQQShareListener() {
                        @Override
                        public void doComplete(Object o) {
                            dismissLoading();
                            showMsg("分享成功");
                        }

                        @Override
                        public void doError(UiError uiError) {
                            dismissLoading();
                            showMsg("分享失败");
                        }

                        @Override
                        public void doCancel() {
                            dismissLoading();
                            showMsg("取消分享");
                        }
                    });
                } else {
                    MyWXWebHelper helperWX = new MyWXWebHelper(platform);
                    helperWX.setUrl(obj.getShare_link());
                    helperWX.setTitle(obj.getTitle());
                    helperWX.setDescription(obj.getContent());
                    helperWX.setBitmapResId(R.drawable.share_img);
                    MyWXShare.newInstance(mContext).shareWeb(helperWX, new MyWXCallback() {
                        @Override
                        public void onSuccess() {
                            dismissLoading();
                            showMsg("分享成功");
                        }

                        @Override
                        public void onFail() {
                            dismissLoading();
                            showMsg("分享失败");
                        }

                        @Override
                        public void onCancel() {
                            dismissLoading();
                            showMsg("取消分享");
                        }
                    });

                }
            }
        });
    }

    public interface UploadImgCallback {
        void result(String imgUrl);
    }

    public void uploadImg(String imgPath, UploadImgCallback callback) {
        showLoading();
        RXStart(new MyFlowableSubscriber<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> subscriber) {
                try {
                    List<File> files = Luban.with(mContext).load(takePhotoImgSavePath).get();
                    File file = files.get(0);
                    String imgStr = BitmapUtils.fileToString(file);
                    subscriber.onNext(imgStr);
                    subscriber.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }

            @Override
            public void onNext(String base64) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("rnd", getRnd());
                map.put("sign", getSign(map));
                UploadImgBody body = new UploadImgBody();
                body.setFile(base64);
                NetApiRequest.uploadImg(map, body, new MyCallBack<BaseObj>(mContext) {
                    @Override
                    public void onSuccess(BaseObj obj, int errorCode, String msg) {
                        callback.result(obj.getImg());
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dismissLoading();
                showToastS("图片插入失败");
            }
        });
    }

    protected void collect(String dataId, String typeId, MyCallBack callBack) {
        showLoading();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", getUserId());
        map.put("type", typeId);
        map.put("all_id", dataId);
        map.put("sign", getSign(map));
        ApiRequest.collect(map, callBack);
/* new MyCallBack<CollectObj>(mContext) {
            @Override
            public void onSuccess(CollectObj obj) {
                showMsg(obj.getMsg());
                if (obj.getIs_collect() == 1) {
                    tv_qianzheng_detail_collect.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.collect_normal2, 0, 0);
                } else {
                    tv_qianzheng_detail_collect.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.collect_normal, 0, 0);
                }
            }
        }*/
    }

    /********************************************咨询******************************************************/
    /*protected void getZiXunData(MyCallBack callBack) {
        getZiXunData("0",callBack);
    }*/
    protected void getZiXunData(String guoJiaId, MyCallBack callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("countrie_region_id", guoJiaId);
        map.put("sign", getSign(map));
        ApiRequest.getZiXunInfo(map, callBack);
        /* new MyCallBack<ZiXunObj>(mContext) {
            @Override
            public void onSuccess(ZiXunObj obj) {
                ziXunObj = obj;
                if(isShow){
                    showZiXun();
                }
            }
        }*/
    }

    private void showZiXun() {
        Dialog dialog = new Dialog(mContext, R.style.DialogStyle);
        setDialogFullWidth(dialog);
        View zixun_popu = getLayoutInflater().inflate(R.layout.kechengdetail_zixun_popu, null);
        zixun_popu.findViewById(R.id.fl_zixun).setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                dialog.dismiss();
            }
        });
        View ll_zixun_tel = zixun_popu.findViewById(R.id.ll_zixun_tel);
        ll_zixun_tel.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                dialog.dismiss();
                requestPermission(Manifest.permission.CALL_PHONE, new PermissionCallback() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ziXunObj.getPhone()));
                        startActivity(intent);
                    }

                    @Override
                    public void onDenied(String s) {
                        showMsg("无法获取拨打电话权限,请开启权限之后再试");
                    }
                });
            }
        });
        View ll_zixun_wx = zixun_popu.findViewById(R.id.ll_zixun_wx);
        ll_zixun_wx.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                dialog.dismiss();
                showWX();
            }
        });
        dialog.setContentView(zixun_popu);
        dialog.show();
    }

    private void setDialogFullWidth(Dialog dialog) {
        Window win = dialog.getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        win.setGravity(Gravity.CENTER);
        win.getDecorView().setPadding(0, 0, 0, 0);
//        win.setBackgroundDrawableResource(R.color.transparent_half);
        win.setBackgroundDrawableResource(R.color.white_half);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = PhoneUtils.getScreenHeight(mContext);
        win.setAttributes(lp);
    }


    protected ZiXunObj ziXunObj;

    private void showWX() {
        View view = getLayoutInflater().inflate(R.layout.kechengdetail_wx_popu, null);
//        view.findViewById(R.id.tv_wx_zixun).setOnClickListener(getListenerSaveIMG());
        ImageView iv_wx_zixun = view.findViewById(R.id.iv_wx_zixun);
        iv_wx_zixun.setOnClickListener(getListenerSaveIMG());
        GlideUtils.into(mContext, ziXunObj.getWechat_code(), iv_wx_zixun);
        TextView tv_wx_zixun_code = view.findViewById(R.id.tv_wx_zixun_code);
        tv_wx_zixun_code.setText("微信号:" + ziXunObj.getWechat_id() + "(点击复制)");
        tv_wx_zixun_code.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                PhoneUtils.copyText(mContext, ziXunObj.getWechat_id());
                showMsg("复制成功");
            }
        });


        Dialog dialog = new Dialog(mContext, R.style.DialogStyle);
        Window win = dialog.getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        win.setGravity(Gravity.CENTER);
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setBackgroundDrawableResource(R.color.transparent_half);
        dialog.setContentView(view);
        dialog.show();
    }

    @android.support.annotation.NonNull
    private MyOnClickListener getListenerSaveIMG() {
        return new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionCallback() {
                    @Override
                    public void onGranted() {
                        FileUtils.downloadImg(mContext, ziXunObj.getWechat_code());
                    }

                    @Override
                    public void onDenied(String s) {
                        showMsg("获取文件管理权限失败,无法保存图片");
                    }
                });
            }
        };
    }

    //    protected void showZiXunDialog(){
//        showZiXunDialog("0");
//    }
    protected void showZiXunDialog(String id) {
        if (ziXunObj == null) {
            showLoading();
            getZiXunData(id, new MyCallBack<ZiXunObj>(mContext) {
                @Override
                public void onSuccess(ZiXunObj obj, int errorCode, String msg) {
                    ziXunObj = obj;
                    showZiXun();
                }
            });
        } else {
            showZiXun();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClickUtils.clearLastClickTime();
    }

    /*****************************************************************************/
    BottomSheetDialog peiXunPayDialog;

    protected void showPeiXunPay(String orderNo, double price, String orderType) {
        peiXunPayDialog = new BottomSheetDialog(mContext);
        peiXunPayDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        View view = LayoutInflater.from(mContext).inflate(R.layout.tijiaoorder_pay_popu, null);
        RadioButton rb_order_pay = view.findViewById(R.id.rb_order_pay);
        view.findViewById(R.id.tv_commit_liuyan).setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                showLoading();
                if (rb_order_pay.isChecked()) {
                    MyWXOrderBean bean = new MyWXOrderBean();
                    bean.setTotalFee((int) AndroidUtils.chengFa(price, 100));
                    bean.setOut_trade_no(orderNo);
                    bean.setBody("英语培训订单支付");
                    weixinPay(bean, peiXunPayDialog, orderType);
                } else {
                    MyAliOrderBean bean = new MyAliOrderBean();
                    bean.setOut_trade_no(orderNo);
                    bean.setTotal_amount(price);
                    bean.setBody("英语培训订单支付");
                    aliPay(bean, peiXunPayDialog, orderType);
                }

            }
        });

        peiXunPayDialog.setContentView(view);
        peiXunPayDialog.show();
    }

    private void weixinPay(MyWXOrderBean bean, BottomSheetDialog payDialog, String type) {
        String url = SPUtils.getString(mContext, Config.payType_WX, null);
        bean.setNotifyUrl(url);
//        bean.setIp("1");
        MyWXPay.newInstance(mContext).startPay(bean, new MyWXCallback() {
            @Override
            public void onSuccess() {
                BaseActivity.this.paySuccess(payDialog, type);
            }

            @Override
            public void onFail() {
                dismissLoading();
                showMsg("支付失败");
                if (payDialog != null) {
                    payDialog.dismiss();
                }
            }

            @Override
            public void onCancel() {
                dismissLoading();
                showMsg("支付已取消");
                if (payDialog != null) {
                    payDialog.dismiss();
                }
            }
        });
    }

    private void paySuccess(BottomSheetDialog payDialog, String type) {
        MyRxBus.getInstance().postReplay(new RefreshOrderEvent(type));

        if (mContext instanceof OrderDetailActivity) {
            OrderDetailActivity mContext = (OrderDetailActivity) BaseActivity.this.mContext;
            mContext.getData(1, false);
        } else {
            dismissLoading();
        }
        if (payDialog != null) {
            payDialog.dismiss();
        }
    }

    protected void aliPay(MyAliOrderBean bean, BottomSheetDialog payDialog, String type) {
        String url = SPUtils.getString(mContext, Config.payType_ZFB, null);
        bean.setAppId(Config.zhifubao_app_id);
        bean.setPid(Config.zhifubao_pid);
        bean.setSiYao(Config.zhifubao_rsa2);
        bean.setNotifyUrl(url);
        bean.setSubject("麦签订单支付");
        MyAliPay.newInstance(mContext).startPay(bean, new MyAliPayCallback() {
            @Override
            public void paySuccess(PayResult payResult) {
                BaseActivity.this.paySuccess(payDialog, type);
            }

            @Override
            public void payFail() {
                dismissLoading();
                showMsg("支付失败");
                if (payDialog != null) {
                    payDialog.dismiss();
                }
            }

            @Override
            public void payCancel() {
                dismissLoading();
                showMsg("支付已取消");
                if (payDialog != null) {
                    payDialog.dismiss();
                }
            }
        });
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

