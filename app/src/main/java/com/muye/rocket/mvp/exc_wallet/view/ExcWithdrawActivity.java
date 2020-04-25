package com.muye.rocket.mvp.exc_wallet.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.exc_wallet.ExcAddress;
import com.muye.rocket.entity.exc_wallet.ExcAssetsDetail;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.entity.exc_wallet.ExcWithdrawSetting;
import com.muye.rocket.mvp.account.contract.CountDownTimeContract;
import com.muye.rocket.mvp.account.presenter.CountDownTimePresenter;
import com.muye.rocket.mvp.exc_wallet.contract.ExcSendSMS2BindPhoneContract;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWithdrawContract;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcSendSMS2BindPhonePresenter;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcWithdrawPresenter;
import com.muye.rocket.mvp.me.view.MyBindWalletListsActivity;
import com.muye.rocket.oss.OSSManager;
import com.muye.rocket.tools.GlideLoadUtils;
import com.muye.rocket.tools.filter.NumberFilter;
import com.muye.rocket.widget.dialog.BigImgDialog;
import com.muye.rocket.widget.dialog.CoinPicker;
import com.muye.rocket.widget.dialog.PasswordGoogleDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
* 提币
* */
public class ExcWithdrawActivity extends BaseActivity implements CoinPicker.OnCoinSelectedListener,
        ExcWithdrawContract.View, ExcSendSMS2BindPhoneContract.View, CountDownTimeContract.View,
        PasswordGoogleDialog.InputPasswordCallBack {
    @BindView(R.id.title_text_view)
    TextView titleTextView;
    @BindView(R.id.address_text_view)
    TextView addressTextView;
    @BindView(R.id.num_edit_text)
    EditText numEditText;
    @BindView(R.id.unit_text_view)
    TextView unitTextView;
    @BindView(R.id.balance_text_view)
    TextView balanceTextView;
    @BindView(R.id.service_charge_text_view)
    TextView serviceChargeTextView;
    @BindView(R.id.service_charge_unit_text_view)
    TextView serviceChargeUnitTextView;
    @BindView(R.id.service_charge_hint_text_view)
    TextView serviceChargeHintTextView;
    @BindView(R.id.code_edit_text)
    EditText codeEditText;
    @BindView(R.id.send_button)
    Button sendButton;
    @BindView(R.id.submit_button)
    Button submitButton;

    String mCoinID;
    String mCoinName;
    ExcAddress mAddress;
    ExcAssetsDetail mExcAssetsDetail;
    ExcWithdrawSetting mWithdrawSetting;
    CoinPicker mCoinPicker;
    List<ExcAssetsEntity.WalletBean> mCoinList;
    PasswordGoogleDialog mPasswordDialog;
    ExcWithdrawContract.Presenter mWithdrawPresenter;
    ExcSendSMS2BindPhoneContract.Presenter mSendSMSPresenter;
    CountDownTimeContract.Presenter mCountDownPresenter;
    boolean isFromScan;
    @BindView(R.id.address_tag_title_text_view)
    TextView addressTagTitleTextView;
    @BindView(R.id.address_tag_edit_text)
    EditText addressTagEditText;
    @BindView(R.id.address_tag_divider)
    View addressTagDivider;
    @BindView(R.id.withdraw_img)
    ImageView withdrawImg;

    public static void openExcWithdrawActivity(Context context, String coinID) {
        Intent intent = new Intent(context, ExcWithdrawActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void openExcWithdrawActivity(Context context, String coinID, ExcAddress address) {
        Intent intent = new Intent(context, ExcWithdrawActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        bundle.putParcelable("address", address);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.exc_wal_activity_withdraw;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);

        setNavigationCenter(getString(R.string.exc_wal_withdraw));
        setNavigationRight(R.drawable.exc_wal_ic_record, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mCoinID)) {
                    showToast(getString(R.string.exc_wal_please_select_currency));
                    return;
                }
                ExcWithdrawRecordActivity.openExcWithdrawRecordActivity(ExcWithdrawActivity.this, mCoinID);
            }
        });

        GlideLoadUtils.getInstance().glideLoad(this, OSSManager.WITHDRAW_TOP, withdrawImg);
        setupInputListener();
        numEditText.setFilters(new InputFilter[]{new NumberFilter()});
        fetchCoinList();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
            ExcAddress address = bundle.getParcelable("address");
            if (address != null) {
                isFromScan = true;
                bindAddress(address);
            }
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

        numEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculationFee();
                setSubmitButtonEnable();
            }
        });
        addressTextView.addTextChangedListener(textWatcher);
        codeEditText.addTextChangedListener(textWatcher);
        addressTagEditText.addTextChangedListener(textWatcher);
    }

    private void setSubmitButtonEnable() {
        String num = numEditText.getText().toString();
        String code = codeEditText.getText().toString();
        double num_ = new BigDecimal(NumberUtil.formatMoney(num)).doubleValue();
        String tag = addressTagEditText.getText().toString();
        submitButton.setEnabled(!TextUtils.isEmpty(mCoinID)
                && mAddress != null && !TextUtils.isEmpty(mAddress.getFid()) && mCoinID.equals(mAddress.getFcoinid())
                && !TextUtils.isEmpty(code)
                && mWithdrawSetting != null && num_ >= mWithdrawSetting.getWithdrawMin()
                && mExcAssetsDetail != null && mExcAssetsDetail.getTotal() > 0 && mExcAssetsDetail.getTotal() >= num_
                && (addressTagEditText.getVisibility() != View.VISIBLE || !TextUtils.isEmpty(tag) ));
    }

    /**
     * 计算手续费
     */
    private double calculationFee() {

        if (TextUtils.isEmpty(mCoinID) || mExcAssetsDetail == null || !mCoinID.equals(mExcAssetsDetail.getCoin_id())
                || mWithdrawSetting == null || !mCoinID.equals(mWithdrawSetting.getCoinId())) {
            serviceChargeTextView.setText("--");
            return 0;
        }
        double serviceCharge = 0;

//        String num = numEditText.getText().toString();
//        double num_ = new BigDecimal(NumberUtil.formatMoney(num)).doubleValue();
//        serviceCharge = num_ * mWithdrawSetting.getWithdrawFee();

        serviceCharge = mWithdrawSetting.getWithdrawFee();
        serviceChargeTextView.setText(NumberUtil.formatMoney(serviceCharge) + " " + mExcAssetsDetail.getCoin_name());
        return serviceCharge;
    }

    @OnClick({R.id.coin_container, R.id.address_text_view,
            R.id.all_button, R.id.send_button, R.id.submit_button,R.id.withdraw_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.coin_container:
                showCoinPicker();
                break;
            case R.id.address_text_view:
                if (TextUtils.isEmpty(mCoinID)) {
                    showToast(getString(R.string.exc_wal_please_select_currency));
                    return;
                }
                ExcAddressBookActivity.openExcAddressPicker(this, mCoinID, mAddress == null ? "" : mAddress.getFid(), 1);
                break;
            case R.id.all_button:
                String num = NumberUtil.formatMoney(0);
                if (!TextUtils.isEmpty(mCoinID) && mExcAssetsDetail != null && mCoinID.equals(mExcAssetsDetail.getCoin_id())) {
                    num = NumberUtil.formatMoneyDown(mExcAssetsDetail.getTotal());
                }
                numEditText.setText(num);
                numEditText.setSelection(num.length());
                break;
            case R.id.send_button:
                if (MMKVTools.hasBindPhone()){
                    if (mSendSMSPresenter == null) {
                        mSendSMSPresenter = new ExcSendSMS2BindPhonePresenter(this);
                    }
                    mSendSMSPresenter.sendSMS(ExcSendSMS2BindPhoneContract.SMS_TYPE_ADD_WITHDRAW);
                }else {
                    showToast(getString(R.string.bind_phone_));
                }
                break;
            case R.id.submit_button:
                showPasswordDialog();
                break;
            case R.id.withdraw_img:
                //                if (!TextUtils.isEmpty(mCoinName)){
//                    if (mCoinName.equals("USDT")||mCoinName.equals("CAT")){
                Intent intent = new Intent(this, MyBindWalletListsActivity.class);
                intent.putExtra("intent_type","2"); // 提币跳转
                startActivity(intent);
//                    }
//                }
                break;
        }
    }

    @Override
    public void getPayPassword(String password, String googleCode) {
        if (mWithdrawPresenter == null) {
            mWithdrawPresenter = new ExcWithdrawPresenter(this);
        }
        if (TextUtils.isEmpty(mCoinID)) {
            showToast(getString(R.string.exc_wal_please_select_currency));
            return;
        }
        if (mAddress == null) {
            showToast(getString(R.string.exc_wal_choose_withdrawal_address));
            return;
        }
        String num = NumberUtil.formatMoney(numEditText.getText().toString());
        String smsCode = codeEditText.getText().toString();
        mWithdrawPresenter.submitWithdraw(mCoinID, num, mAddress.getFid(), addressTagEditText.getText().toString(), smsCode, password, googleCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                ExcAddress address = null;
                if (data != null && data.getExtras() != null) {
                    address = data.getExtras().getParcelable("excAddress");
                }
                if (address != null && !TextUtils.isEmpty(address.getFcoinid()) && !address.getFcoinid().equals(mCoinID)) {
                    return;
                }
                bindAddress(address);
            }
        }
    }

    private void fetchCoinList() {
        if (mWithdrawPresenter == null) {
            mWithdrawPresenter = new ExcWithdrawPresenter(this);
        }
        mWithdrawPresenter.fetchCoinList();
    }

    @Override
    public void onCoinSelected(ExcAssetsEntity.WalletBean walletBean, BindWalletBean mWalletBean) {
        bindCoin(walletBean);
    }

    @Override
    public void bindCoinList(List<ExcAssetsEntity.WalletBean> coinList) {
        List<ExcAssetsEntity.WalletBean> walletBeanList = new ArrayList<>();
        if (coinList != null && coinList.size() > 0) {
            for (int i = 0; i < coinList.size(); i++) {
                ExcAssetsEntity.WalletBean walletBean = coinList.get(i);
                if (walletBean != null && !TextUtils.isEmpty(walletBean.getCoinId()) && walletBean.isCanWithdraw()) {
                    walletBeanList.add(walletBean);
                    if (walletBean.getCoinId().equals(mCoinID)) {
                        bindCoin(walletBean);
                    }
                }
            }
        }
        if (walletBeanList.size()>0){
            initCoinPicker();
            mCoinPicker.setData(walletBeanList);
            mCoinList = walletBeanList;
            if (TextUtils.isEmpty(mCoinID)) {
                showCoinPicker();
            }
        }else {
            BigImgDialog imgDialog = new BigImgDialog(this);
            imgDialog.setImg(4);
            imgDialog.show();
        }
    }

    @Override
    public void bindBalance(ExcAssetsDetail assetsDetail) {
        if (!TextUtils.isEmpty(mCoinID) && assetsDetail != null && mCoinID.equals(assetsDetail.getCoin_id())) {
            mExcAssetsDetail = assetsDetail;
            balanceTextView.setText(NumberUtil.formatMoneyDown(assetsDetail.getTotal()) + " " + assetsDetail.getCoin_name());
            setSubmitButtonEnable();
        }
    }

    @Override
    public void bindWithdrawSetting(ExcWithdrawSetting withdrawSetting) {
        if (!TextUtils.isEmpty(mCoinID) && withdrawSetting != null && mCoinID.equals(withdrawSetting.getCoinId())) {
            mWithdrawSetting = withdrawSetting;
            serviceChargeTextView.setText(NumberUtil.formatMoneyDown(mWithdrawSetting.getWithdrawFee()));
            numEditText.setHint(String.format(getString(R.string.exc_wal_min_withdrawal_amount), NumberUtil.formatMoney(mWithdrawSetting.getWithdrawMin())));
            setSubmitButtonEnable();
        }
    }

    private void bindCoin(ExcAssetsEntity.WalletBean coin) {
        mCoinID = coin == null ? "" : coin.getCoinId();

        if (coin == null || !"EOS".equals(coin.getCoinName())) {
            addressTagDivider.setVisibility(View.GONE);
            addressTagTitleTextView.setVisibility(View.GONE);
            addressTagEditText.setVisibility(View.GONE);
        } else {
            addressTagDivider.setVisibility(View.VISIBLE);
            addressTagTitleTextView.setVisibility(View.VISIBLE);
            addressTagEditText.setVisibility(View.VISIBLE);
        }
        //判断选择的币种是否是USDT或者cat，其它的不能提币到钱包
        if ("USDT".equals(coin.getCoinName()) || "CAT".equals(coin.getCoinName())){
            mCoinName = coin.getCoinName();
        }

        //清空余额
        mExcAssetsDetail = null;
        balanceTextView.setText("--");

        //清空手续费
        mWithdrawSetting = null;
        serviceChargeTextView.setText("--");
        numEditText.setHint(String.format(getString(R.string.exc_wal_min_withdrawal_amount), "--"));

        //清空地址
        if (!isFromScan) {
            bindAddress(null);
        } else {
            isFromScan = false;
        }

        if (TextUtils.isEmpty(mCoinID) || coin == null) {
            titleTextView.setText("--");
            unitTextView.setText("--");
            serviceChargeUnitTextView.setText("--");
        } else {
            titleTextView.setText(coin.getShortName());
            unitTextView.setText(coin.getShortName());
            serviceChargeUnitTextView.setText(coin.getShortName());

            if (mWithdrawPresenter == null) {
                mWithdrawPresenter = new ExcWithdrawPresenter(this);
            }
            mWithdrawPresenter.fetchBalance(mCoinID);
            mWithdrawPresenter.fetchWithdrawSetting(mCoinID);
        }
        setSubmitButtonEnable();
    }

    private void bindAddress(ExcAddress address) {
        mAddress = address;
        if (mAddress != null) {
            addressTextView.setText(mAddress.getFadderess());
        } else {
            addressTextView.setText("");
        }
        setSubmitButtonEnable();
    }

    private void initCoinPicker() {
        if (mCoinPicker == null) {
            mCoinPicker = new CoinPicker(this);
            mCoinPicker.setOnCoinSelectedListener(this);
        }
    }

    private void showCoinPicker() {
        initCoinPicker();
        if (!mCoinPicker.isShowing()) {
            mCoinPicker.setCanceledOnTouchOutside(!TextUtils.isEmpty(mCoinID));
            mCoinPicker.show();
        }
    }

    private void showPasswordDialog() {
        if (mPasswordDialog == null) {
            mPasswordDialog = new PasswordGoogleDialog(this);
            mPasswordDialog.setInputPasswordCallBack(this)
                    .setIsShowPassword(true)
                    .setIsShowGoogleCode(MMKVTools.hasBindGoogleCode());
        }
        if (!mPasswordDialog.isShowing()) {
            mPasswordDialog.show();
        }
    }

    @Override
    public void onCountDownTimeStart() {
        sendButton.setEnabled(false);
    }

    @Override
    public void onCountDownTimeEnd() {
        sendButton.setEnabled(true);
        sendButton.setText(R.string.send_sms);
    }

    @Override
    public void showCountDownTime(int time) {
        sendButton.setEnabled(false);
        sendButton.setText(time + "s");
    }

    @Override
    public void onSendSMSSuccess() {
        sendButton.setEnabled(false);
        if (mCountDownPresenter == null) {
            mCountDownPresenter = new CountDownTimePresenter(this);
        }
        mCountDownPresenter.startCountDownTime(60);
    }

    @Override
    public void onSendSMSFail() {
        sendButton.setEnabled(true);
    }


    @Override
    public void onWithdrawSuccess() {
        showToast(getString(R.string.exc_wal_submit_success));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mCountDownPresenter != null) {
            mCountDownPresenter.dropView();
        }
        if (mSendSMSPresenter != null) {
            mSendSMSPresenter.dropView();
        }
        if (mWithdrawPresenter != null) {
            mWithdrawPresenter.dropView();
        }
        super.onDestroy();
    }

}
