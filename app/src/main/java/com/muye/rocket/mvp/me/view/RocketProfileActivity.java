package com.muye.rocket.mvp.me.view;

import android.os.Bundle;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

public class RocketProfileActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.rocket_profile;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.rocket_));
    }
}
