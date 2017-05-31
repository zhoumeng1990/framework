package com.zero.framework.base;


import com.google.gson.Gson;
import com.zero.framework.exception.APIException;
import com.zero.framework.exception.RequestExpiredException;
import com.zero.framework.exception.UnLoginException;
import com.zero.framework.interfaces.IResponse;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Observer的封装
 * Created by Zero on 2017/5/28.
 */

public class BaseObserver<T> implements Observer<ResponseBody> {

    private IResponse iResponse;
    private Gson mGson;
    private final Type finalNeedType;
    private static final int UNLOGIN_EXCEPTION = 33333;
    private static final int REQUEST_EXCEPTION = 1003;

    public BaseObserver(IResponse<T> iResponse) {
        this.iResponse = iResponse;
        mGson = new Gson();

        final Type[] types = iResponse.getClass().getGenericInterfaces();

        if (MethodHandler(types) == null || MethodHandler(types).size() == 0) {

        }
        finalNeedType = MethodHandler(types).get(0);
    }

    private List<Type> MethodHandler(Type[] types) {
        List<Type> needTypes = new ArrayList<>();

        for (Type paramType : types) {
            if (paramType instanceof ParameterizedType) {
                Type[] parenTypes = ((ParameterizedType) paramType).getActualTypeArguments();
                for (Type childType : parenTypes) {
                    needTypes.add(childType);
                    if (childType instanceof ParameterizedType) {
                        Type[] childTypes = ((ParameterizedType) childType).getActualTypeArguments();
                        for (Type type : childTypes) {
                            needTypes.add(type);
                        }
                    }
                }
            }
        }
        return needTypes;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ResponseBody responseBody) {

        try {
            /**
             * responseBody.string()当前打断点，获取不到值，具体原因还未去查找，此处先用result接收
             */
            String result = responseBody.string();
            BaseResponse httpResponse = mGson.fromJson(result,finalNeedType);
            if (httpResponse.isSuccess()) {
                iResponse.onSuccess(httpResponse);
            } else {
                if (httpResponse.getCode() == UNLOGIN_EXCEPTION) {
                    iResponse.onError(new UnLoginException(httpResponse.getCode(), httpResponse.getMessage()));
                } else if (httpResponse.getCode() == REQUEST_EXCEPTION) {
                    iResponse.onError(new RequestExpiredException(httpResponse.getCode(), httpResponse.getMessage()));
                } else {
                    iResponse.onError(new APIException(httpResponse.getCode(), httpResponse.getMessage()));
                }
            }
        } catch (IOException e) {
            iResponse.onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        iResponse.onError(e);
    }

    @Override
    public void onComplete() {

    }
}
