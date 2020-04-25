package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.c2c.C2CPayAccount;
import com.muye.rocket.mvp.c2c.contract.C2CUpdatePayAccountContract;
import com.muye.rocket.mvp.c2c.presenter.C2CUpdatePayAccountPresenter;
import com.muye.rocket.tools.filter.EditTextFilter;
import com.muye.rocket.widget.dialog.PayPasswordDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * C2C 添加/修改银行卡收款地址
 */
public class BankAddressActivity extends BaseActivity implements PayPasswordDialog.InputPayPasswordCallBack, C2CUpdatePayAccountContract.View {

    @BindView(R.id.cardholder_edit_text)
    EditText cardholderEditText;
    @BindView(R.id.bankCardNumber_edit_text)
    EditText bankCardNumberEditText;
    @BindView(R.id.bank_edit_text)
    EditText bankEditText;
    @BindView(R.id.depositBank_edit_text)
    EditText depositBankEditText;
    @BindView(R.id.submit_button)
    Button submitButton;

    C2CUpdatePayAccountContract.Presenter mPresenter;
    PayPasswordDialog mPasswordDialog;

    C2CPayAccount mAccount;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_bank_address;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    public static void openBankAddressActivity(Context context, C2CPayAccount account) {
        Intent intent = new Intent(context, BankAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("account", account);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_bank_card));

        cardholderEditText.setFilters(new InputFilter[]{new EditTextFilter(60)});
        bankEditText.setFilters(new InputFilter[]{new EditTextFilter(100)});
        depositBankEditText.setFilters(new InputFilter[]{new EditTextFilter(150)});
        setupInputListener();

        if(mAccount!=null){
            cardholderEditText.setText(mAccount.getUsername());
            bankCardNumberEditText.setText(mAccount.getBannumber());
            bankEditText.setText(mAccount.getBanname());
            depositBankEditText.setText(mAccount.getFromban());
        }
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle!=null){
            mAccount=bundle.getParcelable("account");
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
        cardholderEditText.addTextChangedListener(textWatcher);
        bankCardNumberEditText.addTextChangedListener(textWatcher);
        bankEditText.addTextChangedListener(textWatcher);
        depositBankEditText.addTextChangedListener(textWatcher);
    }


    private void setSubmitButtonEnable() {
        String cardholder = cardholderEditText.getText().toString();
        String bankCardNumber = bankCardNumberEditText.getText().toString();
        String bank = bankEditText.getText().toString();
        String depositBank = depositBankEditText.getText().toString();

        submitButton.setEnabled(cardholder.length() > 0
                && bankCardNumber.length() > 0
                && bank.length() > 0
                && depositBank.length() > 0);
    }

    @OnClick(R.id.submit_button)
    void onClicks() {
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
        if (mPresenter == null) mPresenter = new C2CUpdatePayAccountPresenter(this);

        String cardholder = cardholderEditText.getText().toString();
        String bankCardNumber = bankCardNumberEditText.getText().toString();
        String bank = bankEditText.getText().toString();
        String depositBank = depositBankEditText.getText().toString();

        C2CPayAccount account = new C2CPayAccount();
        account.setUsername(cardholder);
        account.setBannumber(bankCardNumber);
        account.setBanname(bank);
        account.setFromban(depositBank);
        Gson gson = new Gson();
        mPresenter.submitC2CPayAccount(gson.toJson(account), "", "", payPassword);
    }

    @Override
    public void onSubmitC2CPayAccountSuccess() {
        showToast(getString(R.string.c2c_submit_success));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.dropView();
        super.onDestroy();
    }
}
