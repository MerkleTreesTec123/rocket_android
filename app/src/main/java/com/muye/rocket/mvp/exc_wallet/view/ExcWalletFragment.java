package com.muye.rocket.mvp.exc_wallet.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.muye.rocket.base.BaseListFragment;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.cache.ExcCache;
import com.muye.rocket.cache.IEOCache;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.event.RefreshIEOReleaseEvent;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWalletContract;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcWalletPresenter;
import com.muye.rocket.tools.GlideLoadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ExcWalletFragment extends BaseListFragment<ExcAssetsEntity.WalletBean> implements ExcWalletContract.View {

    View mHeaderView;
    TextView mAssetsTitleTextView;
    TextView mAssetsTextView;
    ExcWalletContract.Presenter mWalletPresenter;

    double totalAssets = -1;

    public static ExcWalletFragment newInstance() {
        Bundle args = new Bundle();
        ExcWalletFragment fragment = new ExcWalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.exc_wal_item_wallet;
    }

    @Override
    public boolean isSupportLoadMore() {
        return false;
    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
        EventBus.getDefault().register(this);
    }

    @Override
    public View getHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.exc_wal_header_wallet, mRecyclerView, false);
            mAssetsTitleTextView = mHeaderView.findViewById(R.id.assets_title_text_view);
            mAssetsTextView = mHeaderView.findViewById(R.id.assets_text_view);
            mAssetsTitleTextView.setOnClickListener(this);
            bindTotalAssets();
        }
        return mHeaderView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.assets_title_text_view:
                MMKVTools.changeIsShowAssetsWalletNum();
                bindTotalAssets();
                break;
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, ExcAssetsEntity.WalletBean walletBean) {
        if (walletBean != null) {
            //暂未开放
//            if (MMKVTools.isOpenTrade()) {
                if (walletBean.getCoinName().equals("USDT")||walletBean.getCoinName().equals("CAT")){
                    ExcWalletDetailActivity.openExcWalletDetailActivity(getContext(), walletBean.getCoinId());
                }else {
                    showToast(getString(R.string.watting_for_));
                }
//            }else {
//                showToast(getString(R.string.waiting));
//            }
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onStart() {
        super.onStart();
//        forceRefresh();
        onRefresh();
    }

    @Override
    public void onRequest(int page) {
        if (mWalletPresenter == null) {
            mWalletPresenter = new ExcWalletPresenter(this);
        }
        mWalletPresenter.fetchAssets();
    }

    @Override
    public void bindAssets(ExcAssetsEntity assetsEntity) {
        if (assetsEntity != null) {
            totalAssets = assetsEntity.getNetassets();
            List<ExcAssetsEntity.WalletBean> walletBeanList = new ArrayList<>();
            if (assetsEntity != null && assetsEntity.getWallet() != null && assetsEntity.getWallet().size() > 0) {
                for (ExcAssetsEntity.WalletBean walletBean : assetsEntity.getWallet()) {
                    if (walletBean != null) {
                        if ("CAT".equals(walletBean.getShortName()) && walletBeanList.size() > 0) {
                            walletBeanList.add(0, walletBean);
                        } else {
                            walletBeanList.add(walletBean);
                        }
                    }
                }
            }
            dispatchResult(walletBeanList);
        } else {
            totalAssets = -1;
            dispatchResult(new ArrayList<>());
        }
        bindTotalAssets();
    }

    private void bindTotalAssets() {
        if (MMKVTools.isShowAssetsWalletNum()) {
            mAssetsTitleTextView.setSelected(false);
            mAssetsTextView.setText(totalAssets >= 0 ? NumberUtil.formatRMBDown(totalAssets) : "--");
        } else {
            mAssetsTextView.setText("******");
            mAssetsTitleTextView.setSelected(true);
        }
    }

    @Override
    public void onBindView(ViewHolder holder, ExcAssetsEntity.WalletBean walletBean, int position) {
        ImageView coverImageView = holder.getView(R.id.cover_image_view);
        TextView releasedTextView = holder.getView(R.id.released_text_view);
        TextView releasedTextView_ = holder.getView(R.id.released_text_view_);
        TextView unReleasedTextView = holder.getView(R.id.unreleased_text_view);
        TextView unReleasedTextView_ = holder.getView(R.id.unreleased_text_view_);
        if (walletBean != null) {
            GlideLoadUtils.getInstance().glideLoad(getContext(), walletBean.getLogo(), coverImageView);
            //名称
            if ("USDT".equals(walletBean.getShortName())) {
                holder.setText(R.id.title_text_view, walletBean.getShortName() + " (ERC-20)");
            } else {
                holder.setText(R.id.title_text_view, walletBean.getShortName());
            }

            releasedTextView.setVisibility("CAT".equals(walletBean.getShortName()) ? View.VISIBLE : View.GONE);
            releasedTextView_.setVisibility("CAT".equals(walletBean.getShortName()) ? View.VISIBLE : View.GONE);
            unReleasedTextView.setVisibility("CAT".equals(walletBean.getShortName()) ? View.VISIBLE : View.GONE);
            unReleasedTextView_.setVisibility("CAT".equals(walletBean.getShortName()) ? View.VISIBLE : View.GONE);
            if ("CAT".equals(walletBean.getShortName())) {
                releasedTextView.setText(NumberUtil.formatMoney(IEOCache.getIEOReleased()) + " CAT");
                unReleasedTextView.setText(NumberUtil.formatMoney(IEOCache.getIEOUnReleased()) + " CAT");
            } else {
                releasedTextView.setText("--");
                unReleasedTextView.setText("--");
            }

            double rmbBalance = ExcCache.getRMBPriceCache(walletBean.getShortName()) * walletBean.getTotal();
            holder.setText(R.id.balance_text_view, NumberUtil.formatMoneyDown(walletBean.getTotal()));
            holder.setText(R.id.balance_text_view_, "≈ ¥" + NumberUtil.formatRMBDown(rmbBalance));
        } else {
            GlideLoadUtils.getInstance().glideLoad(getContext(), "", coverImageView);
            holder.setText(R.id.title_text_view, "--");
            holder.setText(R.id.unit_text_view, "--");
            holder.setText(R.id.balance_text_view, "--");
            holder.setText(R.id.balance_text_view_, "≈ ¥--");

            releasedTextView.setVisibility(View.GONE);
            releasedTextView_.setVisibility(View.GONE);
            unReleasedTextView.setVisibility(View.GONE);
            unReleasedTextView_.setVisibility(View.GONE);
            releasedTextView.setText("--");
            unReleasedTextView.setText("--");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshIEORelease(RefreshIEOReleaseEvent event) {
        notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        if (mWalletPresenter != null) {
            mWalletPresenter.dropView();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
