package com.zero.framework.interfaces;

/**
 * Created by Zero on 2017/5/25.
 */

public interface IResponse<T> {

    void onSuccess(T baseModel);

    void onError(Throwable e);
}
