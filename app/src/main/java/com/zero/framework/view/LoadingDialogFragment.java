package com.zero.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;

/**
 * 用于在Activity的内容之上展示一个对话框
 * 使用DialogFragment来管理对话框，当旋转屏幕和按下后退键时可以更好的管理其声明周期，它和Fragment有着基本一致的生命周期
 * Created by Zero on 2017/5/27.
 */

public class LoadingDialogFragment extends DialogFragment {
    private static final String ARG_CONTENT = "content";

    public static LoadingDialogFragment newInstance(String content) {
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(content)) {
            args.putString(ARG_CONTENT, content);
        }

        LoadingDialogFragment fragment = new LoadingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LoadingDialog dialog;
        Bundle args = getArguments();
        Context context = getContext();

        if (args.containsKey(ARG_CONTENT)) {
            String content = args.getString(ARG_CONTENT);
            dialog = new LoadingDialog(context, content);
        } else {
            dialog = new LoadingDialog(context);
        }
        return dialog;
    }
}
