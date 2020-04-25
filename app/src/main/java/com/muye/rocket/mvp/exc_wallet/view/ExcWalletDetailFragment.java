package com.muye.rocket.mvp.exc_wallet.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DateTimeTool;
import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.common.view.NormalLoadFootView;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.cache.ExcCache;
import com.muye.rocket.cache.IEOCache;
import com.muye.rocket.entity.c2c.C2CCoinBalance;
import com.muye.rocket.entity.exc_wallet.ExcAssetsDetail;
import com.muye.rocket.entity.exc_wallet.ExcWalletRecord;
import com.muye.rocket.entity.exchange.ExcTradingPair;
import com.muye.rocket.event.RefreshIEOReleaseEvent;
import com.muye.rocket.mvp.c2c.contract.C2CBalanceByAssetsContract;
import com.muye.rocket.mvp.c2c.presenter.C2CBalanceByAssetsPresenter;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWalletDetailContract;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWalletRecordContract;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcWalletDetailPresenter;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcWalletRecordPresenter;
import com.muye.rocket.mvp.exchange.view.ExchangeActivity;
import com.muye.rocket.widget.CustomTabLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.support.v7.widget.RecyclerView.GONE;
import static android.support.v7.widget.RecyclerView.OnScrollListener;
import static android.support.v7.widget.RecyclerView.VISIBLE;

public class ExcWalletDetailFragment extends BaseListFragment<ExcWalletRecord> implements ExcWalletDetailContract.View,
        ExcWalletRecordContract.View, OnTabSelectListener, OnMultiPurposeListener , C2CBalanceByAssetsContract.View{
    View mBackgroundView;
    View mHeaderView;
    TextView mTitleTextView;
    TextView mAvailableTextView;
    TextView mFreezeTextView;
    TextView mReleasedTextView;
    TextView mReleasedTextView_;
    TextView mUnReleasedTextView;
    TextView mUnReleasedTextView_;
    TextView mTransferButton;
    CustomTabLayout mTabLayout;
    ConstraintLayout mTotalContainer;
    List<ExcWalletRecord> mAllRecordList;
    List<ExcWalletRecord> mRechargeRecordList;
    List<ExcWalletRecord> mWithdrawRecordList;
    List<ExcWalletRecord> mTransferInRecordList;
    List<ExcWalletRecord> mTransferOutRecordList;
    ExcWalletDetailContract.Presenter mWalletDetailPresenter;
    ExcWalletRecordContract.Presenter mRecordPresenter;
    C2CBalanceByAssetsContract.Presenter mC2CBalancePresenter;

    String mCoinID;
    String mC2CCoinID;
    int mDp2Px76;
    int mAllRecordPage;
    int mRechargeRecordPage;
    int mWithdrawRecordPage;
    int mTransferInRecordPage;
    int mTransferOutRecordPage;
    ExcAssetsDetail mAssetsDetail;

    public static ExcWalletDetailFragment newInstance(String coinID) {
        Bundle args = new Bundle();
        args.putString("coinID", coinID);
        ExcWalletDetailFragment fragment = new ExcWalletDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.exc_wal_item_wallet_detail;
    }

    @Override
    protected int getRootViewLayout() {
        return R.layout.exc_wal_fragment_wallet_detail;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImmersionBar.statusBarDarkFont(false, 0.2f).init();
    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
        EventBus.getDefault().register(this);
        mDp2Px76 = DimensionTool.dp2px(getContext(), 76);
        mBackgroundView = parentView.findViewById(R.id.header_background_view);
        setupScrollListener();
        mRefreshLayout.setOnMultiPurposeListener(this);
    }

    @Override
    public View getHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.exc_wal_header_wallet_detail, mRecyclerView, false);
            mTitleTextView = mHeaderView.findViewById(R.id.title_text_view);
            mAvailableTextView = mHeaderView.findViewById(R.id.available_text_view);
            mFreezeTextView = mHeaderView.findViewById(R.id.freeze_text_view);
            mTabLayout = mHeaderView.findViewById(R.id.tab_layout);
            mReleasedTextView = mHeaderView.findViewById(R.id.released_text_view);
            mReleasedTextView_ = mHeaderView.findViewById(R.id.released_text_view_);
            mUnReleasedTextView = mHeaderView.findViewById(R.id.unreleased_text_view);
            mUnReleasedTextView_ = mHeaderView.findViewById(R.id.unreleased_text_view_);
            mTotalContainer = mHeaderView.findViewById(R.id.total_container);
            mTransferButton = mHeaderView.findViewById(R.id.transfer_button);

            mHeaderView.findViewById(R.id.recharge_button).setOnClickListener(this);
            mHeaderView.findViewById(R.id.withdraw_button).setOnClickListener(this);
            mTransferButton.setOnClickListener(this);
            mHeaderView.findViewById(R.id.exchange_button).setOnClickListener(this);
            setupTabLayout();
        }
        return mHeaderView;
    }

    private void setupTabLayout() {
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        tabEntityList.add(new TabEntity(getString(R.string.exc_wal_all)));
        tabEntityList.add(new TabEntity(getString(R.string.exc_wal_recharge)));
        tabEntityList.add(new TabEntity(getString(R.string.exc_wal_withdraw)));
        tabEntityList.add(new TabEntity(getString(R.string.exc_wal_transfer_in)));
        tabEntityList.add(new TabEntity(getString(R.string.exc_wal_transfer_out)));
        mTabLayout.setTabData(tabEntityList);
        mTabLayout.setOnTabSelectListener(this);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
        }
