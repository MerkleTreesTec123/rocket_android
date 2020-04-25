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
import com.muye.rocket.mvp.c2c.contract.C2CReleaseDetailContract;
import com.muye.rocket.mvp.c2c.contract.C2CTransactionContract;
import com.muye.rocket.mvp.c2c.presenter.C2CReleaseDetailPresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CTransactionPresenter;
import com.muye.rocket.tools.StringTools;
import com.muye.rocket.tools.filter.NumberFilter;
import com.muye.rocket.widget.UnderLineEditText;
import com.muye.rocket.widget.dialog.PayPasswordDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class BuyActivity extends BaseActivity implements PayPasswordDialog.InputPayPasswordCallBack,
        C2CTransactionContract.View, C2CReleaseDetailContract.View {

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
    @BindView(R.id.term_text_view)
    TextView termTextView;
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

    String mReleaseID;
    C2CReleaseEntity mReleaseEntity;
    C2CTransactionContract.Presenter mPresenter;
    C2CReleaseDetailContract.Presenter mReleaseDetailPresenter;
    PayPasswordDialog mPasswordDialog;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_buy;
    }

    public static void openBuyActivity(Context context, String releaseID) {
        Intent intent = new Intent(context, BuyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("releaseID", releaseID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        numEditText.setFilters(new InputFilter[]{new NumberFilter()});
        setupInputListener();
        termTextView.setText(String.format(getString(R.string.c2c__minute), "15"));

        fetchReleaseInfo();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mReleaseID = bundle.getString("releaseID", "");
        }
    }

    private void fetchReleaseInfo() {
        if (mReleaseDetailPresenter == null) {
            mReleaseDetailPresenter = new C2CReleaseDetailPresenter(this);
        }
        mReleaseDetailPresenter.fetchReleaseDetail("",mReleaseID);
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
            submitButton.setEnabled(num >= mReleaseEntity.getMin() && num <= mReleaseEntity.getNums());
        } else {
            submitButton.setEnabled(num >= mReleaseEntity.getMin() && num <= mReleaseEntity.getMax());
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
    public void bindReleaseDetail(C2CReleaseEntity releaseEntity) {
        mReleaseEntity = releaseEntity;
        if (mReleaseEntity != null) {
            setNavigationCenter(String.format(getString(R.string.c2c_purchase_), releaseEntity.getCoinname()));

            accountTextView.setText(StringTools.phoneNumberFormat(releaseEntity.getUsername()));
            percentageTextView.setText(releaseEntity.getPercentage());
            percentageNumTextView.setText(NumberUtil.formatMoney(releaseEntity.getTotalnums()));
            priceTextView.setText(NumberUtil.formatMoney(releaseEntity.getPrice()));
            numHintTextView.setText(String.format(getString(R.string.c2c_buying_quantity_), releaseEntity.getCoinname()));

            if (releaseEntity.getNums() >= releaseEntity.getMin() && releaseEntity.getNums() < releaseEntity.getMax()) {
                limitTextView.setText(NumberUtil.formatMoney(releaseEntity.getMin()) + " - " + NumberUtil.formatMoney(releaseEntity.getNums()));
            } else {
                limitTextView.setText(NumberUtil.formatMoney(releaseEntity.getMin()) + " - " + NumberUtil.formatMoney(releaseEntity.getMax()));
            }

            bindPayType(releaseEntity.getPay_type());
        } else {
            setNavigationCenter(String.format(getString(R.string.c2c_purchase_), "--"));

            accountTextView.setText("--");
            percentageTextView.setText("--");
            percentageNumTextView.setText("--");
            priceTextView.setText("--");
            numHintTextView.setText(String.format(getString(R.string.c2c_buying_quantity_), "--"));
            limitTextView.setText("--");
            bindPayType("");
        }

        setSubmitButtonEnable();
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

    @Override
    public void getPayPassword(String payPassword) {
        if (mPresenter == null) {
            mPresenter = new C2CTransactionPresenter(this);
        }
        mPresenter.submitTransaction(mReleaseID, NumberUtil.formatMoney(numEditText.getInputText()), Constant.C2C_TRANSACTION_TYPE_BUY, "", payPassword);
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
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        if (mReleaseDetailPresenter != null) {
            mReleaseDetailPresenter.dropView();
        }
        super.onDestroy();
    }
}
