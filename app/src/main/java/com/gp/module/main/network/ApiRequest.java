package com.gp.module.main.network;

import com.github.retrofitutil.NoNetworkException;
import com.gp.Config;
import com.gp.base.HuZhaoCallBack;
import com.library.base.BaseApiRequest;

import java.io.IOException;

import retrofit2.Response;

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
    public static void getData(String code,int type, HuZhaoCallBack callBack) {
        if (notNetWork(callBack.getContext())) { callBack.onFailure(null, new NoNetworkException(Config.noNetWork)); return; }
        if(type==0){
            getGeneralStringClient(IRequest.class).getSZData(code).enqueue(callBack);
        }else{
            getGeneralStringClient(IRequest.class).getSHData(code).enqueue(callBack);
        }
    }

    public static String getDataTongBu(String code,int type)  {
        if(type==0){
            try {
                Response<String> execute = getGeneralStringClient(IRequest.class).getSZData(code).execute();
                return  execute.body().toString();
            } catch (IOException e) {

            }
        }else{
            try {
                Response<String> execute = getGeneralStringClient(IRequest.class).getSHData(code).execute();
                return  execute.body().toString();
            } catch (IOException e) {

            }
        }
        return null;
    }

}
