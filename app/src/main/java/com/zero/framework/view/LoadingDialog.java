package com.zero.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义dialog，用于显示数据请求过程中的loading框
 * Created by Zero on 2017/5/27.
 */

public class LoadingDialog extends Dialog implements OnKeyListener {
    public LoadingDialog(Context context) {
        super(context);

        LinearLayout ll = new LinearLayout(context);
        ll.setGravity(Gravity.CENTER);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setPadding(10, 10, 10, 10);

        TextView textView = new TextView(context);
        textView.setPadding(10, 10, 10, 10);

        ll.addView(progressBar);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(ll, lp);

        this.setCanceledOnTouchOutside(false);
        this.setOnKeyListener(this);
    }

    public LoadingDialog(Context context, String text) {
        super(context);

        LinearLayout ll = new LinearLayout(context);
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setPadding(40, 40, 40, 40);

        TextView textView = new TextView(context);
        textView.setPadding(40, 40, 40, 40);

        ll.addView(progressBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
            ll.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(ll, lp);

        this.setCanceledOnTouchOutside(false);
        this.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            this.dismiss();
        }
        return false;
    }
}
