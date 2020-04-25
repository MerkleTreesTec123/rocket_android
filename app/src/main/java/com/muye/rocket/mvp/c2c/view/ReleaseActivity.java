package com.muye.rocket.mvp.c2c.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ReleaseActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    CommonTabLayout tabLayout;

    int mShowPageIndex;
    String mCoinID;

    public static void openReleaseActivity(Context context, int showPageIndex, String coinID) {
        Intent intent = new Intent(context, ReleaseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("showPageIndex", showPageIndex);
        bundle.putString("coinID", coinID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_release;
    }


    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setupTabLayout();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mShowPageIndex = bundle.getInt("showPageIndex", 0);
            mCoinID = bundle.getString("coinID", "");
        }
    }

    private void setupTabLayout() {
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        tabEntityList.add(new TabEntity(getString(R.string.c2c_buy_in)));
        tabEntityList.add(new TabEntity(getString(R.string.c2c_sell_out)));

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(ReleaseBuyFragment.newInstance(mCoinID));
        fragmentList.add(ReleaseSellFragment.newInstance(mCoinID));
        tabLayout.setTabData(tabEntityList, this, R.id.fl_container, fragmentList);
        tabLayout.setCurrentTab(mShowPageIndex);
    }

    @OnClick(R.id.back_button)
    public void onViewClicked() {
        onClickBack();
    }
}
