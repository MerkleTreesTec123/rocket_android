package com.muye.rocket.mvp.exc_wallet.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.event.AddExcAddressEvent;
import com.muye.rocket.mvp.account.contract.CountDownTimeContract;
import com.muye.rocket.mvp.account.presenter.CountDownTimePresenter;
import com.muye.rocket.mvp.exc_wallet.contract.ExcAddAddressContract;
import com.muye.rocket.mvp.exc_wallet.contract.ExcSendSMS2BindPhoneContract;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcAddAddressPresenter;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcSendSMS2BindPhonePresenter;
import com.muye.rocket.tools.Validator;
import com.muye.rocket.widget.dialog.CoinPicker;
import com.muye.rocket.widget.dialog.GoogleCodeDialog;
import com.muye.rocket.widget.dialog.PayPasswordDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/*
* 添加地址
* */
public class ExcAddAddressActivity extends BaseActivity implements CoinPicker.OnCoinSelectedListener,
        PayPasswordDialog.InputPayPasswordCallBack, GoogleCodeDialog.InputGoogleCodeCallBack,
        ExcAddAddressContract.View, ExcSendSMS2BindPhoneContract.View, CountDownTimeContract.View,
        EasyPermissions.PermissionCallbacks {
    private static final int RC_SCAN_PERM = 123;
    private static final String[] CAMERA_AND_STORAGE =
            {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @BindView(R.id.title_text_view)
    TextView titleTextView;
    @BindView(R.id.address_edit_text)
    EditText addressEditText;
    @BindView(R.id.remark_edit_text)
    EditText remarkEditText;
    @BindView(R.id.code_edit_text)
    EditText codeEditText;
    @BindView(R.id.submit_button)
    Button submitButton;
    @BindView(R.id.send_button)
    Button sendButton;

    boolean isPicker;
    String mCoinID;
    String mPassword;
    String mGoogleCode;
    CoinPicker mCoinPicker;
    PayPasswordDialog mPasswordDialog;
    GoogleCodeDialog mGoogleCodeDialog;
    ExcAddAddressContract.Presenter mAddAddressPresenter;
    ExcSendSMS2BindPhoneContract.Presenter mSendSMSPresenter;
    CountDownTimeContract.Presenter mCountDownPresenter;

    public static void openExcAddAddressActivity(Context context, String coinID, boolean isPicker) {
        Intent intent = new Intent(context, ExcAddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        bundle.putBoolean("isAddressPicker", isPicker);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.exc_wal_activity_add_address;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.exc_wal_create_address));
        addressEditText.setHint(String.format(getString(R.string.exc_wal_input_address_hint), "--"));
        setupInputListener();
        fetchCoinList();
        if (TextUtils.isEmpty(mCoinID)) {
            showCoinPicker();
        }
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
            isPicker = bundle.getBoolean("isAddressPicker", false);
        }
    }

    @OnClick({R.id.coin_container, R.id.scan_button, R.id.submit_button, R.id.send_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.coin_container:
                if (!isPicker) {
                    showCoinPicker();
                } else {
                    showToast(getString(R.string.not_choose_other_coin));
                }
                break;
            case R.id.scan_button:
                openScanPage();
                break;
            case R.id.submit_button:
                showPasswordDialog();
                break;
            case R.id.send_button:
                if (MMKVTools.hasBindPhone()){
                    if (mSendSMSPresenter == null) {
                        mSendSMSPresenter = new ExcSendSMS2BindPhonePresenter(this);
                    }
                    mSendSMSPresenter.sendSMS(ExcSendSMS2BindPhoneContract.SMS_TYPE_ADD_ADDRESS);
                    sendButton.setEnabled(false);
                }else {
                    showToast(getString(R.string.bind_phone_));
                }
                break;
        }
    }

    @AfterPermissionGranted(RC_SCAN_PERM)
    private void openScanPage() {
        if (hasCameraStoragePermission()) {
            ScanActivity.openScanQRForResult(this, 1);
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.request_camera_read_write_permissions),
                    RC_SCAN_PERM, CAMERA_AND_STORAGE);
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

        addressEditText.addTextChangedListener(textWatcher);
        codeEditText.addTextChangedListener(textWatcher);
    }

    public void setSubmitButtonEnable() {
        String address = addressEditText.getText().toString();
        String code = codeEditText.getText().toString();
        submitButton.setEnabled(!TextUtils.isEmpty(mCoinID)
                && !TextUtils.isEmpty(address) && Validator.isCoinAddress(address)
                && !TextUtils.isEmpty(code));
        for (String add: Constant.ARRAY_ADDRESS){
            if (address.equals(add)){
                showToast(getString(R.string.safe_hint_address));
                addressEditText.setText("");
            }
        }
    }

    private void initCoinPicker() {
        if (mCoinPicker == null) {
            mCoinPicker = new CoinPicker(this);
            mCoinPicker.setOnCoinSelectedListener(this);
        }
    }

    private void showPasswordDialog() {
        mPassword = "";
        if (mPasswordDialog == null) {
            mPasswordDialog = new PayPasswordDialog(this);
            mPasswordDialog.setInputPayPasswordCallBack(this);
        }
        if (!mPasswordDialog.isShowing()) {
            mPasswordDialog.show();
        }
    }

    private void showGoogleCodeDialog() {
        mGoogleCode = "";
        if (mGoogleCodeDialog == null) {
            mGoogleCodeDialog = new GoogleCodeDialog(this);
            mGoogleCodeDialog.setInputGoogleCodeCallBack(this);
        }
        if (!mGoogleCodeDialog.isShowing()) {
            mGoogleCodeDialog.show();
        }
    }

    private void showCoinPicker() {
        initCoinPicker();
        mCoinPicker.setCanceledOnTouchOutside(!TextUtils.isEmpty(mCoinID));
        if (!mCoinPicker.isShowing()) {
            mCoinPicker.show();
        }
    }


    @Override
    public void getGoogleCode(String googleCode) {
        mGoogleCode = googleCode;
        submitNewAddress();
    }

    @Override
    public void getPayPassword(String payPassword) {
        mPassword = payPassword;
        if (MMKVTools.hasBindGoogleCode()) {
            showGoogleCodeDialog();
        } else {
            submitNewAddress();
        }
    }

    @Override
    public void onCoinSelected(ExcAssetsEntity.WalletBean walletBean, BindWalletBean bindWalletBean) {
        bindCoin(walletBean);
    }

    private void bindCoin(ExcAssetsEntity.WalletBean walletBean) {

        //暂未开放
        if (walletBean != null){
            if (!walletBean.getCoinName().equals("USDT")){
                showToast(getString(R.string.watting_for_));
                return;
            }
        }

        if (walletBean != null && !TextUtils.isEmpty(walletBean.getCoinId())) {
            mCoinID = walletBean.getCoinId();
            titleTextView.setText(walletBean.getShortName());
            addressEditText.setHint(String.format(getString(R.string.exc_wal_input_address_hint), walletBean.getShortName()));
        } else {
            mCoinID = "";
            addressEditText.setHint(String.format(getString(R.string.exc_wal_input_address_hint), "--"));
            titleTextView.setText("--");
        }
    }

    private void fetchCoinList() {
        if (mAddAddressPresenter == null) {
            mAddAddressPresenter = new ExcAddAddressPresenter(this);
        }
        mAddAddressPresenter.fetchCoinList();
    }

    private void submitNewAddress() {
        String address = addressEditText.getText().toString();
        String remark = remarkEditText.getText().toString();
        String code = codeEditText.getText().toString();
        if (mAddAddressPresenter == null) {
            mAddAddressPresenter = new ExcAddAddressPresenter(this);
        }
        mAddAddressPresenter.submitAddAddress(mCoinID, address, remark, code, mPassword, mGoogleCode);
    }

    @Override
    public void bindCoinList(List<ExcAssetsEntity.WalletBean> coinList) {
        List<ExcAssetsEntity.WalletBean> walletBeanList = new ArrayList<>();
        if (coinList != null && coinList.size() > 0) {
            for (int i = 0; i < coinList.size(); i++) {
                ExcAssetsEntity.WalletBean walletBean = coinList.get(i);
                if (walletBean != null && !TextUtils.isEmpty(walletBean.getCoinId()) && walletBean.isCanWithdraw()) {
                    walletBeanList.add(walletBean);
                    if (!TextUtils.isEmpty(mCoinID) && mCoinID.equals(walletBean.getCoinId())) {
                        bindCoin(walletBean);
                    }
                }
            }
        }
        initCoinPicker();
        mCoinPicker.setData(walletBeanList);
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
    public void onAddAddressSuccess(String coinID) {
        EventBus.getDefault().post(new AddExcAddressEvent(coinID));
        showToast(getString(R.string.exc_wal_add_address_success));
        finish();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    String address = bundle.getString(ScanActivity.BUNDLE_KEY_RESULT, "");
                    addressEditText.setText(address);
                    if (!TextUtils.isEmpty(address)) {
                        addressEditText.setSelection(address.length());
                    }
                }

            }
        }
    }

    private boolean hasCameraStoragePermission() {
        return EasyPermissions.hasPermissions(this, CAMERA_AND_STORAGE);
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

    @Override
    protected void onDestroy() {
        if (mCountDownPresenter != null) {
            mCountDownPresenter.dropView();
        }
        if (mSendSMSPresenter != null) {
            mSendSMSPresenter.dropView();
        }
        if (mAddAddressPresenter != null) {
            mAddAddressPresenter.dropView();
        }
        super.onDestroy();
    }


}
