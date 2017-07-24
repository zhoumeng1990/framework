package com.zero.framework.utils;

import android.os.Handler;
import android.os.Message;

import com.zero.framework.base.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * Created by apple on 2017/7/24.
 */

public class MyHandler extends Handler {
    private final WeakReference<BaseActivity> mActivity;

    /**
     * 从BaseActivity中提取出来，原来是因为内部类会隐式强引用当前类，采用弱引用，避免长生命周期导致内存泄漏
     *
     * @param activity
     */
    public MyHandler(BaseActivity activity) {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mActivity.get() != null) {
            mActivity.get().requestOver(msg);
        }
    }
}
