package com.zero.framework.http;


import android.net.Uri;

import com.zero.framework.base.BaseObserver;
import com.zero.framework.cache.CacheManager;
import com.zero.framework.cache.CacheObject;
import com.zero.framework.interfaces.IResponse;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 请求的封装入口
 * Created by Zero on 2017/5/25.
 */

public class RequestUtil {
    /**
     * get方式处理
     *
     * @param url
     * @param map
     * @param iResponse
     * @param <T>
     */
    public static <T> Observable<ResponseBody> getDispose(String url, Map map, final IResponse<T> iResponse) {
        Observable<ResponseBody> observable = RetrofitFactory.getInstance().executeGet(url, map);
        return getObservable(observable, iResponse, null);
    }

    private static <T> Observable<ResponseBody> getDispose(String url, Map map, final IResponse<T> iResponse, Map cacheMap) {
        Observable<ResponseBody> observable = RetrofitFactory.getInstance().executeGet(url, map);
        return getObservable(observable, iResponse, cacheMap);
    }

    /**
     * 自定义ApiService
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getCutomService(Class<T> clazz) {
        return RetrofitFactory.createRetrofitService(clazz);
    }

    /**************************************post**************************************/

    public static <T> void postDispose(String url, Map map, final IResponse<T> iResponse) {

        Observable<ResponseBody> observable = RetrofitFactory.getInstance().executePost(url, map);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<>(iResponse));
    }

    private static <T> Observable<ResponseBody> postDispose(String url, Map map, final IResponse<T> iResponse, Map cacheMap) {
        Observable<ResponseBody> observable = RetrofitFactory.getInstance().executePost(url, map);
        return getObservable(observable, iResponse, cacheMap);
    }

    /**
     * 获取Observable对象，
     * 此处名称的get为获取的意思，不是数据请求方式
     * @param observable
     * @param iResponse
     * @param cacheMap
     * @param <T>
     * @return
     */
    private static <T> Observable<ResponseBody> getObservable(Observable<ResponseBody> observable, IResponse<T> iResponse, Map cacheMap) {
        if (cacheMap != null && cacheMap.size() > 0) {
            CacheManager.addData(cacheMap.get("cacheKey").toString(), observable, (int) cacheMap.get("period"));
        }
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseObserver<>(iResponse));
        return observable;
    }


    /**************************************cache**************************************/

    private static <T> void cacheData(String url, Map map, final IResponse<T> iResponse, int period, boolean isGet){
        String cacheKey = url + getCacheKey(map);
        CacheObject data = CacheManager.getData(cacheKey);
        if (data == null) {
            Map cacheMap = new HashMap();
            cacheMap.put("cacheKey", cacheKey);
            cacheMap.put("period", period);
            if (isGet) {
                getDispose(url, map, iResponse, cacheMap);
            }else{
                postDispose(url, map, iResponse, cacheMap);
            }
        } else {
            Map cacheMap = new HashMap();
            cacheMap.put("cacheKey", cacheKey);
            cacheMap.put("period", period);
            if (isGet) {
                getDispose(url, map, iResponse, cacheMap);
            }else{
                postDispose(url, map, iResponse, cacheMap);
            }
        }
    }

    /**
     * get方式请求，需要做本地cache，不设置缓存类型，默认-1（长久缓存）
     */
    public static <T> void getDisposeWithCache(String url, Map map, final IResponse<T> iResponse) {
        getDisposeWithCache(url,map,iResponse,-1);
    }

    /**
     * get方式请求，需要做本地cache
     */
    public static <T> void getDisposeWithCache(String url, Map map, final IResponse<T> iResponse, int period) {
        cacheData(url,map,iResponse,period,true);
    }

    /**
     * post方式请求，需要做本地cache，不设置缓存类型，默认-1（长久缓存）
     */
    public static <T> void postDisposeWithCache(String url, Map map, final IResponse<T> iResponse) {
        postDisposeWithCache(url,map,iResponse,-1);
    }

    /**
     * post方式请求，需要做本地cache
     */
    public static <T> void postDisposeWithCache(String url, Map map, final IResponse<T> iResponse, int period) {
        cacheData(url,map,iResponse,period,false);
    }

    private static String getCacheKey(Map param) {
        if (param == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer("");
        TreeMap treeMapParams = new TreeMap(param);
        for (Object key : treeMapParams.keySet()) {
            /**
             * 过滤掉token，根据自己需要
             */
            if (!key.toString().equals("token")) {
                sb.append(key).append("=").append(Uri.encode(treeMapParams.get(key).toString()));
            }
        }
        return sb.toString();
    }


    @Deprecated
    private static <T> Observable<T> test(String url, Map map, Class<?> clazz, String methodName) {
        Observable<T> tObservable = null;
        try {
            /**
             * 第一个参数是反射的方法名，第二个参数是方法里面参数类型
             */
            Method post = clazz.getDeclaredMethod(methodName,new Class[]{String.class,Map.class});
            if(post!=null){
                tObservable =  (Observable<T>) post.invoke(RetrofitFactory.createRetrofitService(clazz),new Object[]{url,map});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tObservable;
//        return RetrofitFactory.createRetrofitService(clazz);
    }
}
