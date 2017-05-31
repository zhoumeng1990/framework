package com.zero.framework.http;


import android.text.TextUtils;

import com.zero.framework.api.ApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 此类主要是对retrofit进行配置
 * Created by Zero on 2017/5/26.
 */

public class RetrofitFactory {

    private RetrofitFactory() {
        new RuntimeException("反射个毛线，好玩吗？");
    }

    private static OkHttpClient httpClient = MyOkHttpClient.getInstance();

    private static ApiService retrofitService;

    private static String baseUrl = "";

    private static Retrofit retrofit;

    /**
     * 默认为ApiService
     *
     * @return
     */
    public static ApiService getInstance() {
        if (retrofitService == null) {
            synchronized (RetrofitFactory.class) {
                if (retrofitService == null) {
                    retrofitService = getInstanceRetrofit().create(ApiService.class);
                }
            }
        }
        return retrofitService;
    }

    /**
     * baseUrl
     */
    private static void getBaseUrl() {
        baseUrl = HttpConfig.getServer();
    }

    private static Retrofit getInstanceRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitFactory.class) {
                if (retrofit == null) {
                    if (TextUtils.isEmpty(baseUrl)) {
                        getBaseUrl();
                    }

                    retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(httpClient)
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * 用于创建自定义的apiService
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createRetrofitService(final Class<T> clazz) {
        return getInstanceRetrofit().create(clazz);
    }
}
