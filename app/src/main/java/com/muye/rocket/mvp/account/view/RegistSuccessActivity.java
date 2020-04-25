package com.muye.rocket.mvp.account.view;

import android.os.Bundle;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import butterknife.OnClick;

/*
 * 注册成功
 * */
public class RegistSuccessActivity extends BaseActivity {
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_regist_success;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        openLogin();//跳转登录
    }

    /*
    * 禁用返回
    * */
    @Override
    protected void onClickBack() {
    }
}
