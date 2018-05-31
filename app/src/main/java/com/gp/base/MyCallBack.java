package com.gp.base;

import android.content.Context;
import android.view.View;

import com.github.androidtools.ToastUtils;
import com.github.baseclass.view.Loading;
import com.github.retrofitutil.NoNetworkException;
import com.library.base.ProgressLayout;
import com.library.base.ResponseObj;
import com.library.base.ServerException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/5/18.
 */

public abstract class MyCallBack<T> implements Callback<ResponseObj<T>> {

    private Context context;
    private boolean noHiddenLoad;
    private PtrFrameLayout pfl;
    private ProgressLayout progressLayout;

    public Context getContext() {
        return context;
    }
    public MyCallBack(Context ctx) {
        this.context = ctx;
    }
    public MyCallBack(Context ctx, ProgressLayout pl) {
        this.context = ctx;
        this.progressLayout = pl;
    }
    public MyCallBack(Context ctx, PtrFrameLayout pfl) {
        this.context = ctx;
        this.pfl = pfl;
    }
    public MyCallBack(Context ctx, PtrFrameLayout pfl, ProgressLayout pl) {
        this.context = ctx;
        this.pfl = pfl;
        this.progressLayout = pl;
    }
    public MyCallBack(Context ctx, ProgressLayout pl, PtrFrameLayout pfl) {
        this.context = ctx;
        this.pfl = pfl;
        this.progressLayout = pl;
    }
    public MyCallBack(Context ctx, boolean noHiddenLoad) {
        this.context = ctx;
        this.noHiddenLoad = noHiddenLoad;
    }
    public abstract void onSuccess(T obj,int errorCode,String msg);

    public void onError(Throwable e) {
        onError(e, false);
    }
    public void onError(Throwable e, boolean hiddenMsg) {
        if(pfl!=null){
            pfl.refreshComplete();
            pfl=null;
        }
        if(e instanceof ServerException ||e instanceof NoNetworkException){
            if(!hiddenMsg){
                ToastUtils.showToast(context,e.getMessage());
            }
        }else{
            if(!hiddenMsg){
                ToastUtils.showToast(context,"连接失败");
            }
            e.printStackTrace();
        }
        if (progressLayout != null) {
            progressLayout.showErrorText();
            if(e instanceof ServerException){
                if (((ServerException) e).errorCode!=0) {
                    progressLayout.againView.setVisibility(View.INVISIBLE);
                    progressLayout.tv_load_error_msg.setText(e.getMessage());
                }
            }
            progressLayout=null;
        }
        Loading.dismissLoading();
    }

    public void onCompelte() {
        if (!noHiddenLoad) {
            Loading.dismissLoading();
        }
        if (pfl != null) {
            pfl.refreshComplete();
        }
        if (progressLayout != null) {
            progressLayout.showContent();
            progressLayout = null;
        }
        this.context = null;
    }

    @Override
    public void onFailure(Call<ResponseObj<T>> call, Throwable t) {
        if (t instanceof ConnectException) {
            onError(new ServerException("服务器开小差去了,请稍后再试"));
        }else if(t instanceof SocketTimeoutException){
            onError(new ServerException("服务器连接超时,请稍后再试"));
        } else {
            onError(t);
        }
    }

    @Override
    public void onResponse(Call<ResponseObj<T>> call, Response<ResponseObj<T>> response) {
        ResponseObj<T> resBody = response.body();
        if (resBody == null) {
            if (response.code() == 500) {
                onError(new ServerException("服务器开小差去了,请稍后再试"));
            } else if(response.code() == 404) {
                onError(new ServerException("地址错误"));
            }else{
                onError(new ServerException("连接异常"));
            }
            onCompelte();
            return;
        }
        int errCode = resBody.getErrCode();
        if (errCode == 1) {
            onError(new ServerException(errCode,resBody.getErrMsg()));
        } else if (errCode == 2) {//2需要登录
            if (this.progressLayout != null) {//需要finish
                onError(new ServerException(response.body().getErrMsg()),false);
//                ActUtils.STActivity((Activity) context, LoginActivity.class);
//                ((Activity) context).finish();
            } else {
                onError(new ServerException(response.body().getErrMsg()));
//                ActUtils.STActivity((Activity) context, LoginActivity.class);
            }
        }else{
            onSuccess(resBody.getResponse(),resBody.getErrCode(),resBody.getErrMsg());
        }
        onCompelte();
        resBody=null;
    }
}
