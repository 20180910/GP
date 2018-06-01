package com.gp.module.main.network;

import com.github.retrofitutil.NoNetworkException;
import com.gp.Config;
import com.gp.base.HuZhaoCallBack;
import com.library.base.BaseApiRequest;

/**
 * Created by Administrator on 2017/6/28.
 */

public class ApiRequest extends BaseApiRequest {

    public static void getSZData(String code, HuZhaoCallBack callBack) {
        if (notNetWork(callBack.getContext())) { callBack.onFailure(null, new NoNetworkException(Config.noNetWork)); return; }
        getGeneralStringClient(IRequest.class).getSZData(code).enqueue(callBack);
    }
    public static void getSHData(String code, HuZhaoCallBack callBack) {
        if (notNetWork(callBack.getContext())) { callBack.onFailure(null, new NoNetworkException(Config.noNetWork)); return; }
        getGeneralStringClient(IRequest.class).getSHData(code).enqueue(callBack);
    }


}
