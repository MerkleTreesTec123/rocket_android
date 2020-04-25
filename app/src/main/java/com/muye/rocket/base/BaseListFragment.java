package com.muye.rocket.base;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ifenduo.common.adapter.CommonAdapter;
import com.ifenduo.common.adapter.MultiItemTypeAdapter;
import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.adapter.wrapper.HeaderAndFooterWrapper;
import com.ifenduo.common.view.NormalLoadFootView;
import com.ifenduo.common.view.RecyclerViewExtension;
import com.ifenduo.lib_base.R;
import com.muye.rocket.widget.CoinRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangxuefeng on 16/10/25.
 */

public abstract class BaseListFragment<T> extends BaseFragment implements MultiItemTypeAdapter.OnItemClickListener,
        RecyclerViewExtension.LoadingListener, SwipeRefreshLayout.OnRefreshListener, OnRefreshListener {

    public final static int DEFAULT_START_PAGE = 1;

    private List<T> mDataSource = new ArrayList<>();
    protected boolean isRefresh;
    protected boolean isLoadMore;
    private int loadPage = DEFAULT_START_PAGE;

    private FrameLayout mContainer;
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerViewExtension mRecyclerView;
    protected NormalLoadFootView mLoadFootView;
//    private View mEmptyPlaceholderView;

    public SmartRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public RecyclerViewExtension getRecyclerView() {
        return mRecyclerView;
    }

    protected HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    protected CommonAdapter<T> mCommonAdapter;

    @Override
    protected int getRootViewLayout() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initView(View parentView) {
        mContainer = parentView.findViewById(R.id.fl_view_container_base_fragment);
        mRefreshLayout = parentView.findViewById(R.id.swipe_refresh_base_fragment);
        mRecyclerView = parentView.findViewById(R.id.recycler_view_base_fragment);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(getAdapter());
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setTriggerLoad(isSupportLoadMore());
        mRefreshLayout.setEnabled(isSupportRefresh());
        mRefreshLayout.setOnRefreshListener(this);
        CoinRefreshHeader refreshHeader = new CoinRefreshHeader(getContext());
        mRefreshLayout.setRefreshHeader(refreshHeader);
        mRefreshLayout.setEnableLoadMore(false);


        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null)
            mRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void initData() {
//        forceRefresh();
        onRefresh();
    }

    public void forceRefresh() {
//        if (null == mRefreshLayout) return;
//        if (mRefreshLayout.isRefreshing()) return;
//        mRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.setRefreshing(true);
//                onRefresh();
//            }
//        });

        if (null == mRefreshLayout) return;
        if (mRefreshLayout.getState() == RefreshState.Loading) return;
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.autoRefresh();
            }
        });
    }

    public View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_empty, mRecyclerView, false);
    }

    protected @DrawableRes
    int getEmptyImage() {
        return R.drawable.ic_empty;
    }

    protected @StringRes
    int getEmptyString() {
        return R.string.no_data;
    }

    protected @ColorInt
    int getEmptyStringColor() {
        return getResources().getColor(R.color.colorB7B7C4);
    }

    public List<T> getDataSource() {
        return mDataSource;
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
//        return new DividerItemDecoration(getContext(), getResources().getDrawable(R.drawable.shape_recycler_view_divider), DividerItemDecoration.VERTICAL_LIST);
    }

    protected RecyclerView.Adapter getAdapter() {
        if (mHeaderAndFooterWrapper != null) return mHeaderAndFooterWrapper;
        mCommonAdapter = new CommonAdapter<T>(getContext(), getItemLayoutResId(), mDataSource) {
            @Override
            protected void convert(ViewHolder holder, T t, int position) {
                onBindView(holder, t, position);
            }
        };
        mCommonAdapter.setOnItemClickListener(this);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mCommonAdapter);
        mHeaderAndFooterWrapper.setEmptyView(getEmptyView());
        mHeaderAndFooterWrapper.setEmptyImage(getEmptyImage());
        mHeaderAndFooterWrapper.setEmptyString(getEmptyString());
        mHeaderAndFooterWrapper.setEmptyStringColor(getEmptyStringColor());

        mLoadFootView = getLoadFootView();
        if (getFooterView() != null)
            mHeaderAndFooterWrapper.addFootView(setupHeaderAndFooterView(getFooterView()));
        mHeaderAndFooterWrapper.addFootView(mLoadFootView);
        mHeaderAndFooterWrapper.addHeaderView(setupHeaderAndFooterView(getHeaderView()));

