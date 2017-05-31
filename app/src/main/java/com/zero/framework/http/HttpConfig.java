package com.zero.framework.http;

/**
 * 配置baseUrl
 * Created by Zero on 2017/5/25.
 */
public class HttpConfig {

    //服务器地址
    private static final boolean isDebug=true;
    private static final String DEBUG_SERVER="https://xxx.com:61001/";
    private static final String OFICAL_SERVER="http://xxx.com/";

    public static String getServer()
    {
        if(isDebug)
        {
            return baseUrl(DEBUG_SERVER);
        }
        else {
            return baseUrl(OFICAL_SERVER);
        }
    }

    private static String baseUrl(String baseUrl){
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl;
    }
}
