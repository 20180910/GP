package com.gp.base;

import android.content.Context;
import android.view.View;

import com.github.androidtools.ToastUtils;
import com.github.baseclass.view.Loading;
import com.github.retrofitutil.NoNetworkException;
import com.library.base.ProgressLayout;
import com.library.base.ServerException;
import com.sk.maiqian.network.response.HuZhaoObj;

import java.net.ConnectException;

import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/5/18.
 */

public abstract class HuZhaoCallBack implements Callback<HuZhaoObj> {

    private Context context;
    private boolean noHiddenLoad;
    private PtrFrameLayout pfl;
    private ProgressLayout progressLayout;

    public Context getContext() {
        return context;
    }

    public HuZhaoCallBack(Context ctx) {
        this.context = ctx;
    }

    public HuZhaoCallBack(Context ctx, ProgressLayout pl) {
        this.context = ctx;
        this.progressLayout = pl;
    }

    public HuZhaoCallBack(Context ctx, PtrFrameLayout pfl) {
        this.context = ctx;
        this.pfl = pfl;
    }

    public HuZhaoCallBack(Context ctx, PtrFrameLayout pfl, ProgressLayout pl) {
        this.context = ctx;
        this.pfl = pfl;
        this.progressLayout = pl;
    }

    public HuZhaoCallBack(Context ctx, ProgressLayout pl, PtrFrameLayout pfl) {
        this.context = ctx;
        this.pfl = pfl;
        this.progressLayout = pl;
    }

    public HuZhaoCallBack(Context ctx, boolean noHiddenLoad) {
        this.context = ctx;
        this.noHiddenLoad = noHiddenLoad;
    }

    public abstract void onSuccess(HuZhaoObj obj);

    public void onError(Throwable e, boolean showContentView) {
        if(pfl!=null){
            pfl.refreshComplete();
            pfl=null;
        }
        if(e instanceof ServerException ||e instanceof NoNetworkException){
            ToastUtils.showToast(context,e.getMessage());
        }else{
            ToastUtils.showToast(context,"连接失败");
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

    public void onError(Throwable e) {
        onError(e, true);
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
    public void onFailure(Call<HuZhaoObj> call, Throwable t) {
        if (t instanceof ConnectException) {
            onError(new ServerException("服务器开小差去了,请稍后再试"));
        } else {
            onError(t);
        }

    }

    @Override
    public void onResponse(Call<HuZhaoObj> call, Response<HuZhaoObj> response) {
        HuZhaoObj resBody = response.body();
        if (resBody == null) {
            onError(new ServerException("处理失败"));
            onCompelte();
            return;
        }
        if (resBody.isSuccess()) {
            onSuccess(resBody);
        }else{
            onError(new ServerException(1,"处理失败"));
        }
        onCompelte();
        resBody = null;
    }
}
