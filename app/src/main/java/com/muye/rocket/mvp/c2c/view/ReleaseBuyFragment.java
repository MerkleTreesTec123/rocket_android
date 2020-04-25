package com.muye.rocket.mvp.c2c.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseFragment;
import com.muye.rocket.entity.c2c.C2CCoin;
import com.muye.rocket.entity.c2c.C2CPayAccount;
import com.muye.rocket.entity.c2c.C2CPayAccountInfo;
import com.muye.rocket.mvp.c2c.contract.C2CCoinListContract;
import com.muye.rocket.mvp.c2c.contract.C2CPayAccountContract;
import com.muye.rocket.mvp.c2c.contract.C2CReleaseContract;
import com.muye.rocket.mvp.c2c.presenter.C2CCoinListPresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CPayAccountPresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CReleasePresenter;
import com.muye.rocket.tools.filter.NumberFilter;
import com.muye.rocket.widget.dialog.C2CCoinPicker;
import com.muye.rocket.widget.dialog.MessageDialog;
import com.muye.rocket.widget.dialog.PayPasswordDialog;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReleaseBuyFragment extends BaseFragment implements C2CCoinPicker.OnCoinSelectedListener,
        PayPasswordDialog.InputPayPasswordCallBack, C2CCoinListContract.View, C2CReleaseContract.View,
        C2CPayAccountContract.View {

    @BindView(R.id.coin_text_view)
    TextView coinTextView;
    @BindView(R.id.price_edit_text)
    EditText priceEditText;
    @BindView(R.id.price_unit_text_view)
    TextView priceUnitTextView;
    @BindView(R.id.num_edit_text)
    EditText numEditText;
    @BindView(R.id.num_unit_text_view)
    TextView numUnitTextView;
    @BindView(R.id.min_edit_text_view)
    EditText minEditTextView;
    @BindView(R.id.max_edit_text)
    EditText maxEditText;
    @BindView(R.id.bank_card_container)
    RelativeLayout bankCardContainer;
    @BindView(R.id.alipay_container)
    RelativeLayout alipayContainer;
    @BindView(R.id.wechat_container)
    RelativeLayout wechatContainer;
    @BindView(R.id.amount_text_view)
    TextView amountTextView;
    @BindView(R.id.submit_button)
    Button submitButton;
    Unbinder unbinder;

    String mCoinID;
    C2CPayAccount mBankInfo;
    C2CPayAccount mAlipayInfo;
    C2CPayAccount mWechatInfo;
    C2CCoin mCheckedCoin;
    C2CCoinPicker mCoinListDialog;
    PayPasswordDialog mPasswordDialog;
    C2CCoinListContract.Presenter mCoinListPresenter;
    C2CReleaseContract.Presenter mReleasePresenter;
    C2CPayAccountContract.Presenter mPayAccountPresenter;


    public static ReleaseBuyFragment newInstance(String coinID) {
        Bundle args = new Bundle();
        args.putString("coinID", coinID);
        ReleaseBuyFragment fragment = new ReleaseBuyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewLayout() {
        return R.layout.c2c_fragment_release_buy;
    }

    @Override
    protected void initView(View parentView) {
        unbinder = ButterKnife.bind(this, parentView);
        priceEditText.setFilters(new InputFilter[]{new NumberFilter()});
        numEditText.setFilters(new InputFilter[]{new NumberFilter()});
        minEditTextView.setFilters(new InputFilter[]{new NumberFilter()});
        maxEditText.setFilters(new InputFilter[]{new NumberFilter()});
        mCoinID = getArguments().getString("coinID", "");
        setupInputListener();
    }

    @Override
    protected void initData() {
        if (mCoinListPresenter == null) {
            mCoinListPresenter = new C2CCoinListPresenter(this);
        }
        mCoinListPresenter.fetchCoinList();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPayAccountPresenter == null) {
            mPayAccountPresenter = new C2CPayAccountPresenter(this);
        }
        mPayAccountPresenter.fetchC2CPayAccountInfo();
    }

    @OnClick({R.id.coin_container, R.id.bank_card_container, R.id.alipay_container, R.id.wechat_container, R.id.submit_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.coin_container:
                showCoinListDialog();
                break;
            case R.id.bank_card_container:
                if (mBankInfo != null) {
                    bankCardContainer.setSelected(!bankCardContainer.isSelected());
                } else {
                    showAddBankDialog();
                }
                setSubmitButtonEnable();
                break;
            case R.id.alipay_container:
                if (mAlipayInfo != null) {
                    alipayContainer.setSelected(!alipayContainer.isSelected());
                } else {
                    showAddAlipayDialog();
                }
                setSubmitButtonEnable();
                break;
            case R.id.wechat_container:
                if (mWechatInfo != null) {
                    wechatContainer.setSelected(!wechatContainer.isSelected());
                } else {
                    showAddWechatDialog();
                }
                setSubmitButtonEnable();
                break;
            case R.id.submit_button:
                showPasswordDialog();
                break;
        }
    }

    private void setupInputListener() {
        TextWatcher textWatcher = new TextWatcher() {
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
        };
        TextWatcher priceTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSubmitButtonEnable();
                amountTextView.setText(NumberUtil.formatMoney(calculationAmount()) + " CNY");
            }
        };

        TextWatcher numTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSubmitButtonEnable();
                amountTextView.setText(NumberUtil.formatMoney(calculationAmount()) + " CNY");
            }
        };

        priceEditText.addTextChangedListener(priceTextWatcher);
        numEditText.addTextChangedListener(numTextWatcher);
        minEditTextView.addTextChangedListener(textWatcher);
        maxEditText.addTextChangedListener(textWatcher);
    }

    private void showAddBankDialog() {
        if (mBankInfo == null) {
            new MessageDialog(getContext())
                    .setMessage(getString(R.string.c2c_not_add_bank_card_hint))
                    .setOkText(getString(R.string.ok))
                    .setCancelText(getString(R.string.cancel))
                    .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                        @Override
                        public void onOkButtonClick(MessageDialog dialog) {
                            dialog.dismiss();
                            Intent intent = new Intent(getContext(), BankAddressActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }

    private void showAddAlipayDialog() {
        if (mAlipayInfo == null) {
            new MessageDialog(getContext())
                    .setMessage(getString(R.string.c2c_not_add_alipay_hint))
                    .setOkText(getString(R.string.ok))
                    .setCancelText(getString(R.string.cancel))
                    .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                        @Override
                        public void onOkButtonClick(MessageDialog dialog) {
                            dialog.dismiss();
                            Intent intent = new Intent(getContext(), AlipayAddressActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }

    private void showAddWechatDialog() {
        if (mWechatInfo == null) {
            new MessageDialog(getContext())
                    .setMessage(getString(R.string.c2c_not_add_wechat_hint))
                    .setOkText(getString(R.string.ok))
                    .setCancelText(getString(R.string.cancel))
                    .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                        @Override
                        public void onOkButtonClick(MessageDialog dialog) {
                            dialog.dismiss();
                            Intent intent = new Intent(getContext(), WeChatAddressActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }

    private double calculationAmount() {
        String price_ = priceEditText.getText().toString();
        String num_ = numEditText.getText().toString();
        double price = new BigDecimal(TextUtils.isEmpty(price_) ? "0" : price_).doubleValue();
        double num = new BigDecimal(TextUtils.isEmpty(num_) ? "0" : num_).doubleValue();
        return price * num;
    }

    private void setSubmitButtonEnable() {
        String price_ = priceEditText.getText().toString();
        String num_ = numEditText.getText().toString();
        String min_ = minEditTextView.getText().toString();
        String max_ = maxEditText.getText().toString();
        double price = new BigDecimal(TextUtils.isEmpty(price_) ? "0" : price_).doubleValue();
        double num = new BigDecimal(TextUtils.isEmpty(num_) ? "0" : num_).doubleValue();
        double min = new BigDecimal(TextUtils.isEmpty(min_) ? "0" : min_).doubleValue();
        double max = new BigDecimal(TextUtils.isEmpty(max_) ? "0" : max_).doubleValue();

        submitButton.setEnabled(mCheckedCoin != null
                && price > 0
                && num > 0
                && min > 0 && min <= num && min <= max
                && max > 0 && max <= num
                && (bankCardContainer.isSelected() || alipayContainer.isSelected() || wechatContainer.isSelected()));
    }

    private void initCoinListDialog() {
        if (mCoinListDialog == null) {
            mCoinListDialog = new C2CCoinPicker(getContext());
            mCoinListDialog.setOnCoinSelectedListener(this);
        }
    }

    private void showCoinListDialog() {
        initCoinListDialog();
        if (!mCoinListDialog.isShowing()) {
            mCoinListDialog.show();
        }
    }

    private void showPasswordDialog() {
        if (mPasswordDialog == null) {
            mPasswordDialog = new PayPasswordDialog(getContext());
            mPasswordDialog.setInputPayPasswordCallBack(this);
        }
        if (!mPasswordDialog.isShowing()) {
            mPasswordDialog.show();
        }
    }

    private void bindCheckedCoin(C2CCoin coin) {
        mCoinID = coin == null ? "" : coin.getId();
        mCheckedCoin = coin;
        coinTextView.setText(mCheckedCoin == null ? "" : mCheckedCoin.getCoinname());
        setSubmitButtonEnable();
    }

    @Override
    public void onCoinSelected(C2CCoin coin) {
        bindCheckedCoin(coin);
    }

    @Override
    public void bindC2CPayAccountInfo(C2CPayAccountInfo accountInfo) {
        if (accountInfo != null) {
            mBankInfo = accountInfo.getBank();
            mAlipayInfo = accountInfo.getAlipay();
            mWechatInfo = accountInfo.getWxpay();
        } else {
            mBankInfo = null;
            mAlipayInfo = null;
            mWechatInfo = null;
        }

        if (bankCardContainer.isSelected()) {
            bankCardContainer.setSelected(mBankInfo != null);
        }

        if (alipayContainer.isSelected()) {
            alipayContainer.setSelected(mAlipayInfo != null);
        }

        if (wechatContainer.isSelected()) {
            wechatContainer.setSelected(mWechatInfo != null);
        }

        setSubmitButtonEnable();
    }

    @Override
    public void bindCoinList(List<C2CCoin> coinList) {
        if (mCheckedCoin == null && coinList != null && coinList.size() > 0) {
            C2CCoin dfCoin = null;
            if (!TextUtils.isEmpty(mCoinID)) {
                for (int i = 0; i < coinList.size(); i++) {
                    C2CCoin coin = coinList.get(i);
                    if (coin != null && mCoinID.equals(coin.getId())) {
                        dfCoin = coin;
                        break;
                    }
                }
            }

            if (dfCoin == null) {
                for (int i = 0; i < coinList.size(); i++) {
                    C2CCoin coin = coinList.get(i);
                    if (coin != null) {
                        dfCoin = coin;
                        break;
                    }
                }
            }
            bindCheckedCoin(dfCoin);
        }
        initCoinListDialog();
        mCoinListDialog.setData(coinList);
    }

    @Override
    public void getPayPassword(String payPassword) {
        if (mReleasePresenter == null) {
            mReleasePresenter = new C2CReleasePresenter(this);
        }
        String price = priceEditText.getText().toString();
        String num = numEditText.getText().toString();
        String min = minEditTextView.getText().toString();
        String max = maxEditText.getText().toString();
        mReleasePresenter.submitC2CRelease(mCheckedCoin.getId(), mCheckedCoin.getCoinname(),
                NumberUtil.formatMoney(price), NumberUtil.formatMoney(num), NumberUtil.formatMoney(min),
                NumberUtil.formatMoney(max), getPayType(), Constant.C2C_TRANSACTION_TYPE_BUY, payPassword);
    }

    @Override
    public void onC2CReleaseSuccess(String releaseID) {
        String num = numEditText.getText().toString();
        double total = new BigDecimal(TextUtils.isEmpty(num) ? "0" : num).doubleValue();

        ReleaseSuccessActivity.openReleaseSuccessActivity(getContext(), releaseID, mCheckedCoin == null ? "" : mCheckedCoin.getCoinname(),
                Constant.C2C_TRANSACTION_TYPE_BUY, total, 0);
        getCurrentActivity().finish();
    }

    @Override
    public void onC2CReleaseFail(int code, String message) {
        onError(code, message);
    }

    private String getPayType() {
        if (bankCardContainer.isSelected() && !alipayContainer.isSelected() && !wechatContainer.isSelected()) {
            return Constant.C2C_PAY_TYPE_BANK_CARD;//银行卡
        } else if (!bankCardContainer.isSelected() && alipayContainer.isSelected() && !wechatContainer.isSelected()) {
            return Constant.C2C_PAY_TYPE_ALIPAY;//支付宝
        } else if (!bankCardContainer.isSelected() && !alipayContainer.isSelected() && wechatContainer.isSelected()) {
            return Constant.C2C_PAY_TYPE_WECHAT;//微信
        } else if (bankCardContainer.isSelected() && alipayContainer.isSelected() && !wechatContainer.isSelected()) {
            return Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY;//银行卡支付宝
        } else if (bankCardContainer.isSelected() && !alipayContainer.isSelected() && wechatContainer.isSelected()) {
            return Constant.C2C_PAY_TYPE_BANK_CARD_WECHAT;//银行卡微信
        } else if (!bankCardContainer.isSelected() && alipayContainer.isSelected() && wechatContainer.isSelected()) {
            return Constant.C2C_PAY_TYPE_ALIPAY_WECHAT;//支付宝微信
        } else if (bankCardContainer.isSelected() && alipayContainer.isSelected() && wechatContainer.isSelected()) {
            return Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT;//银行卡支付宝微信
        } else {
            return "";
        }
    }

    @Override
    public void onDestroyView() {
        if (mCoinListPresenter != null) mCoinListPresenter.dropView();
        if (mReleasePresenter != null) mReleasePresenter.dropView();
        if (mPayAccountPresenter != null) mPayAccountPresenter.dropView();
        super.onDestroyView();
        unbinder.unbind();
    }


}
