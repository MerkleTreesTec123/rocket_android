package com.muye.rocket.mvp.c2c.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class OrderListActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    CommonTabLayout tabLayout;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_order_list;
    }


    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_order_record));
        setupTabLayout();
    }

    private void setupTabLayout() {
        ArrayList<CustomTabEntity> entityList=new ArrayList<>();
        entityList.add(new TabEntity(getString(R.string.c2c_buy)));
        entityList.add(new TabEntity(getString(R.string.c2c_sell)));

        ArrayList<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(OrderListFragment.newInstance(Constant.C2C_TRANSACTION_TYPE_BUY));
        fragmentList.add(OrderListFragment.newInstance(Constant.C2C_TRANSACTION_TYPE_SELL));
        tabLayout.setTabData(entityList,this,R.id.fl_container,fragmentList);
    }

}
