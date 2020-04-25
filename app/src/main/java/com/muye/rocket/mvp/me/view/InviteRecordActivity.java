package com.muye.rocket.mvp.me.view;

import android.os.Bundle;

import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.R;

/*
* 个人推荐记录
* */
public class InviteRecordActivity extends BaseActivity {
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.invote_record_layout;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.personal_direct_push_record));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,InviteRecordFragment.newInstance()).commit();
    }
}
