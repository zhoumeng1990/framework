package com.zero.framework.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 很多注释写在了NewBaseActivity
 */
public abstract class BaseFragment<Presenter extends BasePresenter> extends Fragment implements OnClickListener {
    protected Presenter fragmentPresenter;
    protected View rootView;
    // 用于因为调用基类的getActivity为空的情况
    protected Activity mActivity;
    protected Context mContext;

    protected WeakReference<Activity> weakActivity;

    protected final Handler mHandler = new MyHandler(mActivity);
    protected Unbinder unbinder;

    private class MyHandler extends Handler {

        private MyHandler(Activity activity) {
            weakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakActivity.get() != null) {
                requestOver(msg);
            }
        }
    }

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            if (getPsClass() != null) {
                if(getPsClass().newInstance() instanceof BasePresenter){
                    fragmentPresenter = (Presenter) getPsClass().newInstance();
                    fragmentPresenter.initBaseData(getActivity(), mHandler, getIView(), getActivity().getIntent());
                }else{
                    throw new RuntimeException(getPsClass().getName() +"必须继承BasePresenter");
                }
            }
        } catch (java.lang.InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (initLayout() != 0) {
            rootView = inflater.inflate(initLayout(), container, false);
            unbinder = ButterKnife.bind(this,rootView);
        }else{
            throw new NullPointerException("未找到布局文件");
        }
        initData(rootView);
        return rootView;
    }

    protected void requestOver(Message msg) {
        fragmentPresenter.handMsg(msg);
    }

    abstract protected int initLayout();

    /**
     * 全部使用注解抓取资源和设置监听
     */
    abstract protected void initData(View rootView);

    abstract protected Class getPsClass();

    abstract protected BaseInterface getIView();

    abstract protected void clickView(View v);

    protected void to(Intent intent){
        startActivity(intent);
    }

    protected void to(Class<?> T) {
        Intent intent = new Intent(mActivity, T);
        to(intent);
    }

    protected void to(Class<?> T, Bundle bundle) {
        Intent intent = new Intent(mActivity, T);
        intent.putExtras(bundle);
        to(intent);
    }

    /**
     * 未删除过期方法主要目的是因为在api19以下会走这个方法，而不是下面那个方法
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mActivity = getActivity();
        mContext = activity;
    }

    /**
     * Api19及以后才会走这个方法
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        clickView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
