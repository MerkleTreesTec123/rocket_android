package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.c2c.C2CReleaseEntity;
import com.muye.rocket.mvp.c2c.contract.C2CBalanceContract;
import com.muye.rocket.mvp.c2c.contract.C2CReleaseDetailContract;
import com.muye.rocket.mvp.c2c.contract.C2CTransactionContract;
import com.muye.rocket.mvp.c2c.presenter.C2CBalancePresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CReleaseDetailPresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CTransactionPresenter;
import com.muye.rocket.tools.StringTools;
import com.muye.rocket.tools.filter.NumberFilter;
import com.muye.rocket.widget.UnderLineEditText;
import com.muye.rocket.widget.dialog.PayPasswordDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class SellActivity extends BaseActivity implements PayPasswordDialog.InputPayPasswordCallBack,
        C2CTransactionContract.View, C2CBalanceContract.View, C2CReleaseDetailContract.View {

    @BindView(R.id.account_text_view)
    TextView accountTextView;
    @BindView(R.id.percentage_text_view)
    TextView percentageTextView;
    @BindView(R.id.percentage_num_text_view)
    TextView percentageNumTextView;
    @BindView(R.id.price_text_view)
    TextView priceTextView;
    @BindView(R.id.wechat_icon)
    ImageView wechatIcon;
    @BindView(R.id.alipay_icon)
    ImageView alipayIcon;
    @BindView(R.id.bank_card_icon)
    ImageView bankCardIcon;
    @BindView(R.id.num_hint_text_view)
    TextView numHintTextView;
    @BindView(R.id.num_edit_text)
    UnderLineEditText numEditText;
    @BindView(R.id.limit_text_view)
    TextView limitTextView;
    @BindView(R.id.amount_text_view)
    TextView amountTextView;
    @BindView(R.id.submit_button)
    Button submitButton;
    @BindView(R.id.balance_text_view)
    TextView balanceTextView;

    String mReleaseID;
    double mBalance;
    C2CReleaseEntity mReleaseEntity;
    PayPasswordDialog mPasswordDialog;
    C2CBalanceContract.Presenter mBalancePresenter;
    C2CTransactionContract.Presenter mPresenter;
    C2CReleaseDetailContract.Presenter mReleaseDetailPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_sell;
    }

    public static void openSellActivity(Context context, String releaseID, String coinID, String coinName, String account,
                                        double total, String price, String payType,
                                        double min, double max, double surplusNum) {
        Intent intent = new Intent(context, SellActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("releaseID", releaseID);
        bundle.putString("coinID", coinID);
        bundle.putString("coinName", coinName);
        bundle.putString("account", account);
        bundle.putDouble("total", total);
        bundle.putString("price", price);
        bundle.putString("payType", payType);
        bundle.putDouble("min", min);
        bundle.putDouble("max", max);
        bundle.putDouble("surplusNum", surplusNum);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        numEditText.setFilters(new InputFilter[]{new NumberFilter()});
        setupInputListener();

        fetchReleaseDetail();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mReleaseID = bundle.getString("releaseID", "");
        }
    }

    private void bindPayType(String payType) {
        if (Constant.C2C_PAY_TYPE_BANK_CARD.equals(payType)) {//银行卡
            bankCardIcon.setVisibility(View.VISIBLE);
            alipayIcon.setVisibility(View.GONE);
            wechatIcon.setVisibility(View.GONE);
        } else if (Constant.C2C_PAY_TYPE_ALIPAY.equals(payType)) {//支付宝
            bankCardIcon.setVisibility(View.GONE);
            alipayIcon.setVisibility(View.VISIBLE);
            wechatIcon.setVisibility(View.GONE);
        } else if (Constant.C2C_PAY_TYPE_WECHAT.equals(payType)) {//微信
            bankCardIcon.setVisibility(View.GONE);
            alipayIcon.setVisibility(View.GONE);
            wechatIcon.setVisibility(View.VISIBLE);
        } else if (Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY.equals(payType)) {//银行卡支付宝
            bankCardIcon.setVisibility(View.VISIBLE);
            alipayIcon.setVisibility(View.VISIBLE);
            wechatIcon.setVisibility(View.GONE);
        } else if (Constant.C2C_PAY_TYPE_BANK_CARD_WECHAT.equals(payType)) {//银行卡微信
            bankCardIcon.setVisibility(View.VISIBLE);
            alipayIcon.setVisibility(View.GONE);
            wechatIcon.setVisibility(View.VISIBLE);
        } else if (Constant.C2C_PAY_TYPE_ALIPAY_WECHAT.equals(payType)) {//支付宝微信
            bankCardIcon.setVisibility(View.GONE);
            alipayIcon.setVisibility(View.VISIBLE);
            wechatIcon.setVisibility(View.VISIBLE);
        } else if (Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT.equals(payType)) {//银行卡支付宝微信
            bankCardIcon.setVisibility(View.VISIBLE);
            alipayIcon.setVisibility(View.VISIBLE);
            wechatIcon.setVisibility(View.VISIBLE);
        } else {
            bankCardIcon.setVisibility(View.GONE);
            alipayIcon.setVisibility(View.GONE);
            wechatIcon.setVisibility(View.GONE);
        }
    }

    private void setupInputListener() {
        numEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSubmitButtonEnable();
            }
        });
    }

    private void setSubmitButtonEnable() {
        if (mReleaseEntity == null) {
            submitButton.setEnabled(false);
            amountTextView.setText(NumberUtil.formatMoney(0));
            return;
        }
        double num = NumberUtil.string2Double(numEditText.getInputText());
        amountTextView.setText(NumberUtil.formatMoney(num * NumberUtil.string2Double(mReleaseEntity.getPrice())));

        if (mReleaseEntity.getNums() >= mReleaseEntity.getMin() && mReleaseEntity.getNums() < mReleaseEntity.getMax()) {
            submitButton.setEnabled(num >= mReleaseEntity.getMin() && num <= mReleaseEntity.getNums() && num <= mBalance);
        } else {
            submitButton.setEnabled(num >= mReleaseEntity.getMin() && num <= mReleaseEntity.getMax() && num <= mBalance);
        }
    }

    @OnClick(R.id.submit_button)
    public void onViewClicked() {
        showPasswordDialog();
    }

    private void showPasswordDialog() {
        if (mPasswordDialog == null) {
            mPasswordDialog = new PayPasswordDialog(this);
            mPasswordDialog.setInputPayPasswordCallBack(this);
        }

        if (!mPasswordDialog.isShowing()) {
            mPasswordDialog.show();
        }
    }

    @Override
    public void getPayPassword(String payPassword) {
        if (mPresenter == null) {
            mPresenter = new C2CTransactionPresenter(this);
        }
        mPresenter.submitTransaction(mReleaseID, NumberUtil.formatMoney(numEditText.getInputText()), Constant.C2C_TRANSACTION_TYPE_SELL, "", payPassword);
    }

    private void fetchReleaseDetail() {
        if (mReleaseDetailPresenter == null) {
            mReleaseDetailPresenter = new C2CReleaseDetailPresenter(this);
        }
        mReleaseDetailPresenter.fetchReleaseDetail("", mReleaseID);
    }

    private void fetchCoinBalance(String coinID) {
        if (mBalancePresenter == null) {
            mBalancePresenter = new C2CBalancePresenter(this);
        }
        mBalancePresenter.fetchCoinBalance(coinID);
    }

    @Override
    public void bindReleaseDetail(C2CReleaseEntity releaseEntity) {
        mReleaseEntity = releaseEntity;
        if (mReleaseEntity != null) {
            setNavigationCenter(String.format(getString(R.string.c2c_sell_), releaseEntity.getCoinname()));

            accountTextView.setText(StringTools.phoneNumberFormat(releaseEntity.getUsername()));
            percentageTextView.setText(releaseEntity.getPercentage());
            percentageNumTextView.setText(NumberUtil.formatMoney(releaseEntity.getTotalnums()));
            priceTextView.setText(NumberUtil.formatMoney(releaseEntity.getPrice()));
            numHintTextView.setText(String.format(getString(R.string.c2c_quantity_of_sale_), releaseEntity.getCoinname()));

            if (releaseEntity.getNums() >= releaseEntity.getMin() && releaseEntity.getNums() < releaseEntity.getMax()) {
                limitTextView.setText(NumberUtil.formatMoney(releaseEntity.getMin()) + " - " + NumberUtil.formatMoney(releaseEntity.getNums()));
            } else {
                limitTextView.setText(NumberUtil.formatMoney(releaseEntity.getMin()) + " - " + NumberUtil.formatMoney(releaseEntity.getMax()));
            }

            bindPayType(releaseEntity.getPay_type());

            mBalance = 0;
            balanceTextView.setText(String.format(getString(R.string.c2c_available_balance_), NumberUtil.formatMoneyDown(0)));
            setSubmitButtonEnable();
            fetchCoinBalance(mReleaseEntity.getCoin_id());
        } else {
            setNavigationCenter(String.format(getString(R.string.c2c_sell_), "--"));

            accountTextView.setText("--");
            percentageTextView.setText("--");
            percentageNumTextView.setText("--");
            priceTextView.setText("--");
            numHintTextView.setText(String.format(getString(R.string.c2c_quantity_of_sale_), "--"));
            limitTextView.setText("--");
            bindPayType("");

            mBalance = 0;
            balanceTextView.setText(String.format(getString(R.string.c2c_available_balance_), NumberUtil.formatMoneyDown(0)));
            setSubmitButtonEnable();
        }
    }

    @Override
    public void onSubmitTransactionSuccess(String orderID) {
        showToast(getString(R.string.c2c_submit_success));
        if (!TextUtils.isEmpty(orderID)) {
            OrderDetailActivity.openOrderDetailActivity(this, orderID);
        }
        finish();
    }

    @Override
    public void bindCoinBalance(String coinID, String coinName, double balance, double freeze) {
        mBalance = balance;
        balanceTextView.setText(String.format(getString(R.string.c2c_available_balance_), NumberUtil.formatMoneyDown(balance)));
        setSubmitButtonEnable();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        if (mBalancePresenter != null) {
            mBalancePresenter.dropView();
        }
        if (mReleaseDetailPresenter != null) {
            mReleaseDetailPresenter.dropView();
        }
        super.onDestroy();
    }


}
