package com.muye.rocket.mvp.ieo.view;

import android.os.Bundle;

import com.ifenduo.common.tool.StatusBarTool;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

public class SpacePlanActivity extends BaseActivity {
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_space_plan;
    }

    @Override
    protected int getNavigationIcon() {
        return com.ifenduo.lib_base.R.drawable.rc_back_icon_white;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        StatusBarTool.setTranslucentForImageView(this,mToolBarContainer);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, SpacePlanFragment.newInstance()).commit();
    }
}
