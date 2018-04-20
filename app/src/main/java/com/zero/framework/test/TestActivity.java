package com.zero.framework.test;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.framework.R;
import com.zero.framework.base.BaseActivity;
import com.zero.framework.base.BaseInterface;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by apple on 2017/5/27.
 */

public class TestActivity extends BaseActivity<TestPresenter> implements ITest{
    private static final String TAG = "TestActivity";
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.img_header)
    ImageView imgHeader;
    Map map;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndListen() {
        txtLogin.setOnClickListener(this);
        imgHeader.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onclick(View v) {
        switch (v.getId()){
            case R.id.txt_login:
                presenter.commit();
                break;
            case R.id.img_header:
                presenter.getCommit();
                break;
        }
    }

    @Override
    protected Class getPsClass() {
        return TestPresenter.class;
    }

    @Override
    protected BaseInterface getIView() {
        return this;
    }

    @Override
    public void showLoading() {
        Log.e(TAG, "showLoading: ");
    }

    @Override
    public void hideLoading() {
        Log.e(TAG, "hideLoading: ");
    }

    @Override
    public void sendData(TestModel model) {
        System.out.println(model.title);
    }
}
