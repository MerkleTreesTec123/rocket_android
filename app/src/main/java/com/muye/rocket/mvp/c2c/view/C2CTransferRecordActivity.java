package com.muye.rocket.mvp.c2c.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class C2CTransferRecordActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    CommonTabLayout tabLayout;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_transfer_record;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_transfer_record));
        setupTabLayout();
    }

    private void setupTabLayout() {
        ArrayList<CustomTabEntity> tabList = new ArrayList<>();
        tabList.add(new TabEntity(getString(R.string.c2c_all)));
        tabList.add(new TabEntity(getString(R.string.c2c_assets_to_c2c_wallet)));
        tabList.add(new TabEntity(getString(R.string.c2c_wallet_to_assets)));

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(C2CTransferRecordFragment.newInstance("90,91"));
        fragmentList.add(C2CTransferRecordFragment.newInstance("90"));
        fragmentList.add(C2CTransferRecordFragment.newInstance("91"));

        tabLayout.setTabData(tabList, this, R.id.fl_container, fragmentList);
        tabLayout.setCurrentTab(0);
    }
}
