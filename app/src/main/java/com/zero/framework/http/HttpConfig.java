package com.zero.framework.http;

import com.zero.framework.BuildConfig;

/**
 * 配置baseUrl
 * Created by Zero on 2017/5/25.
 */
public class HttpConfig {

    //服务器地址
    private static final String DEBUG_SERVER = "https://api.douban.com/v2/movie/";
    private static final String OFFICIAL_SERVER = "http://xxx.com/interface.php?";

    public static String getServer() {
        if (BuildConfig.DEBUG) {
            return baseUrl(DEBUG_SERVER);
        } else {
            return baseUrl(OFFICIAL_SERVER);
        }
    }

    private static String baseUrl(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl;
    }
}
