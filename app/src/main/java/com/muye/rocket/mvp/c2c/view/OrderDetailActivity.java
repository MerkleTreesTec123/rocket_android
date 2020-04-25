package com.muye.rocket.mvp.c2c.view;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.c2c.C2COrder;
import com.muye.rocket.mvp.SystemTimeService;
import com.muye.rocket.mvp.c2c.contract.C2COrderDetailContract;
import com.muye.rocket.mvp.c2c.contract.UpdateOrderStatusContract;
import com.muye.rocket.mvp.c2c.presenter.C2COrderDetailPresenter;
import com.muye.rocket.mvp.c2c.presenter.UpdateOrderStatusPresenter;
import com.muye.rocket.tools.GlideLoadUtils;
import com.muye.rocket.widget.dialog.C2CPayAccountPicker;
import com.muye.rocket.widget.dialog.MessageDialog;
import com.muye.rocket.widget.dialog.ShowVoucherDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity implements C2COrderDetailContract.View,
        C2CPayAccountPicker.OnPayTypeCheckedListener, UpdateOrderStatusContract.View {
    @BindView(R.id.status_text_view)
    TextView statusTextView;
    @BindView(R.id.no_text_view)
    TextView noTextView;
    @BindView(R.id.num_hint_text_view)
    TextView numHintTextView;
    @BindView(R.id.num_text_view)
    TextView numTextView;
    @BindView(R.id.pay_no_text_view)
    TextView payNoTextView;
    @BindView(R.id.price_text_view)
    TextView priceTextView;
    @BindView(R.id.amount_text_view)
    TextView amountTextView;
    @BindView(R.id.type_text_view)
    TextView typeTextView;
    @BindView(R.id.pay_type_icon)
    ImageView payTypeIcon;
    @BindView(R.id.pay_type_text_view)
    TextView payTypeTextView;
    @BindView(R.id.pay_type_triangle)
    ImageView payTypeTriangle;
    @BindView(R.id.amount_text_view_)
    TextView amountTextView_;
    @BindView(R.id.qr_image_view)
    ImageView qrImageView;
    @BindView(R.id.account_title_text_view_1)
    TextView accountTitleTextView1;
    @BindView(R.id.account_content_text_view_1)
    TextView accountContentTextView1;
    @BindView(R.id.account_title_text_view_2)
    TextView accountTitleTextView2;
    @BindView(R.id.account_content_text_view_2)
    TextView accountContentTextView2;
    @BindView(R.id.account_title_text_view_3)
    TextView accountTitleTextView3;
    @BindView(R.id.account_content_text_view_3)
    TextView accountContentTextView3;

    @BindView(R.id.button_divider)
    View buttonDivider;
    @BindView(R.id.buyer_cancel_button)
    TextView buyerCancelButton;
    @BindView(R.id.count_down_text_view)
    TextView countDownTextView;
    @BindView(R.id.has_pay_button)
    LinearLayout hasPayButton;
    @BindView(R.id.buyer_appeal_button)
    TextView buyerAppealButton;
    @BindView(R.id.seller_cancel_button)
    TextView sellerCancelButton;
    @BindView(R.id.wait_button)
    LinearLayout waitButton;
    @BindView(R.id.seller_appeal_button)
    TextView sellerAppealButton;
    @BindView(R.id.seller_get_button)
    TextView sellerGetButton;
    @BindView(R.id.button_container)
    LinearLayout buttonContainer;
    @BindView(R.id.voucher_text_view)
    AppCompatTextView voucherButton;
    @BindView(R.id.voucher_text_view_)
    TextView voucherButtonHintView;

    long mSystemTime;
    String mOrderID;
    String mPayType;
    C2COrder mOrder;
    ClipboardManager mClipboardManager;
    MessageDialog mActionDialog;
    ShowVoucherDialog mVoucherDialog;
    C2CPayAccountPicker mPayTypePicker;
    C2COrderDetailContract.Presenter mOrderDetailPresenter;
    UpdateOrderStatusContract.Presenter mUpdateOrderStatusPresenter;
    BroadcastReceiver mSystemTimeReceiver;


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_order_detail;
    }

    public static void openOrderDetailActivity(Context context, String orderID) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        setNavigationCenter(getString(R.string.c2c_order_detail));
        countDownTextView.setText(String.format(getString(R.string.c2c_remaining_), "--"));
        registerSystemTimeReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchOrderDetail();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mOrderID = bundle.getString("orderID", "");
        }
    }

    private void registerSystemTimeReceiver() {
        if (mSystemTimeReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(SystemTimeService.INTENT_FILTER_SYSTEM_TIME);
            mSystemTimeReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null && intent.hasExtra("systemTime")) {
                        mSystemTime = intent.getLongExtra("systemTime", 0);
                        showCountDown(mSystemTime);
                    }
                }
            };
            registerReceiver(mSystemTimeReceiver, filter);
        }
    }

    @OnClick({R.id.pay_type_container, R.id.buyer_cancel_button, R.id.has_pay_button,
            R.id.buyer_appeal_button, R.id.seller_cancel_button, R.id.seller_appeal_button,
            R.id.seller_get_button, R.id.pay_no_text_view, R.id.account_content_text_view_1,
            R.id.account_content_text_view_2, R.id.account_content_text_view_3,R.id.no_text_view,
            R.id.voucher_text_view})
    public void onViewClicked(View view) {
        ClipData clipData = null;
        switch (view.getId()) {
            case R.id.pay_type_container:
                showPayTypePicker();
                break;
            case R.id.buyer_cancel_button:
            case R.id.seller_cancel_button:
                cancelOrder();
                break;
            case R.id.has_pay_button:
                submitHasPay();
                break;
            case R.id.buyer_appeal_button:
            case R.id.seller_appeal_button:
                AppealActivity.openAppealActivity(this, mOrderID);
                break;
            case R.id.seller_get_button:
                submitHasGetMoney();
                break;
            case R.id.pay_no_text_view:
                String payNo = payNoTextView.getText().toString();
                if (TextUtils.isEmpty(payNo)) return;
                // 创建普通字符型ClipData
                clipData = ClipData.newPlainText("ufo_pay_no", payNo);
                // 将ClipData内容放到系统剪贴板里。
                mClipboardManager.setPrimaryClip(clipData);
                showToast(getString(R.string.exc_wal_copy_success));
                break;
            case R.id.account_content_text_view_1:
                String account1 = accountContentTextView1.getText().toString();
                if (TextUtils.isEmpty(account1)) return;
                // 创建普通字符型ClipData
                clipData = ClipData.newPlainText("ufo_pay_account", account1);
                // 将ClipData内容放到系统剪贴板里。
                mClipboardManager.setPrimaryClip(clipData);
                showToast(getString(R.string.exc_wal_copy_success));
                break;
            case R.id.account_content_text_view_2:
                String account2 = accountContentTextView2.getText().toString();
                if (TextUtils.isEmpty(account2)) return;
                // 创建普通字符型ClipData
                clipData = ClipData.newPlainText("ufo_pay_account", account2);
                // 将ClipData内容放到系统剪贴板里。
                mClipboardManager.setPrimaryClip(clipData);
                showToast(getString(R.string.exc_wal_copy_success));
                break;
            case R.id.account_content_text_view_3:
                String account3 = accountContentTextView3.getText().toString();
                if (TextUtils.isEmpty(account3)) return;
                // 创建普通字符型ClipData
                clipData = ClipData.newPlainText("ufo_pay_account", account3);
                // 将ClipData内容放到系统剪贴板里。
                mClipboardManager.setPrimaryClip(clipData);
                showToast(getString(R.string.exc_wal_copy_success));
                break;
            case R.id.no_text_view:
                String orderNo = noTextView.getText().toString();
                if (TextUtils.isEmpty(orderNo)) return;
                // 创建普通字符型ClipData
                clipData = ClipData.newPlainText("ufo_order_no", orderNo);
                // 将ClipData内容放到系统剪贴板里。
                mClipboardManager.setPrimaryClip(clipData);
                showToast(getString(R.string.exc_wal_copy_success));
                break;
            case R.id.voucher_text_view:
                showVoucherImageDialog();
                break;
        }
    }

    private void showVoucherImageDialog() {
        if (mOrder == null) return;
        if (mVoucherDialog == null) {
            mVoucherDialog = new ShowVoucherDialog(this);
        }
        mVoucherDialog.setTitle(getString(R.string.c2c_payment_voucher));
        mVoucherDialog.setVoucherImage(mOrder.getPayvoucher());
        if (!mVoucherDialog.isShowing()) {
            mVoucherDialog.show();
        }
    }

    private void showPayTypePicker() {
        if (mOrder == null) return;
        if (mPayTypePicker == null) {
            mPayTypePicker = new C2CPayAccountPicker(this);
            mPayTypePicker.setOnPayTypeCheckedListener(this);
        }
        mPayTypePicker.setIsShowBankCard(Constant.C2C_PAY_TYPE_BANK_CARD.equals(mOrder.getTrpay_type())
                || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY.equals(mOrder.getTrpay_type())
                || Constant.C2C_PAY_TYPE_BANK_CARD_WECHAT.equals(mOrder.getTrpay_type())
                || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT.equals(mOrder.getTrpay_type()));
        mPayTypePicker.setIsShowAlipay(Constant.C2C_PAY_TYPE_ALIPAY.equals(mOrder.getTrpay_type())
                || Constant.C2C_PAY_TYPE_ALIPAY_WECHAT.equals(mOrder.getTrpay_type())
                || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY.equals(mOrder.getTrpay_type())
                || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT.equals(mOrder.getTrpay_type()));
        mPayTypePicker.setIsShowWechat(Constant.C2C_PAY_TYPE_WECHAT.equals(mOrder.getTrpay_type())
                || Constant.C2C_PAY_TYPE_ALIPAY_WECHAT.equals(mOrder.getTrpay_type())
                || Constant.C2C_PAY_TYPE_BANK_CARD_WECHAT.equals(mOrder.getTrpay_type())
                || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT.equals(mOrder.getTrpay_type()));
        if (!mPayTypePicker.isShowing()) {
            mPayTypePicker.show();
        }
    }

    private void fetchOrderDetail() {
        if (mOrderDetailPresenter == null) {
            mOrderDetailPresenter = new C2COrderDetailPresenter(this);
        }
        mOrderDetailPresenter.fetchC2COrderDetail(mOrderID);
    }

    @Override
    public void bindC2COrderDetail(C2COrder order) {
        mOrder = order;
        bindOrderStatus();
        bindOrderInfo();
        bindPayType();
        bindActionButton();
    }

    private void bindOrderInfo() {
        if (mOrder != null) {
            noTextView.setText(mOrder.getId());
            payNoTextView.setText(mOrder.getPaycode());
            numHintTextView.setText(String.format(getString(R.string.c2c_transaction_quantity_), mOrder.getCoinname()));
            numTextView.setText(NumberUtil.formatMoney(mOrder.getNums()));
            priceTextView.setText(NumberUtil.formatMoney(mOrder.getPrice()));
            amountTextView.setText(NumberUtil.formatMoney(mOrder.getNums() * mOrder.getPrice()));
            amountTextView_.setText(NumberUtil.formatMoney(mOrder.getNums() * mOrder.getPrice()) + " CNY");

            if (Constant.C2C_TRANSACTION_TYPE_BUY.equals(mOrder.getType())) {
                if (MMKVTools.getUID().equals(mOrder.getUid())) {//买家
                    typeTextView.setText(R.string.c2c_buy);
                    bindBuyerActiobButton(mOrder.getStatus());
                } else {//卖家
                    typeTextView.setText(R.string.c2c_sell);
                }
            } else {
                if (MMKVTools.getUID().equals(mOrder.getUid())) {//卖家
                    typeTextView.setText(R.string.c2c_sell);
                } else {//买家
                    typeTextView.setText(R.string.c2c_buy);
                }
            }

        } else {
            noTextView.setText("--");
            payNoTextView.setText("--");
            numHintTextView.setText("--");
            numTextView.setText("--");
            priceTextView.setText("--");
            amountTextView.setText("--");
            amountTextView_.setText("--");
            typeTextView.setText("--");
        }
    }

    private void bindOrderStatus() {
        if (mOrder != null) {
            if ("1".equals(mOrder.getStatus())) {//未付款
                statusTextView.setText(R.string.c2c_unpaid);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColor8488F5));
                voucherButton.setVisibility(View.GONE);
                voucherButtonHintView.setVisibility(View.GONE);
            } else if ("2".equals(mOrder.getStatus())) {//待确认
                statusTextView.setText(R.string.c2c_to_be_confirmed);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColor8488F5));
                voucherButton.setVisibility(View.VISIBLE);
                voucherButtonHintView.setVisibility(View.VISIBLE);
            } else if ("3".equals(mOrder.getStatus())) {//申诉中
                statusTextView.setText(R.string.c2c_appealing);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColorF95D04));
                voucherButton.setVisibility(View.VISIBLE);
                voucherButtonHintView.setVisibility(View.VISIBLE);
            } else if ("4".equals(mOrder.getStatus())) {//已完成
                statusTextView.setText(R.string.c2c_completed);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColor00DA97));
                voucherButton.setVisibility(View.VISIBLE);
                voucherButtonHintView.setVisibility(View.VISIBLE);
            } else {//交易关闭
                statusTextView.setText(R.string.c2c_deal_closure);
                statusTextView.setTextColor(getResources().getColor(R.color.c2cColorB7B7C4));
                voucherButton.setVisibility(TextUtils.isEmpty(mOrder.getPayvoucher()) ? View.GONE : View.VISIBLE);
                voucherButtonHintView.setVisibility(TextUtils.isEmpty(mOrder.getPayvoucher()) ? View.GONE : View.VISIBLE);
            }
        }
    }

    private void bindActionButton() {
        if (mOrder != null) {
            if (Constant.C2C_TRANSACTION_TYPE_BUY.equals(mOrder.getType())) {
                if (MMKVTools.getUID().equals(mOrder.getUid())) {//买家
                    bindBuyerActiobButton(mOrder.getStatus());
                } else {//卖家
                    bindSellerActiobButton(mOrder.getStatus());
                }
            } else {
                if (MMKVTools.getUID().equals(mOrder.getUid())) {//卖家
                    bindSellerActiobButton(mOrder.getStatus());
                } else {//买家
                    bindBuyerActiobButton(mOrder.getStatus());
                }
            }
        } else {
            buttonDivider.setVisibility(View.GONE);
            buttonContainer.setVisibility(View.GONE);

            sellerCancelButton.setVisibility(View.GONE);//卖家取消按钮
            waitButton.setVisibility(View.GONE);//等待对方付款
            sellerAppealButton.setVisibility(View.GONE);//卖家申诉按钮
            sellerGetButton.setVisibility(View.GONE);//确认收款

            buyerCancelButton.setVisibility(View.GONE);//买家取消按钮
            hasPayButton.setVisibility(View.GONE);//我已付款
            buyerAppealButton.setVisibility(View.GONE);//买家申诉按钮
        }

    }

    private void bindBuyerActiobButton(String status) {
        sellerCancelButton.setVisibility(View.GONE);//卖家取消按钮
        waitButton.setVisibility(View.GONE);//等待对方付款
        sellerAppealButton.setVisibility(View.GONE);//卖家申诉按钮
        sellerGetButton.setVisibility(View.GONE);//确认收款

        if ("1".equals(status)) {//未付款
            buttonDivider.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.VISIBLE);

            buyerCancelButton.setVisibility(View.VISIBLE);//买家取消按钮
            hasPayButton.setVisibility(View.VISIBLE);//我已付款
            buyerAppealButton.setVisibility(View.GONE);//买家申诉按钮

        } else if ("2".equals(status)) {//待确认
            buttonDivider.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.VISIBLE);

            buyerCancelButton.setVisibility(View.GONE);//买家取消按钮
            hasPayButton.setVisibility(View.GONE);//我已付款
            buyerAppealButton.setVisibility(View.VISIBLE);//买家申诉按钮

        } else if ("3".equals(status)) {//申诉中
            buttonDivider.setVisibility(View.GONE);
            buttonContainer.setVisibility(View.GONE);

            buyerCancelButton.setVisibility(View.GONE);//买家取消按钮
            hasPayButton.setVisibility(View.GONE);//我已付款
            buyerAppealButton.setVisibility(View.GONE);//买家申诉按钮

        } else if ("4".equals(status)) {//已完成
            buttonDivider.setVisibility(View.GONE);
            buttonContainer.setVisibility(View.GONE);

            buyerCancelButton.setVisibility(View.GONE);//买家取消按钮
            hasPayButton.setVisibility(View.GONE);//我已付款
            buyerAppealButton.setVisibility(View.GONE);//买家申诉按钮

        } else {//交易关闭
            buttonDivider.setVisibility(View.GONE);
            buttonContainer.setVisibility(View.GONE);

            buyerCancelButton.setVisibility(View.GONE);//买家取消按钮
            hasPayButton.setVisibility(View.GONE);//我已付款
            buyerAppealButton.setVisibility(View.GONE);//买家申诉按钮
        }
    }

    private void bindSellerActiobButton(String status) {
        buyerCancelButton.setVisibility(View.GONE);//买家取消按钮
        hasPayButton.setVisibility(View.GONE);//我已付款
        buyerAppealButton.setVisibility(View.GONE);//买家申诉按钮

        if ("1".equals(status)) {//未付款
            buttonDivider.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.VISIBLE);

            sellerCancelButton.setVisibility(View.VISIBLE);//卖家取消按钮
            waitButton.setVisibility(View.VISIBLE);//等待对方付款
            sellerAppealButton.setVisibility(View.GONE);//卖家申诉按钮
            sellerGetButton.setVisibility(View.GONE);//确认收款

        } else if ("2".equals(status)) {//待确认
            buttonDivider.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.VISIBLE);

            sellerCancelButton.setVisibility(View.GONE);//卖家取消按钮
            waitButton.setVisibility(View.GONE);//等待对方付款
            sellerAppealButton.setVisibility(View.VISIBLE);//卖家申诉按钮
            sellerGetButton.setVisibility(View.VISIBLE);//确认收款

        } else if ("3".equals(status)) {//申诉中
            buttonDivider.setVisibility(View.GONE);
            buttonContainer.setVisibility(View.GONE);

            sellerCancelButton.setVisibility(View.GONE);//卖家取消按钮
            waitButton.setVisibility(View.GONE);//等待对方付款
            sellerAppealButton.setVisibility(View.GONE);//卖家申诉按钮
            sellerGetButton.setVisibility(View.GONE);//确认收款

        } else if ("4".equals(status)) {//已完成
            buttonDivider.setVisibility(View.GONE);
            buttonContainer.setVisibility(View.GONE);

            sellerCancelButton.setVisibility(View.GONE);//卖家取消按钮
            waitButton.setVisibility(View.GONE);//等待对方付款
            sellerAppealButton.setVisibility(View.GONE);//卖家申诉按钮
            sellerGetButton.setVisibility(View.GONE);//确认收款

        } else {//交易关闭
            buttonDivider.setVisibility(View.GONE);
            buttonContainer.setVisibility(View.GONE);

            sellerCancelButton.setVisibility(View.GONE);//卖家取消按钮
            waitButton.setVisibility(View.GONE);//等待对方付款
            sellerAppealButton.setVisibility(View.GONE);//卖家申诉按钮
            sellerGetButton.setVisibility(View.GONE);//确认收款
        }
    }

    private void bindPayType() {
        if (TextUtils.isEmpty(mPayType) && mOrder != null) {
            if (Constant.C2C_PAY_TYPE_BANK_CARD.equals(mOrder.getTrpay_type())
                    || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY.equals(mOrder.getTrpay_type())
                    || Constant.C2C_PAY_TYPE_BANK_CARD_WECHAT.equals(mOrder.getTrpay_type())
                    || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT.equals(mOrder.getTrpay_type())) {
                mPayType = Constant.C2C_PAY_TYPE_BANK_CARD;
            } else if (Constant.C2C_PAY_TYPE_ALIPAY.equals(mOrder.getTrpay_type())
                    || Constant.C2C_PAY_TYPE_ALIPAY_WECHAT.equals(mOrder.getTrpay_type())
                    || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY.equals(mOrder.getTrpay_type())
                    || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT.equals(mOrder.getTrpay_type())) {
                mPayType = Constant.C2C_PAY_TYPE_ALIPAY;
            } else if (Constant.C2C_PAY_TYPE_WECHAT.equals(mOrder.getTrpay_type())
                    || Constant.C2C_PAY_TYPE_ALIPAY_WECHAT.equals(mOrder.getTrpay_type())
                    || Constant.C2C_PAY_TYPE_BANK_CARD_WECHAT.equals(mOrder.getTrpay_type())
                    || Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT.equals(mOrder.getTrpay_type())) {
                mPayType = Constant.C2C_PAY_TYPE_WECHAT;
            }
        }

        if (Constant.C2C_PAY_TYPE_ALIPAY.equals(mPayType)) {
            showAlipayInfo();
        } else if (Constant.C2C_PAY_TYPE_WECHAT.equals(mPayType)) {
            showWechatInfo();
        } else {
            showBankCardInfo();
        }
    }

    @Override
    public void onPayTypeChecked(String payType) {
        mPayType = payType;
        bindPayType();
    }

    private void showBankCardInfo() {
        payTypeIcon.setImageResource(R.drawable.c2c_ic_home_bank_card);
        payTypeTextView.setText(R.string.c2c_bank_card);

        qrImageView.setVisibility(View.GONE);
        accountTitleTextView1.setVisibility(View.VISIBLE);
        accountContentTextView1.setVisibility(View.VISIBLE);
        accountTitleTextView2.setVisibility(View.VISIBLE);
        accountContentTextView2.setVisibility(View.VISIBLE);
        accountTitleTextView3.setVisibility(View.VISIBLE);
        accountContentTextView3.setVisibility(View.VISIBLE);

        accountTitleTextView1.setText(R.string.c2c_receiving_bank);
        accountTitleTextView2.setText(R.string.c2c_account_name);
        accountTitleTextView3.setText(R.string.c2c_account);
        if (mOrder != null && mOrder.getBank() != null) {
            accountContentTextView1.setText(mOrder.getBank().getBanname() + "(" + mOrder.getBank().getFromban() + ")");
            accountContentTextView2.setText(mOrder.getBank().getUsername());
            accountContentTextView3.setText(mOrder.getBank().getBannumber());
        } else {
            accountContentTextView1.setText("--");
            accountContentTextView2.setText("--");
            accountContentTextView3.setText("--");
        }
    }

    private void showAlipayInfo() {
        payTypeIcon.setImageResource(R.drawable.c2c_ic_home_alipay);
        payTypeTextView.setText(R.string.c2c_alipay);

        qrImageView.setVisibility(View.VISIBLE);
        accountTitleTextView1.setVisibility(View.VISIBLE);
        accountContentTextView1.setVisibility(View.VISIBLE);
        accountTitleTextView2.setVisibility(View.GONE);
        accountContentTextView2.setVisibility(View.GONE);
        accountTitleTextView3.setVisibility(View.VISIBLE);
        accountContentTextView3.setVisibility(View.VISIBLE);

        accountTitleTextView1.setText(R.string.c2c_name);
        accountTitleTextView3.setText(R.string.c2c_alipay_account);
        accountTitleTextView2.setText("");
        if (mOrder != null && mOrder.getAlipay() != null) {
            RequestOptions options = new RequestOptions();
            options.error(R.drawable.no_banner);
            GlideLoadUtils.getInstance().glideLoad(this, mOrder.getAlipay().getUrl(), qrImageView, options);
            accountContentTextView1.setText(mOrder.getAlipay().getUsername());
            accountContentTextView3.setText(mOrder.getAlipay().getAccount());
            accountContentTextView2.setText("");
        } else {
            accountContentTextView1.setText("--");
            accountContentTextView3.setText("--");
            accountContentTextView2.setText("");
        }
    }

    private void showWechatInfo() {
        payTypeIcon.setImageResource(R.drawable.c2c_ic_home_wechat);
        payTypeTextView.setText(R.string.c2c_wechat);

        qrImageView.setVisibility(View.VISIBLE);
        accountTitleTextView1.setVisibility(View.VISIBLE);
        accountContentTextView1.setVisibility(View.VISIBLE);
        accountTitleTextView2.setVisibility(View.GONE);
        accountContentTextView2.setVisibility(View.GONE);
        accountTitleTextView3.setVisibility(View.VISIBLE);
        accountContentTextView3.setVisibility(View.VISIBLE);

        accountTitleTextView1.setText(R.string.c2c_wechat_nickname);
        accountTitleTextView3.setText(R.string.c2c_wechat_account);
        accountTitleTextView2.setText("");
        if (mOrder != null && mOrder.getWxpay() != null) {
            RequestOptions options = new RequestOptions();
            options.error(R.drawable.no_banner);
            GlideLoadUtils.getInstance().glideLoad(this, mOrder.getWxpay().getUrl(), qrImageView, options);
            accountContentTextView1.setText(mOrder.getWxpay().getUsername());
            accountContentTextView3.setText(mOrder.getWxpay().getAccount());
            accountContentTextView2.setText("");
        } else {
            accountContentTextView1.setText("--");
            accountContentTextView3.setText("--");
            accountContentTextView2.setText("");
        }
    }

    private void updateOrderStatus(String orderID, String status) {
        if (mUpdateOrderStatusPresenter == null) {
            mUpdateOrderStatusPresenter = new UpdateOrderStatusPresenter(this);
        }
        mUpdateOrderStatusPresenter.updateOrderStatus(orderID, "", "", "", status);
    }

    private void initActionDialog() {
        if (mActionDialog == null) {
            mActionDialog = new MessageDialog(this);
        }
    }

    private void cancelOrder() {
        initActionDialog();
        mActionDialog.setMessage(getString(R.string.c2c_cancel_order_message))
                .setOkText(getString(R.string.c2c_ok))
                .setCancelText(getString(R.string.c2c_cancel))
                .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                    @Override
                    public void onOkButtonClick(MessageDialog dialog) {
                        dialog.dismiss();
                        updateOrderStatus(mOrderID, "5");
                    }
                }).show();
    }

    private void submitHasPay() {
        if (mOrder == null) return;
        C2CCertificateActivity.openC2CCertificateActivity(this, mOrder.getId(), mOrder.getTrpay_type());
    }

    private void submitHasGetMoney() {
        initActionDialog();
        mActionDialog.setMessage(getString(R.string.c2c_have_received_message))
                .setOkText(getString(R.string.c2c_have_received))
                .setCancelText(getString(R.string.c2c_cancel))
                .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                    @Override
                    public void onOkButtonClick(MessageDialog dialog) {
                        dialog.dismiss();
                        updateOrderStatus(mOrderID, "4");
                    }
                }).show();
    }

    private void showCountDown(long systemTime) {
        if (mOrder != null && hasPayButton.getVisibility() == View.VISIBLE) {
            long time = mOrder.getEndtime() - systemTime;
            if (time == 0) {
                fetchOrderDetail();
            }
            countDownTextView.setText(String.format(getString(R.string.c2c_remaining_), buildCountDownTime(time)));
        }
    }

    private String buildCountDownTime(long time) {
        final int MINUTE = 60;
        final int HOUR = 60 * 60;

        String hour_ = "";
        String minute_ = "";
        String second_ = "";
        StringBuilder builder = new StringBuilder("");
        if (time >= 0) {
            long hour = time / HOUR;
            long minute = time % HOUR / MINUTE;
            long second = time % MINUTE;

            if (hour >= 10) {
                hour_ = String.valueOf(hour);
            } else if (hour > 0) {
                hour_ = "0" + String.valueOf(hour);
            }
            if (minute >= 10) {
                minute_ = String.valueOf(minute);
            } else {
                minute_ = "0" + String.valueOf(minute);
            }
            if (second >= 10) {
                second_ = String.valueOf(second);
            } else {
                second_ = "0" + String.valueOf(second);
            }

            if (!TextUtils.isEmpty(hour_)) {
                builder.append(hour_);
                builder.append(":");
            }
            builder.append(minute_);
            builder.append(":");
            builder.append(second_);
        } else {
            builder.append("00:00");
        }
        return builder.toString();
    }


    @Override
    public void onUpdateOrderStatusSuccess() {
        fetchOrderDetail();
    }

    @Override
    protected void onDestroy() {
        if (mOrderDetailPresenter != null) {
            mOrderDetailPresenter.dropView();
        }
        if (mUpdateOrderStatusPresenter != null) {
            mUpdateOrderStatusPresenter.dropView();
        }
        if (mSystemTimeReceiver != null) {
            unregisterReceiver(mSystemTimeReceiver);
        }
        super.onDestroy();
    }

}
