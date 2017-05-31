package com.zero.framework.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 在retrofit使用中，可以通过@Header来实现Header
 * Created by Zero on 2017/5/27.
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("User-Agent", "Zero")
                .build();
        return chain.proceed(request);
    }
}