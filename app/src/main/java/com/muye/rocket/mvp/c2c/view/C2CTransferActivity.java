package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.c2c.C2CCoin;
import com.muye.rocket.mvp.c2c.contract.AssetsBalanceContract;
import com.muye.rocket.mvp.c2c.contract.C2CBalanceContract;
import com.muye.rocket.mvp.c2c.contract.C2CCoinListContract;
import com.muye.rocket.mvp.c2c.contract.C2CTransferContract;
import com.muye.rocket.mvp.c2c.presenter.AssetsBalancePresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CBalancePresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CCoinListPresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CTransferPresenter;
import com.muye.rocket.tools.filter.NumberFilter;
import com.muye.rocket.widget.dialog.C2CCoinPicker;
import com.muye.rocket.widget.dialog.PayPasswordDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class C2CTransferActivity extends BaseActivity implements C2CCoinListContract.View,
        C2CCoinPicker.OnCoinSelectedListener, C2CBalanceContract.View, AssetsBalanceContract.View,
        C2CTransferContract.View, PayPasswordDialog.InputPayPasswordCallBack {
    @BindView(R.id.coin_text_view)
    TextView coinTextView;
    @BindView(R.id.from_text_view)
    TextView fromTextView;
    @BindView(R.id.to_text_view)
    TextView toTextView;
    @BindView(R.id.num_edit_text)
    EditText numEditText;
    @BindView(R.id.num_coin_text_view)
    TextView numCoinTextView;
    @BindView(R.id.balance_text_view)
    TextView balanceTextView;
    @BindView(R.id.submit_button)
    Button submitButton;

    String mTransferType;
    String mCoinID;
    C2CCoin mCoin;
    double mBalance = 0;
    C2CCoinPicker mCoinPicker;
    PayPasswordDialog mPasswordDialog;
    C2CCoinListContract.Presenter mCoinListPresenter;
    C2CBalanceContract.Presenter mC2CBalancePresenter;
    AssetsBalanceContract.Presenter mAssetsBalancePresenter;
    C2CTransferContract.Presenter mTransferPresenter;

    public static void openC2CTransferActivity(Context context, String coinID) {
        Intent intent = new Intent(context, C2CTransferActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_transfer;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_transfer));
        setNavigationRight(R.drawable.c2c_ic_transfer_record, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(C2CTransferActivity.this, C2CTransferRecordActivity.class);
                startActivity(intent);
            }
        });
        numEditText.setFilters(new InputFilter[]{new NumberFilter()});
        setupInputListener();

        changeTransferType(Constant.C2C_TRANSFER_ASSETS_TO_C2C);
        fetchCoinList();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
        }
    }

    private void setupInputListener() {
        numEditText.addTextChangedListener(new TextWatcher() {
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

    @OnClick({R.id.coin_container, R.id.all_button, R.id.submit_button, R.id.exchange_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.coin_container:
                showCoinPicker();
                break;
            case R.id.all_button:
                numEditText.setText(NumberUtil.formatMoneyDown(mBalance));
                break;
            case R.id.submit_button:
                if (mPasswordDialog == null) {
                    mPasswordDialog = new PayPasswordDialog(this);
                    mPasswordDialog.setInputPayPasswordCallBack(this);
                }
                if (!mPasswordDialog.isShowing()) {
                    mPasswordDialog.show();
                }
                break;
            case R.id.exchange_button:
                changeTransferType(Constant.C2C_TRANSFER_C2C_TO_ASSETS.equals(mTransferType)
                        ? Constant.C2C_TRANSFER_ASSETS_TO_C2C : Constant.C2C_TRANSFER_C2C_TO_ASSETS);
                break;
        }
    }

    private void changeTransferType(String transferType) {
        mTransferType = transferType;
        if (Constant.C2C_TRANSFER_C2C_TO_ASSETS.equals(mTransferType)) {
            fromTextView.setText(R.string.c2c_c2c_wallet);
            toTextView.setText(R.string.c2c_assets);
        } else {
            fromTextView.setText(R.string.c2c_assets);
            toTextView.setText(R.string.c2c_c2c_wallet);
        }
        fetchBalance();
    }

    private void setSubmitButtonEnable() {
        double num = NumberUtil.string2Double(numEditText.getText().toString());
        submitButton.setEnabled(mCoin != null && mBalance > 0 && num > 0 && num <= mBalance);
    }

    private void showCoinPicker() {
        initCoinPicker();
        if (!mCoinPicker.isShowing()) {
            mCoinPicker.show();
        }
    }

    private void initCoinPicker() {
        if (mCoinPicker == null) {
            mCoinPicker = new C2CCoinPicker(this);
            mCoinPicker.setOnCoinSelectedListener(this);
        }
    }


    private void fetchCoinList() {
        if (mCoinListPresenter == null) {
            mCoinListPresenter = new C2CCoinListPresenter(this);
        }
        mCoinListPresenter.fetchCoinList();
    }

    @Override
    public void bindCoinList(List<C2CCoin> coinList) {
        initCoinPicker();
        if (coinList != null && coinList.size() > 0) {
            C2CCoin dfCoin = null;
            for (int i = 0; i < coinList.size(); i++) {
                C2CCoin coin = coinList.get(i);
                if (coin != null) {
                    if (!TextUtils.isEmpty(mCoinID)) {
                        if (mCoinID.equals(coin.getId())) {
                            dfCoin = coin;
                            break;
                        }
                    }
                }
            }

            if (dfCoin == null) {
                for (int i = 0; i < coinList.size(); i++) {
                    C2CCoin coin = coinList.get(i);
                    if (coin != null) {
                        if (dfCoin == null) {
                            dfCoin = coin;
                            break;
                        }
                    }
                }
            }
            if (dfCoin != null) bindCoin(dfCoin);
        }
        mCoinPicker.setData(coinList);
    }

    @Override
    public void onCoinSelected(C2CCoin coin) {
        bindCoin(coin);
    }

    private void bindCoin(C2CCoin coin) {
        mCoin = coin;
        if (mCoin != null) {
            numCoinTextView.setText(coin.getCoinname());
            coinTextView.setText(coin.getCoinname());
            mCoinID = coin.getId();
            initCoinPicker();
            mCoinPicker.setSelectedCoin(coin.getId());
        } else {
            numCoinTextView.setText("--");
            coinTextView.setText("--");
            mCoinID = "";
        }
        fetchBalance();
    }

    private void cleanBalance() {
        mBalance = 0;
        balanceTextView.setText(mCoin == null ? "" : ("-- " + mCoin.getCoinname()));
        setSubmitButtonEnable();
    }

    private void fetchBalance() {
        cleanBalance();
        if (Constant.C2C_TRANSFER_C2C_TO_ASSETS.equals(mTransferType)) {
            fetchC2CWalletBalance();
        } else {
            fetchAssetsWalletBalance();
        }
    }

    //获取C2C钱包余额
    private void fetchC2CWalletBalance() {
        if (mCoin == null) return;
        if (mC2CBalancePresenter == null) {
            mC2CBalancePresenter = new C2CBalancePresenter(this);
        }
        mC2CBalancePresenter.fetchCoinBalance(mCoin.getId());
    }

    //获取资产钱包余额
    private void fetchAssetsWalletBalance() {
        if (mCoin == null) return;
        if (mAssetsBalancePresenter == null) {
            mAssetsBalancePresenter = new AssetsBalancePresenter(this);
        }
        mAssetsBalancePresenter.fetchAssetsBalance(mCoin.getId());
    }

    //C2C钱包余额
    @Override
    public void bindCoinBalance(String coinID, String coinName, double balance, double freeze) {
        if (mCoin != null && mCoin.getId().equals(coinID) && Constant.C2C_TRANSFER_C2C_TO_ASSETS.equals(mTransferType)) {
            mBalance = balance;
            balanceTextView.setText(NumberUtil.formatMoneyDown(balance) + " " + mCoin.getCoinname());
        }
        setSubmitButtonEnable();
    }

    //资产钱包余额
    @Override
    public void bindAssetsBalance(String coinID, String coinName, double balance, double freeze) {
        if (mCoin != null && mCoin.getId().equals(coinID) && Constant.C2C_TRANSFER_ASSETS_TO_C2C.equals(mTransferType)) {
            balanceTextView.setText(NumberUtil.formatMoneyDown(balance) + " " + mCoin.getCoinname());
            mBalance = balance;
        }
        setSubmitButtonEnable();
    }

    @Override
    public void getPayPassword(String payPassword) {
        if (mCoin == null) return;
        if (mTransferPresenter == null) {
            mTransferPresenter = new C2CTransferPresenter(this);
        }
        mTransferPresenter.submitTransferSuccess(mCoin.getId(), NumberUtil.formatMoney(numEditText.getText().toString()), mTransferType, payPassword);
    }

    @Override
    public void onTransferSuccess() {
        showToast(getString(R.string.c2c_transfer_successful));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mCoinListPresenter != null) {
            mCoinListPresenter.dropView();
        }
        if (mC2CBalancePresenter != null) {
            mC2CBalancePresenter.dropView();
        }
        if (mAssetsBalancePresenter != null) {
            mAssetsBalancePresenter.dropView();
        }
        if (mTransferPresenter != null) {
            mTransferPresenter.dropView();
        }
        super.onDestroy();
    }


}
