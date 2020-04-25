package com.muye.rocket.mvp.exc_wallet.view;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.cache.ExcCache;
import com.muye.rocket.cache.IEOCache;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.event.RefreshIEOReleaseEvent;
import com.muye.rocket.mvp.c2c.view.C2CTransferActivity;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWalletContract;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcWalletPresenter;
import com.muye.rocket.tools.GlideLoadUtils;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/*
* 资产
* */
public class CapitalFragment extends BaseListFragment<ExcAssetsEntity.WalletBean> implements ExcWalletContract.View, EasyPermissions.PermissionCallbacks {
    View mHeaderView;
    TextView mAssetsTitleTextView;
    TextView mAssetsTextView;
    ExcWalletContract.Presenter mWalletPresenter;
    double totalAssets = -1;
    private Toolbar mToolBar;
    private int mDp2px16;
    EasyPopup mActionMenu;
    private static final int RC_SCAN_PERM = 123;
    private static final String[] CAMERA_AND_STORAGE =
            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private TextView mTbNext;

    public static CapitalFragment newInstance() {
        Bundle args = new Bundle();
        CapitalFragment fragment = new CapitalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mImmersionBar.statusBarDarkFont(false, 0.2f).init();
        }
    }

    @Override
    public View getHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_capital, mRecyclerView, false);

            mToolBar = (Toolbar) mHeaderView.findViewById(R.id.toolbar);
            ImageView mTbBack = (ImageView) mHeaderView.findViewById(R.id.tb_back);
            TextView mTbTitle = (TextView) mHeaderView.findViewById(R.id.tb_title);
            mTbNext = (TextView) mHeaderView.findViewById(R.id.tb_next);

            mAssetsTitleTextView = mHeaderView.findViewById(R.id.assets_title_text_view);

            mHeaderView.findViewById(R.id.btn_recharge).setOnClickListener(this);
            mHeaderView.findViewById(R.id.btn_withdraw).setOnClickListener(this);
            mHeaderView.findViewById(R.id.btn_transfer).setOnClickListener(this);


            mAssetsTextView = mHeaderView.findViewById(R.id.assets_text_view);
            mAssetsTitleTextView.setOnClickListener(this);

            mToolBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mTbTitle.setText("资产");
            mTbTitle.setTextColor(getResources().getColor(R.color.white));
            mTbBack.setVisibility(View.GONE);
            mTbNext.setVisibility(View.VISIBLE);
            mTbNext.setText("...");
            mTbNext.setTextColor(getResources().getColor(R.color.white));
            mTbNext.setTextSize(22);
            mTbNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //暂未开放
//                if (MMKVTools.isOpenTrade()) {

                    showMenu();
//                }else {
//                    showToast(getString(R.string.waiting));
//                }
                }
            });
            bindTotalAssets();
        }
        return mHeaderView;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.exc_wal_item_wallet;
    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
        EventBus.getDefault().register(this);
        mDp2px16 = DimensionTool.dp2px(getContext(), 16);
    }

    @Override
    public boolean isSupportLoadMore() {
        return false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.assets_title_text_view:
                MMKVTools.changeIsShowAssetsWalletNum();
                bindTotalAssets();
                break;
            case R.id.btn_recharge://充币
                if (!MMKVTools.hasBindPhone()) {
                    showToast(getString(R.string.bind_you_phone));
                }else {
                    ExcRechargeActivity.openExcRechargeActivity(getContext(), "");
                }
                break;
            case R.id.btn_withdraw://提币
//                if (MMKVTools.isOpenTrade()) {
                    ExcWithdrawActivity.openExcWithdrawActivity(getContext(), "");
//                }else {
                    // 暂未开放
//                    showToast(getString(R.string.watting_for_));
//                }
                break;
            case R.id.btn_transfer://划转
                //暂未开放
                if (MMKVTools.isOpenTrade()) {
                    C2CTransferActivity.openC2CTransferActivity(getContext(),"");
                }else
                    showToast(getString(R.string.watting_for_));
                break;

            case R.id.scan_button:
                if (mActionMenu != null) {
                    mActionMenu.dismiss();
                }
                openScanPage();
                break;
            case R.id.qr_button://二维码（充币）
                if (mActionMenu != null) {
                    mActionMenu.dismiss();
                }
                ExcRechargeActivity.openExcRechargeActivity(getContext(), "");
                break;
            case R.id.address_button://地址簿
                if (mActionMenu != null) {
                    mActionMenu.dismiss();
                }
                ExcAddressBookActivity.openExcAddressBookActivity(getContext(), "");
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

    private void showMenu() {
        if (mActionMenu == null) {
            mActionMenu = EasyPopup.create(getContext())
                    .setContentView(R.layout.exc_wallet_menu_action)
                    .setAnchorView(mTbNext)
                    .setBackgroundDimEnable(true)
                    .apply();

            mActionMenu.getContentView().findViewById(R.id.scan_button).setOnClickListener(this);
            mActionMenu.getContentView().findViewById(R.id.qr_button).setOnClickListener(this);
            mActionMenu.getContentView().findViewById(R.id.address_button).setOnClickListener(this);
        }
        if (!mActionMenu.isShowing()) {
            mActionMenu.showAtAnchorView(mToolBar, YGravity.BELOW, XGravity.ALIGN_RIGHT, -mDp2px16, 0);
        }
    }

    @AfterPermissionGranted(RC_SCAN_PERM)
    private void openScanPage() {
        if (hasCameraStoragePermission()) {
            ScanActivity.openScan(getContext());
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.request_camera_read_write_permissions),
                    RC_SCAN_PERM, CAMERA_AND_STORAGE);
        }
    }

    private boolean hasCameraStoragePermission() {
        return EasyPermissions.hasPermissions(getContext(), CAMERA_AND_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

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
