package com.muye.rocket.mvp.c2c.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.lib_base.tools.AppTool;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.c2c.WalletEntity;
import com.muye.rocket.mvp.c2c.contract.WalletContract;
import com.muye.rocket.mvp.c2c.presenter.WalletPresenter;

import java.util.ArrayList;

public class C2CWalletFragment extends BaseListFragment<WalletEntity.CoinWalletBean> implements WalletContract.View {

    View mHeaderView;
    TextView mAssetsTitleTextView;
    TextView mAssetsTextView;
    double mTotalAssets;

    WalletContract.Presenter mWalletPresenter;

    public static C2CWalletFragment newInstance() {
        Bundle args = new Bundle();
        C2CWalletFragment fragment = new C2CWalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.c2c_item_wallet;
    }

    @Override
    public boolean isSupportLoadMore() {
        return false;
    }

    @Override
    public View getHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.c2c_header_wallet, mRecyclerView, false);
            mAssetsTitleTextView = mHeaderView.findViewById(R.id.assets_title_text_view);
            mAssetsTextView = mHeaderView.findViewById(R.id.assets_text_view);
            mAssetsTitleTextView.setOnClickListener(this);
        }
        return mHeaderView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.assets_title_text_view:
                MMKVTools.changeIsShowC2CWalletNum();
                bindTotalAssets();
                break;
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, WalletEntity.CoinWalletBean coinWalletBean) {
        if (coinWalletBean != null) {
            WalletDetailActivity.openWalletDetailActivity(getContext(), coinWalletBean.getCoin_id());
        }
    }


    private void fetchWalletData() {
        if (mWalletPresenter == null) {
            mWalletPresenter = new WalletPresenter(this);
        }
        mWalletPresenter.fetchWallet();
    }

    @Override
    public void onRequest(int page) {
        fetchWalletData();
    }

    @Override
    public void bindWallet(WalletEntity walletEntity) {
        if (walletEntity != null) {
            mTotalAssets = walletEntity.getTotal();
            dispatchResult(walletEntity.getCoin_wallet());
        } else {
            mAssetsTextView.setText("——");
            dispatchResult(new ArrayList<>());
        }
        bindTotalAssets();

        if (!AppTool.isApkInDebug(getContext())) {
            fetchWalletData();
        }

    }

    private void bindTotalAssets() {
        if (MMKVTools.isShowC2CWalletNum()) {
            mAssetsTitleTextView.setSelected(false);
            mAssetsTextView.setText(mTotalAssets >= 0 ? NumberUtil.formatMoneyDown(mTotalAssets) : "--");
        } else {
            mAssetsTextView.setText("******");
            mAssetsTitleTextView.setSelected(true);
        }
    }

    @Override
    public void onBindView(ViewHolder holder, WalletEntity.CoinWalletBean coinWalletBean, int position) {

        if (coinWalletBean != null) {
            holder.setText(R.id.title_text_view, coinWalletBean.getCoinname());
            holder.setText(R.id.balance_text_view, NumberUtil.formatMoneyDown(coinWalletBean.getTotal()));
            holder.setText(R.id.freeze_text_view, NumberUtil.formatMoneyDown(coinWalletBean.getFrozen()));
        } else {
            holder.setText(R.id.title_text_view, "--");
            holder.setText(R.id.balance_text_view, "--");
            holder.setText(R.id.freeze_text_view, "--");
        }
    }

    @Override
    public void onDestroyView() {
        if (mWalletPresenter != null) {
            mWalletPresenter.dropView();
        }
        super.onDestroyView();
    }
}
