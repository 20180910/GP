package com.gp.module.main.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/6/28.
 */

public interface IRequest {
    @GET("q=sz{code}")
    Call<String> getSZData(@Path("code") String code);

    @GET("q=sh{code}")
    Call<String> getSHData(@Path("code") String code);
}
