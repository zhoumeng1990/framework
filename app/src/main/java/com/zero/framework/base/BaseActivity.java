package com.zero.framework.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zero.framework.R;
import com.zero.framework.utils.MyHandler;
import com.zero.framework.view.LoadingDialogFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by ZhouMeng on 2018/9/20.
 * activity父类
 */
public abstract class BaseActivity<Pre extends BasePresenter> extends AppCompatActivity implements OnClickListener {

    private static final String DIALOG_LOADING = "DialogLoading";
    private boolean mVisible;
    private LoadingDialogFragment waitDialog = null;

    protected Pre presenter;
    /**
     * 原本是打算Handler用static内部类的，内部类里面的方法和字段都必须是static，此处会有很大问题，便改为外部类去解决内存泄漏的问题
     */
    protected final Handler mHandler = new MyHandler(this);
    private Map<String, BroadcastReceiver> mapReceiver = new HashMap<>();
    private BroadcastReceiver receiver;
    private IntentFilter filter;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }

        if (initLayout() != 0) {
            /*
             * 设置布局，其实很多view注解框架都可以对layout抓取到，但还是习惯这样写，^_^
             */
            setContentView(initLayout());
            ButterKnife.bind(this);
        }

        try {
            if (getPsClass() != null) {
                if (getPsClass().newInstance() instanceof BasePresenter) {
                    /*
                     * presenter实例化，new和newInstance()不清晰，自己百度
                     */
                    presenter = (Pre) getPsClass().newInstance();
                    /*
                     * 把一些必要的数据和presenter传过去
                     */
                    presenter.initBaseData(this, mHandler, getIView(), getIntent());
                } else {
                    throw new RuntimeException("必须继承BasePresenter");
                }
            }
        } catch (InstantiationException e) {
            /*
             * 不能newInstance()导致的错误
             */
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            /*
             * 权限不足，主要是构造方法使用了private
             */
            e.printStackTrace();
        }

        initData();
        initViewAndListen();
    }

    /**
     * 传入需要过滤的action不定参数
     *
     * @param filterActions
     */
    protected void registerReceiver(@NonNull String... filterActions) {
        filter = filter == null ? new IntentFilter() : filter;
        for (String action : filterActions) {
            filter.addAction(action);
        }
        registerReceiver(filter);
    }

    /**
     * 传入filter，注册广播
     *
     * @param filter
     */
    protected void registerReceiver(@NonNull IntentFilter filter) {
        // TODO Auto-generated method stub
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                executeReceiver(context, intent);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver, filter);
        mapReceiver.put(getClass().getSimpleName(), receiver);
    }

    /**
     * 接收到广播
     *
     * @param context
     * @param intent
     */
    protected void executeReceiver(Context context, Intent intent) {

    }

    /**
     * setContentView()
     *
     * @return
     */
    abstract protected int initLayout();

    /**
     * 使用比如ButterKnife可以不使用
     */
    abstract protected void initViewAndListen();

    /**
     * 初始化简单数据，比如传过来的title
     */
    abstract protected void initData();

    /**
     * 不用多个类实现OnClickListener
     *
     * @param v
     */
    abstract protected void onclick(View v);

    /**
     * @return presenter, 此处不能使用返回Pre类型，newInstance()方法是class下的，Pre不能使用newInstance()实例化
     */
    abstract protected Class getPsClass();

    /**
     * 接口回调
     *
     * @return
     */
    abstract protected BaseInterface getIView();

    /**
     * 把发送到view层的message传递到presenter层处理，因为采用了rxjava和retrofit，
     * 很多view不在使用handler发送数据，所以没写成抽象方法
     *
     * @param msg
     */
    public void requestOver(Message msg) {
        if (presenter != null) {
            presenter.handMsg(msg);
        }
    }

    protected void to(Intent intent) {
        startActivity(intent);
    }

    protected void to(Class<?> T) {
        Intent intent = new Intent(this, T);
        to(intent);
    }

    protected void to(Class<?> T, Bundle bundle) {
        Intent intent = new Intent(this, T);
        intent.putExtras(bundle);
        to(intent);
    }

    @Override
    public void onBackPressed() {
        if (waitDialog != null) {
            hideProcessDialog();
        } else {
            super.onBackPressed();
        }
    }

    public LoadingDialogFragment showProcessDialog() {
        return showProcessDialog(R.string.loading);
    }

    public LoadingDialogFragment showProcessDialog(int resId) {
        return showProcessDialog(getString(resId));
    }

    private LoadingDialogFragment showProcessDialog(String msg) {
        if (mVisible) {
            FragmentManager fm = getSupportFragmentManager();
            if (waitDialog == null) {
                waitDialog = LoadingDialogFragment.newInstance(msg);
            }

            if (!waitDialog.isAdded()) {
                waitDialog.show(fm, DIALOG_LOADING);
            }

            return waitDialog;
        }
        return null;
    }

    public void hideProcessDialog() {
        if (mVisible && waitDialog != null) {
            try {
                waitDialog.dismiss();
                waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        onclick(v);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         * 移除mHandler，避免因为移除mHandler超activity生命周期工作造成内存泄漏
         */
        mHandler.removeCallbacksAndMessages(null);
        if (mapReceiver != null && mapReceiver.size() > 0 && mapReceiver.get(getClass().getSimpleName()) != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mapReceiver.get(getClass().getSimpleName()));
        }
    }
}
