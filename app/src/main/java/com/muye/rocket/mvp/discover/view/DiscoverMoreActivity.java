package com.muye.rocket.mvp.discover.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.DiscoverCat;
import com.muye.rocket.mvp.discover.contract.DiscoverCatContract;
import com.muye.rocket.mvp.discover.presenter.DiscoverCatPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DiscoverMoreActivity extends BaseActivity implements DiscoverCatContract.View {
    @BindView(R.id.tab_layout)
    CommonTabLayout tabLayout;
    DiscoverCatContract.Presenter mPresenter;

    String mCatID;

    public static void openDiscoverMoreActivity(Context context, String catID) {
        Intent intent = new Intent(context, DiscoverMoreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("catID", catID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_discover_more;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.list));
        mPresenter = new DiscoverCatPresenter(this);
        mPresenter.fetchCatList();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCatID = bundle.getString("catID", "");
        }
    }

    private void setupTabLayout(List<DiscoverCat> catList) {
        int startIndex = 0;
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        ArrayList<Fragment> fragmentList = new ArrayList<>();

        tabEntityList.add(new TabEntity(getString(R.string.all)));
        fragmentList.add(DiscoverMoreFragment.newInstance(""));

        if (catList != null && catList.size() > 0) {
            for (int i = 0; i < catList.size(); i++) {
                DiscoverCat discoverCat = catList.get(i);
                if (discoverCat != null) {
                    fragmentList.add(DiscoverMoreFragment.newInstance(discoverCat.getId()));
                    tabEntityList.add(new TabEntity(discoverCat.getName()));

                    if (!TextUtils.isEmpty(mCatID) && mCatID.equals(discoverCat.getId())) {
                        startIndex = fragmentList.size() - 1;
                    }
                }
            }
        }
        tabLayout.setTabData(tabEntityList, this, R.id.fl_container, fragmentList);
        tabLayout.setCurrentTab(startIndex);
    }


    @Override
    public void bindCatList(List<DiscoverCat> catList) {
        List<DiscoverCat> catList_ = new ArrayList<>();
        if (catList != null && catList.size() > 0) {
            for (int i = 0; i < catList.size(); i++) {
                DiscoverCat discoverCat = catList.get(i);
                if (discoverCat != null) {
                    catList_.add(discoverCat);
                }
            }
        }
        setupTabLayout(catList_);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        super.onDestroy();
    }


}
