package com.sn.sinanews.tools;

import com.sn.sinanews.services.BaseService;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Ming on 2016/1/7.
 */
public class HttpUtils {
    private static BaseService service;
    static {
        service = new Retrofit.Builder().baseUrl("http://api.sina.cn")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(BaseService.class);
    }
    public static BaseService getService() {
        return service;
    }
}
