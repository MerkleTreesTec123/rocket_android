package com.muye.rocket.mvp.c2c.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.lib_base.tools.AppManager;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.c2c.WalletDetailRecord;
import com.muye.rocket.mvp.c2c.contract.C2CBalanceContract;
import com.muye.rocket.mvp.c2c.contract.WalletDetailRecordListContract;
import com.muye.rocket.mvp.c2c.presenter.C2CBalancePresenter;
import com.muye.rocket.mvp.c2c.presenter.WalletDetailRecordListPresenter;
import com.muye.rocket.mvp.home.view.HomeActivity;
import com.muye.rocket.widget.CustomTabLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;

import java.util.ArrayList;
import java.util.List;

public class WalletDetailFragment extends BaseListFragment<WalletDetailRecord> implements
        C2CBalanceContract.View, WalletDetailRecordListContract.View, OnTabSelectListener, OnMultiPurposeListener {
    View mBackgroundView;
    View mHeaderView;
    TextView mTitleTextView;
    TextView mAvailableTextView;
    TextView mFreezeTextView;
    CustomTabLayout mTabLayout;
    ConstraintLayout mTotalContainer;
    View mMoreContainer;

    String mCoinID;
    String mDataType = "6,90,91";
    C2CBalanceContract.Presenter mBalancePresenter;
    WalletDetailRecordListContract.Presenter mRecordPresenter;

    public static WalletDetailFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        WalletDetailFragment fragment = new WalletDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.c2c_item_wallet_detail;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public View getHeaderView() {
        if (mHeaderView == null) {
            if (mHeaderView == null) {
                mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.c2c_header_wallet_detail, mRecyclerView, false);
                mTitleTextView = mHeaderView.findViewById(R.id.title_text_view);
                mAvailableTextView = mHeaderView.findViewById(R.id.available_text_view);
                mFreezeTextView = mHeaderView.findViewById(R.id.freeze_text_view);
                mTabLayout = mHeaderView.findViewById(R.id.tab_layout);
                mTotalContainer = mHeaderView.findViewById(R.id.total_container);

                mHeaderView.findViewById(R.id.c2c_button).setOnClickListener(this);
                mHeaderView.findViewById(R.id.transfer_button).setOnClickListener(this);
                mHeaderView.findViewById(R.id.release_button).setOnClickListener(this);
                setupTabLayout();
            }
        }
        return mHeaderView;
    }

    private void setupTabLayout() {
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        tabEntityList.add(new TabEntity(getString(R.string.c2c_all)));
        tabEntityList.add(new TabEntity(getString(R.string.c2c)));
        tabEntityList.add(new TabEntity(getString(R.string.c2c_transfer)));
        mTabLayout.setTabData(tabEntityList);
        mTabLayout.setOnTabSelectListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.c2c_button:
                AppManager.getInstance().finishOtherActivity(HomeActivity.class);
                break;
            case R.id.transfer_button:
                C2CTransferActivity.openC2CTransferActivity(getContext(), mCoinID);
                break;
            case R.id.release_button:
                ReleaseActivity.openReleaseActivity(getContext(),0,mCoinID);
                break;
        }
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCoinID = bundle.getString("id", "");
        }
        super.initData();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, WalletDetailRecord record) {

    }

    @Override
    public void onRequest(int page) {
        if (mBalancePresenter == null) {
            mBalancePresenter = new C2CBalancePresenter(this);
        }

        if (mRecordPresenter == null) {
            mRecordPresenter = new WalletDetailRecordListPresenter(this);
        }
        mBalancePresenter.fetchCoinBalance(mCoinID);
        mRecordPresenter.fetchWalletDetailRecord(mCoinID, mDataType, page);
    }

    @Override
    public void bindCoinBalance(String coinID, String coinName, double balance, double freeze) {
        mTitleTextView.setText(coinName);
        mAvailableTextView.setText(NumberUtil.formatMoneyDown(balance));
        mFreezeTextView.setText(NumberUtil.formatMoneyDown(freeze));
    }

    @Override
    public void bindWalletDetailRecord(List<WalletDetailRecord> recordList) {
        dispatchResult(recordList);
    }

    @Override
    public void onBindView(ViewHolder holder, WalletDetailRecord record, int position) {
        TextView statusTextView = holder.getView(R.id.status_text_view);
        TextView timeTextView_ = holder.getView(R.id.time_text_view_);
        if (record != null) {
            holder.setText(R.id.title_text_view, record.getNote());
            if (record.getValue() > 0) {
                holder.setText(R.id.num_text_view, "+" + NumberUtil.formatMoney(record.getValue()));
            } else {
                holder.setText(R.id.num_text_view, NumberUtil.formatMoney(record.getValue()));
            }

            holder.setText(R.id.time_text_view, record.getInputtime());
            statusTextView.setText(getString(R.string.c2c_completed));
            statusTextView.setTextColor(getResources().getColor(R.color.c2cColor00DA97));
        } else {
            holder.setText(R.id.title_text_view, "");
            holder.setText(R.id.num_text_view, "");
            holder.setText(R.id.time_text_view, "");
            statusTextView.setText("");
            timeTextView_.setText("");
        }
    }

    @Override
    public void onDestroy() {
        if (mRecordPresenter != null) {
            mRecordPresenter.dropView();
        }
        if (mBalancePresenter != null) {
            mBalancePresenter.dropView();
        }
        super.onDestroy();
    }

    @Override
    public void onTabSelect(int position) {
        if (position == 1) {
            mDataType = "6";
        } else if (position == 2) {
            mDataType = "90,91";
        } else {
            mDataType = "6,90,91";
        }
//        forceRefresh();
        onRefresh();
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {

    }

    @Override
    public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

    }

    @Override
    public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

    }

    @Override
    public void onHeaderFinish(RefreshHeader header, boolean success) {

    }

    @Override
    public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

    }

    @Override
    public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

    }

    @Override
    public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

    }

    @Override
    public void onFooterFinish(RefreshFooter footer, boolean success) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        mTabLayout.setEnabled(RefreshState.None== newState);
    }
}
