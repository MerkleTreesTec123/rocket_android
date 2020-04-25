package com.muye.rocket.mvp.c2c.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.c2c.C2CCoin;
import com.muye.rocket.entity.c2c.C2CReleaseEntity;
import com.muye.rocket.mvp.c2c.contract.C2CCoinListContract;
import com.muye.rocket.mvp.c2c.contract.C2CReleaseListContract;
import com.muye.rocket.mvp.c2c.presenter.C2CCoinListPresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CReleaseListPresenter;
import com.muye.rocket.tools.StringTools;
import com.muye.rocket.widget.popup.C2CCoinPicker;
import com.muye.rocket.widget.popup.C2CPayTypePicker;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class C2CHomeFragment extends BaseListFragment<C2CReleaseEntity> implements C2CCoinPicker.OnCoinCheckedListener,
        C2CCoinListContract.View, C2CReleaseListContract.View {
    @BindView(R.id.sell_list_button)
    TextView sellListButton;
    @BindView(R.id.buy_list_button)
    TextView buyListButton;
    @BindView(R.id.more_button)
    ImageView moreButton;
    @BindView(R.id.coin_text_view)
    TextView coinTextView;
    @BindView(R.id.coin_triangle)
    ImageView coinTriangle;
    @BindView(R.id.pay_type_triangle)
    ImageView payTypeTriangle;
    @BindView(R.id.pay_type_text_view)
    TextView payTypeTextView;
    @BindView(R.id.ctl_select)
    ConstraintLayout ctlSelect;

    Unbinder unbinder;

    int mDp2px16;
    C2CCoin mCheckedCoin;
    EasyPopup mActionMenu;
    C2CCoinPicker mCoinPicker;
    C2CPayTypePicker mPayTypePicker;

    C2CCoinListContract.Presenter mCoinListPresenter;
    C2CReleaseListContract.Presenter mReleaseListPresenter;
    // 暂时关闭
    public static C2CHomeFragment newInstance() {
        Bundle args = new Bundle();
        C2CHomeFragment fragment = new C2CHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewLayout() {
        return R.layout.c2c_fragment_home;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.c2c_item_home;
    }


    @Override
    protected void initView(View parentView) {
        unbinder = ButterKnife.bind(this, parentView);
        super.initView(parentView);
        mDp2px16 = DimensionTool.dp2px(getContext(), 16);
        changeListType(ListType.LIST_TYPE_SELL);
        mRefreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
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
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                if (buyListButton != null) {
                    buyListButton.setEnabled(RefreshState.None == newState);
                }
                if (sellListButton != null) {
                    sellListButton.setEnabled(RefreshState.None == newState);
                }
                if (coinTextView != null) {
                    coinTextView.setEnabled(RefreshState.None == newState);
                }
                if (coinTriangle != null) {
                    coinTriangle.setEnabled(RefreshState.None == newState);
                }
                if (payTypeTextView != null) {
                    payTypeTextView.setEnabled(RefreshState.None == newState);
                }
                if (payTypeTriangle != null) {
                    payTypeTriangle.setEnabled(RefreshState.None == newState);
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
            fetchCoinList();
        }
    }

    private void showActionMenu() {
        if (mActionMenu == null) {
            mActionMenu = EasyPopup.create(getContext())
                    .setContentView(R.layout.c2c_home_action_menu)
                    .setFocusAndOutsideEnable(true)
                    .setBackgroundDimEnable(true)
                    .apply();
            mActionMenu.getContentView().findViewById(R.id.menu_address).setOnClickListener(this);
            mActionMenu.getContentView().findViewById(R.id.menu_wallet).setOnClickListener(this);
        }
        if (!mActionMenu.isShowing()) {
            mActionMenu.showAtAnchorView(moreButton, YGravity.BELOW, XGravity.ALIGN_RIGHT, -mDp2px16, 0);
        }
    }

    private void initCoinPicker() {
        if (mCoinPicker == null) {
            int[] location = new int[2];
            coinTextView.getLocationOnScreen(location);
            int width = DimensionTool.getScreenWidth(getContext());
            int height = DimensionTool.getScreenHeight(getContext()) -
//                    location[1] - coinTextView.getHeight() + DimensionTool.getStatusBarHeight(getContext()) - 2;
                    location[1] - coinTextView.getHeight() - 2;
            mCoinPicker = C2CCoinPicker.create(getContext(), width, height);
            mCoinPicker.setOnCoinCheckedListener(this);
        }
    }

    private void showCoinPicker() {
        initCoinPicker();
        if (!mCoinPicker.isShowing()) {
            mCoinPicker.showAtAnchorView(coinTextView, YGravity.BELOW, XGravity.ALIGN_LEFT, 0, 2);
        }
    }

    private void showPayTypePicker() {
        if (mPayTypePicker == null) {
            int[] location = new int[2];
            payTypeTextView.getLocationOnScreen(location);
            int width = DimensionTool.getScreenWidth(getContext());
            int height = DimensionTool.getScreenHeight(getContext()) -
//                    location[1] - coinTextView.getHeight() + DimensionTool.getStatusBarHeight(getContext()) - 2;
                    location[1] - coinTextView.getHeight() - 2;
            mPayTypePicker = C2CPayTypePicker.create(getContext(), width, height);
        }
        if (!mPayTypePicker.isShowing()) {
            mPayTypePicker.showAtAnchorView(payTypeTextView, YGravity.BELOW, XGravity.ALIGN_RIGHT, 0, 2);
        }
    }

    private void changeListType(@ListType String listType) {
        if (ListType.LIST_TYPE_BUY.equals(listType)) {
            sellListButton.setSelected(false);
            buyListButton.setSelected(true);
        } else {
            sellListButton.setSelected(true);
            buyListButton.setSelected(false);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent = null;
        switch (view.getId()) {
            case R.id.menu_address:  // 收款地址
                intent = new Intent(getContext(), C2CPayAccountActivity.class);
                startActivity(intent);
                mActionMenu.dismiss();
                break;
            case R.id.menu_wallet:// C2C钱包
                // 暂未开放
                if (MMKVTools.isOpenTrade()) {
                    intent = new Intent(getContext(), C2CWalletActivity.class);
                    startActivity(intent);
                }else {
                    showToast(getString(R.string.watting_for_));
                }
                mActionMenu.dismiss();
                break;
        }
    }

    @OnClick({R.id.sell_list_button, R.id.buy_list_button, R.id.more_button, R.id.coin_text_view,
            R.id.coin_triangle, R.id.pay_type_triangle, R.id.pay_type_text_view, R.id.release_button,
            R.id.my_release_button, R.id.order_button, R.id.transfer_button})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.sell_list_button:
                changeListType(ListType.LIST_TYPE_SELL);
//                forceRefresh();
                onRefresh();
                break;
            case R.id.buy_list_button:
                changeListType(ListType.LIST_TYPE_BUY);
//                forceRefresh();
                onRefresh();
                break;
            case R.id.more_button: // C2CFragment中 右上角的弹框
                showActionMenu();
                break;
            case R.id.coin_text_view:
            case R.id.coin_triangle:
                showCoinPicker();
                break;
            case R.id.pay_type_triangle:
            case R.id.pay_type_text_view:
                showPayTypePicker();
                break;
            case R.id.release_button:
                //暂未开放
//                if (MMKVTools.isOpenTrade()) {
//                    intent = new Intent(getContext(), ReleaseActivity.class);
//                    startActivity(intent);
//                }else {
                    showToast(getString(R.string.waiting));
//                }
                break;
            case R.id.my_release_button:
                intent = new Intent(getContext(), MyReleaseActivity.class);
                startActivity(intent);
                break;
            case R.id.order_button:
                intent = new Intent(getContext(), OrderListActivity.class);
                startActivity(intent);
                break;
            case R.id.transfer_button:
                //暂未开放
                if (MMKVTools.isOpenTrade()) {
                    intent = new Intent(getContext(), C2CTransferActivity.class);
                    startActivity(intent);
                }else
                showToast(getString(R.string.watting_for_));

                break;
        }
    }

    @Override
    public void onCoinChecked(C2CCoin coin, C2CCoinPicker coinPicker) {
        coinPicker.dismiss();
        bindCheckedCoin(coin);
    }


    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, C2CReleaseEntity releaseEntity) {

    }

    @Override
    public void onRequest(int page) {
        fetchReleaseList(page);
    }

    private void fetchCoinList() {
        if (mCoinListPresenter == null) {
            mCoinListPresenter = new C2CCoinListPresenter(this);
        }
        mCoinListPresenter.fetchCoinList();
    }

    private void fetchReleaseList(int page) {
        if (mCheckedCoin == null) {
            dispatchResult(null);
            return;
        }
        if (mReleaseListPresenter == null) {
            mReleaseListPresenter = new C2CReleaseListPresenter(this);
        }
        mReleaseListPresenter.fetchReleaseList("", mCheckedCoin.getId(),
                buyListButton.isSelected() ? ListType.LIST_TYPE_BUY : ListType.LIST_TYPE_SELL, page);
    }

    @Override
    public void bindCoinList(List<C2CCoin> coinList) {
        initCoinPicker();
        if (mCheckedCoin == null && coinList != null && coinList.size() > 0) {
            bindCheckedCoin(coinList.get(0));
            mCoinPicker.setCheckedID(mCheckedCoin == null ? "" : mCheckedCoin.getId());
        }
        mCoinPicker.setData(coinList);
    }

    private void bindCheckedCoin(C2CCoin coin) {
        if (coin == null) return;
        mCheckedCoin = coin;
        coinTextView.setText(mCheckedCoin.getCoinname());
//        forceRefresh();
        onRefresh();
    }

    @Override
    public void bindReleaseList(String orderType, List<C2CReleaseEntity> entityList) {
        dispatchResult(entityList);
    }

    @Override
    public void onBindView(ViewHolder holder, C2CReleaseEntity releaseEntity, int position) {
        TextView percentageNumTextView = holder.getView(R.id.percentage_num_text_view);
        Button actionButton = holder.getView(R.id.action_button);

        percentageNumTextView.setHint(String.format(getString(R.string.total_amount_), "--"));

        if (releaseEntity != null) {
            holder.setText(R.id.account_text_view, StringTools.phoneNumberFormat(releaseEntity.getUsername()));
            holder.setText(R.id.percentage_text_view, releaseEntity.getPercentage());
            percentageNumTextView.setText(String.format(getString(R.string.total_amount_),  NumberUtil.formatMoney(releaseEntity.getTotalnums())));
            holder.setText(R.id.price_text_view, NumberUtil.formatMoney(releaseEntity.getPrice()));
            holder.setText(R.id.amount_text_view, NumberUtil.formatMoney(releaseEntity.getNums()) + " " + releaseEntity.getCoinname());
            holder.setText(R.id.quota_text_view, NumberUtil.formatMoney(releaseEntity.getMin()) + " - " + NumberUtil.formatMoney(releaseEntity.getMax()));

            bindPayType(releaseEntity.getPay_type(), holder);

            actionButton.setVisibility(View.VISIBLE);
            actionButton.setText(ListType.LIST_TYPE_BUY.equals(releaseEntity.getType()) ? R.string.c2c_sell : R.string.c2c_buy);

        } else {
            holder.setText(R.id.account_text_view, "");
            holder.setText(R.id.percentage_text_view, "");
            percentageNumTextView.setText("");
            holder.setText(R.id.price_text_view, "");
            holder.setText(R.id.amount_text_view, "");
            holder.setText(R.id.quota_text_view, "");
            actionButton.setVisibility(View.GONE);
            bindPayType("", holder);
        }


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (releaseEntity == null) return;
                showToast(getString(R.string.waiting));
//                if (Constant.C2C_TRANSACTION_TYPE_BUY.equals(releaseEntity.getType())) {
//                    SellActivity.openSellActivity(getContext(), releaseEntity.getId(), releaseEntity.getCoin_id(), releaseEntity.getCoinname(),
//                            releaseEntity.getUsername(), releaseEntity.getTotalnums(), releaseEntity.getPrice(),
//                            releaseEntity.getPay_type(), releaseEntity.getMin(), releaseEntity.getMax(), releaseEntity.getNums());
//                } else {
//                    BuyActivity.openBuyActivity(getContext(), releaseEntity.getId());
//                }

            }
        });

    }

    private void bindPayType(String payType, ViewHolder holder) {
        if (Constant.C2C_PAY_TYPE_BANK_CARD.equals(payType)) {//银行卡
            holder.setVisible(R.id.bank_card_icon, true);
            holder.setVisible(R.id.alipay_icon, false);
            holder.setVisible(R.id.wechat_icon, false);
        } else if (Constant.C2C_PAY_TYPE_ALIPAY.equals(payType)) {//支付宝
            holder.setVisible(R.id.bank_card_icon, false);
            holder.setVisible(R.id.alipay_icon, true);
            holder.setVisible(R.id.wechat_icon, false);
        } else if (Constant.C2C_PAY_TYPE_WECHAT.equals(payType)) {//微信
            holder.setVisible(R.id.bank_card_icon, false);
            holder.setVisible(R.id.alipay_icon, false);
            holder.setVisible(R.id.wechat_icon, true);
        } else if (Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY.equals(payType)) {//银行卡支付宝
            holder.setVisible(R.id.bank_card_icon, true);
            holder.setVisible(R.id.alipay_icon, true);
            holder.setVisible(R.id.wechat_icon, false);
        } else if (Constant.C2C_PAY_TYPE_BANK_CARD_WECHAT.equals(payType)) {//银行卡微信
            holder.setVisible(R.id.bank_card_icon, true);
            holder.setVisible(R.id.alipay_icon, false);
            holder.setVisible(R.id.wechat_icon, true);
        } else if (Constant.C2C_PAY_TYPE_ALIPAY_WECHAT.equals(payType)) {//支付宝微信
            holder.setVisible(R.id.bank_card_icon, false);
            holder.setVisible(R.id.alipay_icon, true);
            holder.setVisible(R.id.wechat_icon, true);
        } else if (Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT.equals(payType)) {//银行卡支付宝微信
            holder.setVisible(R.id.bank_card_icon, true);
            holder.setVisible(R.id.alipay_icon, true);
            holder.setVisible(R.id.wechat_icon, true);
        } else {
            holder.setVisible(R.id.bank_card_icon, false);
            holder.setVisible(R.id.alipay_icon, false);
            holder.setVisible(R.id.wechat_icon, false);
        }
    }


    @Override
    public void onDestroyView() {
        if (mCoinListPresenter != null) {
            mCoinListPresenter.dropView();
        }
        if (mReleaseListPresenter != null) {
            mReleaseListPresenter.dropView();
        }
        super.onDestroyView();
        unbinder.unbind();
    }


    @StringDef({ListType.LIST_TYPE_SELL, ListType.LIST_TYPE_BUY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ListType {
        String LIST_TYPE_SELL = "2";
        String LIST_TYPE_BUY = "1";
    }
}
