package com.muye.rocket.mvp.me.view;

import android.content.Intent;
import android.os.Bundle;

import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.R;
import com.muye.rocket.event.BindGoogleSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

public class BindGoogleStep1Activity extends BaseActivity {
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_bind_google_step_1;
    }


    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.google_authenticator));
        EventBus.getDefault().register(this);
    }


    @OnClick(R.id.submit_button)
    public void onViewClicked() {
        Intent intent = new Intent(this, BindGoogleStep2Activity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindGoogleSuccess(BindGoogleSuccessEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