//        FragmentStatePagerAdapter
//        FragmentPagerAdapter
        return mHeaderAndFooterWrapper;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    private NormalLoadFootView getLoadFootView() {
        return new NormalLoadFootView(getContext());
    }

    private View setupHeaderAndFooterView(View view) {
        if (null == view) return null;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(layoutParams);
        return view;
    }

    public View getHeaderView() {
        return null;
    }

    public View getFooterView() {
        return null;
    }

    public boolean isSupportLoadMore() {
        return true;
    }

    public boolean isSupportRefresh() {
        return true;
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        try {
            if (getHeaderView() == null) {
                onItemClick(view, holder, position, mDataSource.get(position));
            } else {
                onItemClick(view, holder, position, mDataSource.get(position - 1));

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public abstract void onItemClick(View view, RecyclerView.ViewHolder holder, int position, T t);


    @Override
    public void onRefresh() {
        if (isRefresh || isLoadMore) return;
        isRefresh = true;
        loadPage = DEFAULT_START_PAGE;
        onRequest(loadPage);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (isRefresh || isLoadMore) return;
        isRefresh = true;
        loadPage = DEFAULT_START_PAGE;
        onRequest(loadPage);
    }

    @Override
    public void onLoadMore() {
        if (isRefresh || isLoadMore) return;
        isLoadMore = true;
        loadPage++;
        mLoadFootView.setViewState(NormalLoadFootView.STATE_LOADING);
        onRequest(loadPage);
    }


    public void dispatchResult(List<T> dataSource) {
        boolean isLoadEffectiveData = null != dataSource && dataSource.size() > 0;
        if (isRefresh) {
//            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.finishRefresh();
            isRefresh = false;
            if (isLoadEffectiveData) {
                mLoadFootView.setViewState(NormalLoadFootView.STATE_IDLE);
                mRecyclerView.setSupportLoadMore(true);
            }
            onRefreshResult(dataSource);
            return;
        }
        if (isLoadMore) {
            isLoadMore = false;
            if (isLoadEffectiveData) {
                mLoadFootView.setViewState(NormalLoadFootView.STATE_IDLE);
            } else {
                mLoadFootView.setViewState(NormalLoadFootView.STATE_NO_MORE);
                mRecyclerView.setSupportLoadMore(false);
            }
            onLoadMoreResult(dataSource);
            return;
        }
        if (!isRefresh && !isLoadMore) {
            if (isLoadEffectiveData) {
                mLoadFootView.setViewState(NormalLoadFootView.STATE_IDLE);
                mRecyclerView.setSupportLoadMore(true);
            }
            onRefreshResult(dataSource);
        }
    }


    protected void onRefreshResult(List<T> dataSource) {
        mCommonAdapter.refreshData(dataSource);
        mDataSource = mCommonAdapter.getDatas();
        notifyDataSetChanged();
//        mEmptyPlaceholderView.setVisibility((mDataSource != null && mDataSource.size() > 0) ? View.GONE : View.VISIBLE);
    }

    protected void setViewStateWithDataNoMore() {
//        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.finishRefresh();
        mLoadFootView.setViewState(NormalLoadFootView.STATE_NO_MORE);
        mRecyclerView.setSupportLoadMore(false);
        isRefresh = false;
        isLoadMore = false;
    }

    protected void cancelAllStatus() {
//        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.finishRefresh();
        mLoadFootView.setViewState(NormalLoadFootView.STATE_IDLE);
        mRecyclerView.setSupportLoadMore(isSupportLoadMore());
        isRefresh = false;
        isLoadMore = false;
    }

    protected void onLoadMoreResult(List<T> dataSource) {
        mCommonAdapter.appendData(dataSource);
        mDataSource = mCommonAdapter.getDatas();
        notifyDataSetChanged();
//        mEmptyPlaceholderView.setVisibility((mDataSource != null && mDataSource.size() > 0) ? View.GONE : View.VISIBLE);
    }

    protected void notifyDataSetChanged() {
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    protected void notifyItemChanged(int pos) {
        mHeaderAndFooterWrapper.notifyItemChanged(pos, "changed");
    }

    public abstract void onRequest(int page);

    public abstract int getItemLayoutResId();

    public abstract void onBindView(ViewHolder holder, T t, int position);


}
