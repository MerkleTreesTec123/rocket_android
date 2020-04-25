package com.muye.rocket.mvp.home.view;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.hjq.toast.ToastUtils;
import com.ifenduo.lib_bottomnavigation.BaseTab;
import com.ifenduo.lib_bottomnavigation.ShowHiddenNavigation;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.cache.ExcCache;
import com.muye.rocket.mvp.c2c.view.C2CHomeFragment;
import com.muye.rocket.mvp.exc_wallet.view.CapitalFragment;
import com.muye.rocket.mvp.exchange.contract.ExcUSDT2RMBContract;
import com.muye.rocket.mvp.exchange.presenter.ExcUSDT2RMBPresenter;
import com.muye.rocket.mvp.exchange.view.TradeFragment;
import com.muye.rocket.mvp.me.view.ColumnFreeActivity;
import com.muye.rocket.mvp.me.view.MeFragment;
import com.muye.rocket.tools.DataCleanManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements ExcUSDT2RMBContract.View, ShowHiddenNavigation.OnItemTabClickCallBack {
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.navigation_bar)
    ShowHiddenNavigation navigationBar;

    ExcUSDT2RMBContract.Presenter mUSDT2RMBPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        fetchUSDT2RMBRate();
        setupNavigationBar();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(new Intent(this, SystemTimeService.class));
//        } else {
//            startService(new Intent(this, SystemTimeService.class));
//        }
    }

    private void setupNavigationBar() {
        navigationBar.addTab(R.drawable.ic_nav_home_unselected, R.drawable.ic_nav_home_selected, getResources().getColor(R.color.colorC5D7D2),
                getResources().getColor(R.color.color01AA78), getString(R.string.home));
        navigationBar.addTab(R.drawable.ic_nav_c2c_unselected, R.drawable.ic_nav_c2c_selected, getResources().getColor(R.color.colorC5D7D2),
                getResources().getColor(R.color.color01AA78), R.string.c2c);
        navigationBar.addTab(R.drawable.action_rocket_normal, R.drawable.action_rocket_select, getResources().getColor(R.color.colorC5D7D2),
                getResources().getColor(R.color.color01AA78),  R.string.trade);
        navigationBar.addTab(R.drawable.ic_nav_discover_unselected, R.drawable.ic_nav_discover_selected, getResources().getColor(R.color.colorC5D7D2),
                getResources().getColor(R.color.color01AA78), R.string.capital);
        navigationBar.addTab(R.drawable.ic_nav_me_unselected, R.drawable.ic_nav_me_selected, getResources().getColor(R.color.colorC5D7D2),
                getResources().getColor(R.color.color01AA78), R.string.me);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(C2CHomeFragment.newInstance());

        fragmentList.add(TradeFragment.newInstance());
//        fragmentList.add(SpacePlanFragment.newInstance());
//        fragmentList.add(DiscoverFragment.newInstance());
        fragmentList.add(CapitalFragment.newInstance());

        fragmentList.add(MeFragment.newInstance());
        navigationBar.setupFragments(getSupportFragmentManager(), R.id.fl_container, fragmentList);
        navigationBar.setOnItemTabClickCallBack(this);
        navigationBar.setCheckedTab(0);
    }

    private void fetchUSDT2RMBRate() {
        if (mUSDT2RMBPresenter == null) {
            mUSDT2RMBPresenter = new ExcUSDT2RMBPresenter(this);
        }
        mUSDT2RMBPresenter.fetchUSDT2RMBRate();

    }

    @Override
    public void bindUSDT2RMBRate(double rate) {
        ExcCache.saveUsdt2RmbCache(rate);
    }


    @OnClick({R.id.action_button})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.action_button:
                Intent intent = new Intent(this, ColumnFreeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mUSDT2RMBPresenter != null) {
            mUSDT2RMBPresenter.dropView();
        }
        super.onDestroy();
    }

    public void showC2C() {
        if (navigationBar != null) {
            navigationBar.setCheckedTab(1);
        }
    }

    public void showTrade() {
        if (navigationBar != null) {
            navigationBar.setCheckedTab(2);
        }
    }

    public void showIEO() {
        if (navigationBar != null) {
            navigationBar.setCheckedTab(3);
        }
    }

    //用于计算连点返回倒计时
    private long exitTime = 0;

    @Override
    protected void onClickBack() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.show(R.string.exit_hint);
            exitTime = System.currentTimeMillis();
        } else {
            super.onClickBack();
            System.exit(0);
        }

    }

    @Override
    public boolean onItemTabClick(int index, BaseTab tab) {
        if (index == 2) {
            DataCleanManager.clearAllCache(this);
        }
        return false;
    }

}
