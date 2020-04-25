package com.muye.rocket.mvp.c2c.view;

import android.os.Bundle;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

public class C2CWalletActivity extends BaseActivity {
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_wallet;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_wallet));

        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, C2CWalletFragment.newInstance()).commit();
    }
}
