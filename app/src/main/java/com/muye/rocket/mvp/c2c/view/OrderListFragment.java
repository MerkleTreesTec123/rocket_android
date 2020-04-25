package com.muye.rocket.mvp.c2c.view;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.c2c.C2COrder;
import com.muye.rocket.mvp.c2c.contract.C2COrderListContract;
import com.muye.rocket.mvp.c2c.contract.UpdateOrderStatusContract;
import com.muye.rocket.mvp.c2c.presenter.C2COrderListPresenter;
import com.muye.rocket.mvp.c2c.presenter.UpdateOrderStatusPresenter;
import com.muye.rocket.widget.dialog.MessageDialog;

import java.util.List;

public class OrderListFragment extends BaseListFragment<C2COrder> implements C2COrderListContract.View,
        UpdateOrderStatusContract.View {

    String mTransactionType;
    C2COrderListContract.Presenter mOrderPresenter;
    UpdateOrderStatusContract.Presenter mUpdateOrderStatusPresenter;
    int mSp2px12;
    int mSp2px14;

    public static OrderListFragment newInstance(String transactionType) {
        Bundle args = new Bundle();
        args.putString("transactionType", transactionType);
        OrderListFragment fragment = new OrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
        mSp2px12 = DimensionTool.sp2px(getContext(), 12);
        mSp2px14 = DimensionTool.sp2px(getContext(), 14);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTransactionType = bundle.getString("transactionType", "");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        forceRefresh();
        onRefresh();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, C2COrder order) {

    }


    @Override
    public void onRequest(int page) {
        if (mOrderPresenter == null) {
            mOrderPresenter = new C2COrderListPresenter(this);
        }
        mOrderPresenter.fetchOrderList(MMKVTools.getToken(), mTransactionType, "", "", "", page);
    }

    private void updateOrderStatus(String orderID, String status) {
        if (mUpdateOrderStatusPresenter == null) {
            mUpdateOrderStatusPresenter = new UpdateOrderStatusPresenter(OrderListFragment.this);
        }
        mUpdateOrderStatusPresenter.updateOrderStatus(orderID, "", "", "", status);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.c2c_item_order_record;
    }


    @Override
    public void bindOrderList(List<C2COrder> orderList) {
        dispatchResult(orderList);
    }

    @Override
    public void onBindView(ViewHolder holder, C2COrder order, int position) {
        ImageView deleteButton = holder.getView(R.id.delete_button);
        SwipeMenuLayout swipeLayout = holder.getView(R.id.swipe_layout);
        TextView statusTextView = holder.getView(R.id.status_text_view);
        ConstraintLayout contentView = holder.getView(R.id.content_container);

        TextView numTextView = holder.getView(R.id.num_text_view);
        TextView priceTextView = holder.getView(R.id.price_text_view);
        TextView amountTextView = holder.getView(R.id.amount_text_view);
        TextView timeTextView = holder.getView(R.id.time_text_view);

        TextView leftButton = holder.getView(R.id.left_action_button);
        TextView rightButton = holder.getView(R.id.right_action_button);
        if (order != null) {
            if (Constant.C2C_TRANSACTION_TYPE_BUY.equals(order.getType())) {
                holder.setText(R.id.title_text_view, getString(R.string.c2c_buy) + " " + order.getCoinname());
            } else if (Constant.C2C_TRANSACTION_TYPE_SELL.equals(order.getType())) {
                holder.setText(R.id.title_text_view, getString(R.string.c2c_sell) + " " + order.getCoinname());
            } else {
                holder.setText(R.id.title_text_view, order.getCoinname());
            }

            //单价
            String price = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_price_cny)
                    + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;" + NumberUtil.formatMoney(order.getPrice()) + "</font>";
            priceTextView.setText(Html.fromHtml(price));
            //数量
            String num = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_quantity)
                    + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;" + NumberUtil.formatMoney(order.getNums()) + "</font>";
            numTextView.setText(Html.fromHtml(num));
            //总价
            String amount = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_amount)
                    + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;" + NumberUtil.formatMoney(order.getPrice() * order.getNums()) + "</font>";
            amountTextView.setText(Html.fromHtml(amount));
            //时间
            String time = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_time)
                    + "</font>" + "<font color=\"#292A49\"><small>" + "&nbsp;&nbsp;" + order.getInputtime() + "</small></font>";
            timeTextView.setText(Html.fromHtml(time));

            if ("1".equals(order.getStatus())) {//未付款
                statusTextView.setText(R.string.c2c_unpaid);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColor8488F5));

                swipeLayout.setSwipeEnable(false);

                leftButton.setVisibility(View.VISIBLE);
                leftButton.setText(R.string.c2c_cancel_order);
                bindLeftAction(leftButton, order.getId(), order.getType(), order.getStatus());

                //买单显示右边的按钮 我已付款
                if (Constant.C2C_TRANSACTION_TYPE_BUY.equals(order.getType())) {
                    rightButton.setVisibility(View.VISIBLE);
                    rightButton.setText(R.string.c2c_i_have_paid);

                    bindRightAction(rightButton, order.getId(), order.getType(), order.getStatus(),order.getTrpay_type());
                } else {//卖单不显示右边的按钮
                    rightButton.setVisibility(View.GONE);
                }
            } else if ("2".equals(order.getStatus())) {//待确认
                statusTextView.setText(R.string.c2c_to_be_confirmed);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColor8488F5));

                swipeLayout.setSwipeEnable(false);

                leftButton.setVisibility(View.VISIBLE);
                leftButton.setText(R.string.c2c_appeal);
                bindLeftAction(leftButton, order.getId(), order.getType(), order.getStatus());
                //买单不显示右边的按钮
                if (Constant.C2C_TRANSACTION_TYPE_BUY.equals(order.getType())) {
                    rightButton.setVisibility(View.GONE);
                } else {//卖单显示右边的按钮 确认收款
                    rightButton.setVisibility(View.VISIBLE);
                    rightButton.setText(R.string.c2c_confirm_receipt);

                    bindRightAction(rightButton, order.getId(), order.getType(), order.getStatus(),order.getTrpay_type());
                }

            } else if ("3".equals(order.getStatus())) {//申诉中
                statusTextView.setText(R.string.c2c_appealing);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColorF95D04));

                swipeLayout.setSwipeEnable(false);

                leftButton.setVisibility(View.GONE);

                rightButton.setVisibility(View.GONE);

            } else if ("4".equals(order.getStatus())) {//已完成
                statusTextView.setText(R.string.c2c_completed);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColor00DA97));

                swipeLayout.setSwipeEnable(true);

                leftButton.setVisibility(View.GONE);

                rightButton.setVisibility(View.GONE);
            } else {//交易关闭
                statusTextView.setText(R.string.c2c_deal_closure);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColorB7B7C4));

                swipeLayout.setSwipeEnable(true);

                leftButton.setVisibility(View.GONE);

                rightButton.setVisibility(View.GONE);
            }

        } else {
            //单价
            String price = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_price_cny)
                    + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;--</font>";
            priceTextView.setText(Html.fromHtml(price));

            //数量
            String num = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_quantity)
                    + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;--</font>";
            numTextView.setText(Html.fromHtml(num));

            //总价
            String amount = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_amount)
                    + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;--</font>";
            amountTextView.setText(Html.fromHtml(amount));

            //时间
            String time = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_time)
                    + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;--</font>";
            timeTextView.setText(Html.fromHtml(time));

            statusTextView.setText("--");
            statusTextView.setTextColor(getResources().getColor(R.color.c2cColorB7B7C4));
        }
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order == null) return;
                OrderDetailActivity.openOrderDetailActivity(getContext(), order.getId());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order == null) return;
                showDeleteDialog(order.getId());
            }
        });
    }

    private void showDeleteDialog(String orderID) {
        new MessageDialog(getContext())
                .setMessage(getString(R.string.c2c_delete_order_message))
                .setOkText(getString(R.string.c2c_ok))
                .setCancelText(getString(R.string.c2c_cancel))
                .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                    @Override
                    public void onOkButtonClick(MessageDialog dialog) {
                        dialog.dismiss();
                        if (mUpdateOrderStatusPresenter == null) {
                            mUpdateOrderStatusPresenter = new UpdateOrderStatusPresenter(OrderListFragment.this);
                        }
                        mUpdateOrderStatusPresenter.updateOrderStatus(orderID, "", "", "", "6");
                    }
                })
                .show();
    }

    MessageDialog mLeftActionDialog;

    private void bindLeftAction(View actionView, String orderID, String orderType, String orderStatus) {
        if (!TextUtils.isEmpty(orderID) && !TextUtils.isEmpty(orderType) && !TextUtils.isEmpty(orderStatus) && ("1".equals(orderStatus) || "2".equals(orderStatus))) {
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLeftActionDialog == null) {
                        mLeftActionDialog = new MessageDialog(getContext());
                    }
                    if ("1".equals(orderStatus)) {//未付款 取消订单
                        mLeftActionDialog.setMessage(getString(R.string.c2c_cancel_order_message))
                                .setOkText(getString(R.string.c2c_ok))
                                .setCancelText(getString(R.string.c2c_cancel))
                                .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                                    @Override
                                    public void onOkButtonClick(MessageDialog dialog) {
                                        dialog.dismiss();
                                        updateOrderStatus(orderID, "5");
                                    }
                                }).show();
                    } else if ("2".equals(orderStatus)) {//待确认 申诉
                        AppealActivity.openAppealActivity(getContext(), orderID);
                    }
                }
            });

        }

    }

    private void bindRightAction(View actionView, String orderID, String orderType, String orderStatus,String payType) {
        if (!TextUtils.isEmpty(orderID) && !TextUtils.isEmpty(orderType) && !TextUtils.isEmpty(orderStatus) && ("1".equals(orderStatus) || "2".equals(orderStatus))) {
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLeftActionDialog == null) {
                        mLeftActionDialog = new MessageDialog(getContext());
                    }
                    if ("1".equals(orderStatus) && Constant.C2C_TRANSACTION_TYPE_BUY.equals(orderType)) {//未付款 我已付款
                        C2CCertificateActivity.openC2CCertificateActivity(getContext(),orderID,payType);
                    } else if ("2".equals(orderStatus) && Constant.C2C_TRANSACTION_TYPE_SELL.equals(orderType)) {//待确认 我已收款
                        mLeftActionDialog.setMessage(getString(R.string.c2c_have_received_message))
                                .setOkText(getString(R.string.c2c_have_received))
                                .setCancelText(getString(R.string.c2c_cancel))
                                .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                                    @Override
                                    public void onOkButtonClick(MessageDialog dialog) {
                                        dialog.dismiss();
                                        updateOrderStatus(orderID, "4");
                                    }
                                }).show();
                    }
                }
            });

        }

    }

    @Override
    public void onUpdateOrderStatusSuccess() {
        forceRefresh();
    }

    @Override
    public void onDestroyView() {
        if (mOrderPresenter != null) mOrderPresenter.dropView();
        if (mUpdateOrderStatusPresenter != null) mUpdateOrderStatusPresenter.dropView();
        super.onDestroyView();
    }

}
