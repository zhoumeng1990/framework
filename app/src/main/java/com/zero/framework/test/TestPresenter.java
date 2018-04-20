package com.zero.framework.test;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.zero.framework.base.BaseInterface;
import com.zero.framework.base.BasePresenter;
import com.zero.framework.http.RequestUtil;
import com.zero.framework.interfaces.IResponse;

import java.util.HashMap;

/**
 * Created by Zero on 2017/5/31.
 */

public class TestPresenter extends BasePresenter {

    private ITest iView;

    @Override
    protected void initBaseData(Context context, Handler handler, BaseInterface iView, Intent intent) {
        if (iView != null) {
            if (iView instanceof ITest) {
                this.iView = (ITest) iView;
            }
        }
    }

    @Override
    protected void handMsg(Message msg) {

    }

    public void commit() {

    }

    public void getCommit() {
        RequestUtil.getDispose("top250", new HashMap(), new IResponse<TestModel>() {

            @Override
            public void onSuccess(TestModel baseModel) {
                iView.sendData(baseModel);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
