package com.muye.rocket.mvp.ieo.view;


import android.os.Bundle;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

/**
 * 我的IEO
 */
public class MyIeoActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_ieo;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.my_record), getResources().getColor(R.color.color262626));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout,MyIeoListFragment.newInstance())
                .commit();
    }
}
