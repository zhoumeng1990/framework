package com.zero.framework.http;

import com.zero.framework.BuildConfig;
import com.zero.framework.cookie.JavaNetCookieJar;
import com.zero.framework.interceptor.HeaderInterceptor;
import com.zero.framework.interceptor.LogInterceptor;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.OkHttpClient;


/**
 * OkHttpClient配置
 * Created by Zero on 2017/5/25.
 */

public class MyOkHttpClient {

    private static OkHttpClient singleton;
    private MyOkHttpClient(){
        new RuntimeException("反射好玩吗？^_^");
    }
    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (MyOkHttpClient.class) {
                if (singleton == null) {

                    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
                    CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER);
                    /**
                     * 设置cookie持久化
                     */
                    okHttpClientBuilder.cookieJar(new JavaNetCookieJar(cookieManager));

                    if (BuildConfig.DEBUG) {
                        /**
                         * debug模式下打印json
                         */
                        LogInterceptor logging = new LogInterceptor();
                        okHttpClientBuilder.addInterceptor(logging);
                    }

                    okHttpClientBuilder.addInterceptor(new HeaderInterceptor());
                    singleton = okHttpClientBuilder.build();
                }
            }
        }
        return singleton;
    }
}
