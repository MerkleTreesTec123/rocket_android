/*
package com.muye.rocket.mvp.discover.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ifenduo.common.tool.DimensionTool;
import com.ms.banner.Banner;
import com.ms.banner.BannerConfig;
import com.ms.banner.Transformer;
import com.ms.banner.listener.OnBannerClickListener;
import com.muye.rocket.CustomViewHolder;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseFragment;
import com.muye.rocket.entity.DiscoverBannerEntity;
import com.muye.rocket.entity.DiscoverEntity;
import com.muye.rocket.entity.DiscoverHomeEntity;
import com.muye.rocket.mvp.WebBrowser;
import com.muye.rocket.mvp.discover.contract.DiscoverHomeContract;
import com.muye.rocket.mvp.discover.presenter.DiscoverHomePresenter;
import com.muye.rocket.widget.CoinRefreshHeader;
import com.muye.rocket.widget.dialog.DappDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DiscoverFragment extends BaseFragment implements OnRefreshListener, ViewPager.OnPageChangeListener, DiscoverHomeContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;

    View mBannerItemView;
    Banner mBannerView;
    List<DiscoverBannerEntity> mBannerData;
    LinearLayout mIndicatorContainer;
    DiscoverAdapter mDiscoverAdapter;
    DiscoverHomeContract.Presenter mHomePresenter;


    public static DiscoverFragment newInstance() {
        Bundle args = new Bundle();
        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewLayout() {
        return R.layout.fragment_discover;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
        }
    }

    @Override
    protected void initView(View parentView) {
        unbinder = ButterKnife.bind(this, parentView);
        CoinRefreshHeader refreshHeader = new CoinRefreshHeader(getContext());
        refreshLayout.setRefreshHeader(refreshHeader);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(this);
        buildDiscoverAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mDiscoverAdapter);
        initBannerView();
    }

    private void setupBannerView() {
        int screenWidth = DimensionTool.getScreenWidth(getContext());
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mBannerView.getLayoutParams();
        layoutParams.height = (int) (188f / 343f * screenWidth);
        mBannerView.setLayoutParams(layoutParams);

        //设置banner样式
        mBannerView.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
//        mBannerView.setImageLoader(new BannerImageLoader());
        //设置banner动画效果
        mBannerView.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBannerView.setAutoPlay(true);
        //设置轮播时间
        mBannerView.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBannerView.setIndicatorGravity(BannerConfig.CENTER);

        mBannerView.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onBannerClick(List datas, int position) {
                if (mBannerData != null && mBannerData.size() > position) {
                    DiscoverBannerEntity bannerEntity = mBannerData.get(position);
                    if (bannerEntity != null) {
                        showMessageDialog(bannerEntity.getTitle(), bannerEntity.getLianjie());
                    }
                }
            }
        });

        mBannerView.setOnPageChangeListener(this);
    }

    private void setupIndicator() {
        int mDp2px = DimensionTool.dp2px(getContext(), 8);
        mIndicatorContainer.removeAllViews();
        if (mBannerData != null) {
            for (int i = 0; i < mBannerData.size(); i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.selector_indicator_discover));
                LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i != mBannerData.size() - 1) {
                    layoutParams.rightMargin = mDp2px;
                } else {
                    layoutParams.rightMargin = 0;
                }
                imageView.setLayoutParams(layoutParams);
                mIndicatorContainer.addView(imageView);
            }
        }
    }


    @Override
    protected void initData() {
        refreshLayout.autoRefresh();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mHomePresenter == null) {
            mHomePresenter = new DiscoverHomePresenter(this);
        }
        mHomePresenter.fetchBanner();
        mHomePresenter.fetchRecommend();
        mHomePresenter.fetchHomeList();
    }

    private void initBannerView() {
        if (mBannerItemView == null) {
            mBannerItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_discover_banner, recyclerView, false);
            mBannerView = mBannerItemView.findViewById(R.id.banner_view);
            mIndicatorContainer = mBannerItemView.findViewById(R.id.indicator_container);
            mBannerView.setOnPageChangeListener(DiscoverFragment.this);
            setupBannerView();
        }
    }

    private void buildDiscoverAdapter() {
        mDiscoverAdapter = new DiscoverAdapter(getContext()) {
            @Override
            View getBannerView() {
                initBannerView();
                return mBannerItemView;
            }
        };
    }


    @Override
    public void bindBanner(List<DiscoverBannerEntity> bannerList) {
        List<String> imageList = new ArrayList<>();
        mBannerData = new ArrayList<>();
        if (bannerList != null && bannerList.size() > 0) {
            for (int i = 0; i < bannerList.size(); i++) {
                DiscoverBannerEntity bannerEntity = bannerList.get(i);
                if (bannerEntity != null) {
                    imageList.add(bannerEntity.getThumb());
                    mBannerData.add(bannerEntity);
                }
            }
        }
        setupIndicator();
        mDiscoverAdapter.setBannerData(imageList);
        mBannerView.setPages(imageList, new CustomViewHolder());
        mBannerView.update(imageList);
        mBannerView.startAutoPlay();
    }

    @Override
    public void binRecommend(List<DiscoverEntity> recommendData) {
        mDiscoverAdapter.setRecommendData(recommendData);
    }

    @Override
    public void bindHomeList(List<DiscoverHomeEntity> homeEntityList) {
        refreshLayout.finishRefresh();
        mDiscoverAdapter.setDiscoverHomeData(homeEntityList);
    }

    DappDialog mDappDialog;
    private void showMessageDialog(String name, String url) {
        if(mDappDialog==null){
            mDappDialog=new DappDialog(getContext());
        }
        mDappDialog.setDappName(name);
        mDappDialog.setOnOkButtonClickListener(new DappDialog.OnOkButtonClickListener() {
            @Override
            public void onOkButtonClick(DappDialog dialog) {
                dialog.dismiss();
                WebBrowser.openWebBrowser(getContext(), url, name);
            }
        });
        if(!mDappDialog.isShowing()){
            mDappDialog.show();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        int indicatorCount = mIndicatorContainer.getChildCount();
        if (indicatorCount > i) {
            for (int j = 0; j < indicatorCount; j++) {
                if (mIndicatorContainer.getChildAt(j) != null) {
                    mIndicatorContainer.getChildAt(j).setSelected(j == i);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBannerView != null) {
            mBannerView.startAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBannerView != null) {
            mBannerView.stopAutoPlay();
        }
    }

    @Override
    public void onDestroyView() {
        if (mHomePresenter != null) {
            mHomePresenter.dropView();
        }
        super.onDestroyView();
        unbinder.unbind();
    }


}
*/