//        super.initData();
//       fetchData();
    }

    private void fetchData(){
        if (mRecordPresenter == null) {
            mRecordPresenter = new ExcWalletRecordPresenter(this);
        }
        mRecordPresenter.fetchWalletRecord(mCoinID, ExcWalletRecordContract.RECORD_TYPE_RECHARGE, 1);
        mRecordPresenter.fetchWalletRecord(mCoinID, ExcWalletRecordContract.RECORD_TYPE_WITHDRAW, 1);
        mRecordPresenter.fetchWalletRecord(mCoinID, ExcWalletRecordContract.RECORD_TYPE_C2C_2_ASSETS, 1);
        mRecordPresenter.fetchWalletRecord(mCoinID, ExcWalletRecordContract.RECORD_TYPE_ASSETS_2_C2C, 1);

        if (mC2CBalancePresenter == null) {
            mC2CBalancePresenter = new C2CBalanceByAssetsPresenter(this);
        }
        mC2CBalancePresenter.fetchCoinBalance(mCoinID);
    }

    @Override
    public void onStart() {
        super.onStart();
//        forceRefresh();
        onRefresh();
        fetchData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.recharge_button:          // 充币
                if (mAssetsDetail == null) return;
                if (!mAssetsDetail.getIs_recharge()) {
                    showToast(String.format(getString(R.string.exc_wal_can_not_recharge), mAssetsDetail.getCoin_name()));
                    return;
                }
                if (MMKVTools.hasBindPhone()) {
                    ExcRechargeActivity.openExcRechargeActivity(getContext(), mCoinID);
                }else {
                    showToast(getString(R.string.bind_you_phone));
                }
                break;
            case R.id.withdraw_button:   // 提币
                //暂未开放
//                showToast(getString(R.string.watting_for_));
                if (mAssetsDetail == null) return;
                if (!mAssetsDetail.getIs_recharge()) {
                    showToast(String.format(getString(R.string.exc_wal_can_not_withdraw), mAssetsDetail.getCoin_name()));
                    return;
                }
                ExcWithdrawActivity.openExcWithdrawActivity(getContext(), mCoinID);
                break;
            case R.id.transfer_button:  // 划转
                if (mAssetsDetail == null) return;
                showToast(getString(R.string.watting_for_));
//                if (TextUtils.isEmpty(mC2CCoinID)) {
//                    showToast(String.format(getString(R.string._not_support_transfer), mAssetsDetail.getCoin_name()));
//                    return;
//                }
//                C2CTransferActivity.openC2CTransferActivity(getContext(), mC2CCoinID);
                break;
            case R.id.exchange_button:   // 币币交易
                if (mAssetsDetail == null) return;
                if (!isSupportExchange()) {
                    showToast(getString(R.string.the_coin_not_support_currency_transaction));
                    return;
                }
                ExchangeActivity.openExchangeActivity(getContext(), mAssetsDetail.getCoin_id());
                break;
        }
    }

    private boolean isSupportExchange() {
        if(mAssetsDetail==null)return false;
        Map<String, List<ExcTradingPair>> cacheData = ExcCache.getTradingAreaCache();
        if (cacheData != null && cacheData.size() > 0) {
            for (Map.Entry<String, List<ExcTradingPair>> entry : cacheData.entrySet()) {
                List<ExcTradingPair> tradingPairList = entry.getValue();
                if (tradingPairList != null && tradingPairList.size() > 0) {
                    for (int i = 0; i < tradingPairList.size(); i++) {
                        ExcTradingPair tradingPair = tradingPairList.get(i);
                        if (tradingPair != null && mAssetsDetail.getCoin_id().equals(tradingPair.getSellCoinId())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, ExcWalletRecord record) {

    }

    @Override
    public void onTabSelect(int position) {
        loadData();
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onRequest(int page) {
        if (mWalletDetailPresenter == null) {
            mWalletDetailPresenter = new ExcWalletDetailPresenter(this);
        }
        if (mRecordPresenter == null) {
            mRecordPresenter = new ExcWalletRecordPresenter(this);
        }

        String type = "";
        int page_ = 1;
        if (mTabLayout.getCurrentTab() == 1) {
            type = ExcWalletRecordContract.RECORD_TYPE_RECHARGE;
            page_ = isLoadMore ? ++mRechargeRecordPage : 1;
        } else if (mTabLayout.getCurrentTab() == 2) {
            type = ExcWalletRecordContract.RECORD_TYPE_WITHDRAW;
            page_ = isLoadMore ? ++mWithdrawRecordPage : 1;
        } else if (mTabLayout.getCurrentTab() == 3) {
            type = ExcWalletRecordContract.RECORD_TYPE_C2C_2_ASSETS;
            page_ = isLoadMore ? ++mTransferInRecordPage : 1;
        } else if (mTabLayout.getCurrentTab() == 4) {
            type = ExcWalletRecordContract.RECORD_TYPE_ASSETS_2_C2C;
            page_ = isLoadMore ? ++mTransferOutRecordPage : 1;
        } else {
            type = "";
            page_ = isLoadMore ? ++mAllRecordPage : 1;
        }
        mWalletDetailPresenter.fetchAssetsDetail(mCoinID);
        mRecordPresenter.fetchWalletRecord(mCoinID, type, page_);
    }


    @Override
    public void onBindView(ViewHolder holder, ExcWalletRecord record, int position) {
        if (record != null) {
            holder.setText(R.id.title_text_view, record.getOperationDesc());
            holder.setText(R.id.num_text_view, NumberUtil.formatMoney(record.getAmount()));
            holder.setText(R.id.status_text_view, record.getStatusDesc());
            holder.setText(R.id.time_text_view, DateTimeTool.getYYYYMMDDHHMMSS(record.getCreateDate()));
        } else {
            holder.setText(R.id.title_text_view, "--");
            holder.setText(R.id.num_text_view, "--");
            holder.setText(R.id.status_text_view, "--");
            holder.setText(R.id.time_text_view, "--");
        }
    }

    @Override
    public void bindAssetsDetail(ExcAssetsDetail assetsDetail) {
        mAssetsDetail = assetsDetail;
        if (assetsDetail != null) {
            mTitleTextView.setText(assetsDetail.getCoin_name());
            mAvailableTextView.setText(NumberUtil.formatMoneyDown(assetsDetail.getTotal()));
            mFreezeTextView.setText(NumberUtil.formatMoneyDown(assetsDetail.getFrozen()));

            mReleasedTextView.setVisibility("CAT".equals(mAssetsDetail.getCoin_name()) ? VISIBLE : GONE);
            mReleasedTextView_.setVisibility("CAT".equals(mAssetsDetail.getCoin_name()) ? VISIBLE : GONE);
            mUnReleasedTextView.setVisibility("CAT".equals(mAssetsDetail.getCoin_name()) ? VISIBLE : GONE);
            mUnReleasedTextView_.setVisibility("CAT".equals(mAssetsDetail.getCoin_name()) ? VISIBLE : GONE);
            mTotalContainer.setBackgroundResource("CAT".equals(mAssetsDetail.getCoin_name()) ?
                    R.drawable.exc_wal_image_detail_header_bg_large : R.drawable.exc_wal_image_detail_header_bg_small);
        } else {
            mTitleTextView.setText("--");
            mAvailableTextView.setText("--");
            mFreezeTextView.setText("--");

            mReleasedTextView.setVisibility(GONE);
            mReleasedTextView_.setVisibility(GONE);
            mUnReleasedTextView.setVisibility(GONE);
            mUnReleasedTextView_.setVisibility(GONE);
            mTotalContainer.setBackgroundResource(R.drawable.exc_wal_image_detail_header_bg_small);
        }
        bindReleased();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshIEORelease(RefreshIEOReleaseEvent event) {
        bindReleased();
    }

    private void bindReleased() {
        if (mAssetsDetail != null && "GOD".equals(mAssetsDetail.getCoin_name())) {
            mReleasedTextView.setText(NumberUtil.formatMoney(IEOCache.getIEOReleased()));
            mUnReleasedTextView.setText(NumberUtil.formatMoney(IEOCache.getIEOUnReleased()));
        }
    }

    @Override
    public void bindWalletRecord(List<ExcWalletRecord> recordList, String recordType) {
        if (ExcWalletRecordContract.RECORD_TYPE_RECHARGE.equals(recordType)) {
            if (mRechargeRecordList == null) {
                mRechargeRecordList = new ArrayList<>();
            }
            if (!isLoadMore) {
                mRechargeRecordList.clear();
            }
            if (recordList != null) {
                mRechargeRecordList.addAll(recordList);
            }
        } else if (ExcWalletRecordContract.RECORD_TYPE_WITHDRAW.equals(recordType)) {
            if (mWithdrawRecordList == null) {
                mWithdrawRecordList = new ArrayList<>();
            }
            if (!isLoadMore) {
                mWithdrawRecordList.clear();
            }
            if (recordList != null) {
                mWithdrawRecordList.addAll(recordList);
            }
        } else if (ExcWalletRecordContract.RECORD_TYPE_C2C_2_ASSETS.equals(recordType)) {
            if (mTransferInRecordList == null) {
                mTransferInRecordList = new ArrayList<>();
            }
            if (!isLoadMore) {
                mTransferInRecordList.clear();
            }
            if (recordList != null) {
                mTransferInRecordList.addAll(recordList);
            }
        } else if (ExcWalletRecordContract.RECORD_TYPE_ASSETS_2_C2C.equals(recordType)) {
            if (mTransferOutRecordList == null) {
                mTransferOutRecordList = new ArrayList<>();
            }
            if (!isLoadMore) {
                mTransferOutRecordList.clear();
            }
            if (recordList != null) {
                mTransferOutRecordList.addAll(recordList);
            }
        } else {
            if (mAllRecordList == null) {
                mAllRecordList = new ArrayList<>();
            }
            if (!isLoadMore) {
                mAllRecordList.clear();
            }
            if (recordList != null) {
                mAllRecordList.addAll(recordList);
            }
        }
        loadData();
    }

    private void loadData() {
        if (mTabLayout.getCurrentTab() == 1) {
            dispatchResult(mRechargeRecordList);
        } else if (mTabLayout.getCurrentTab() == 2) {
            dispatchResult(mWithdrawRecordList);
        } else if (mTabLayout.getCurrentTab() == 3) {
            dispatchResult(mTransferInRecordList);
        } else if (mTabLayout.getCurrentTab() == 4) {
            dispatchResult(mTransferOutRecordList);
        } else {
            dispatchResult(mAllRecordList);
        }
    }

    @Override
    public void dispatchResult(List dataSource) {
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
            onRefreshResult(dataSource);
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

    private void setupScrollListener() {
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //找到即将移出屏幕Item的position,position是移出屏幕item的数量
                int position = linearLayoutManager.findFirstVisibleItemPosition();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mBackgroundView.getLayoutParams();
                if (position == 0) {
                    //根据position找到这个Item
                    View firstVisibleChildView = linearLayoutManager.findViewByPosition(position);
                    //算出该Item还未移出屏幕的高度
                    int itemTop = firstVisibleChildView.getTop();
                    if (itemTop < 0) {
                        if (Math.abs(itemTop) < mDp2Px76) {
                            layoutParams.height = mDp2Px76 - Math.abs(itemTop);
                        } else {
                            layoutParams.height = 0;
                        }
                    } else {
                        layoutParams.height = mDp2Px76;
                    }
                } else {
                    layoutParams.height = 0;
                }
                mBackgroundView.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (mWalletDetailPresenter != null) {
            mWalletDetailPresenter.dropView();
        }
        if (mRecordPresenter != null) {
            mRecordPresenter.dropView();
        }
        if(mC2CBalancePresenter!=null){
            mC2CBalancePresenter.dropView();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
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
        mTabLayout.setEnabled(RefreshState.None.name().equals(newState.name()));
    }

    @Override
    public void bindCoinBalance(C2CCoinBalance balance) {
        if (balance != null && !TextUtils.isEmpty(balance.getCoin_id())) {
            mC2CCoinID = balance.getCoin_id();
            mTransferButton.setVisibility(VISIBLE);
        } else {
            mC2CCoinID = "";
            mTransferButton.setVisibility(GONE);
        }
    }
}
