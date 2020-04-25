package com.muye.rocket.mvp.me.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.widget.CustomEditText;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.mvp.account.contract.CountDownTimeContract;
import com.muye.rocket.mvp.account.presenter.CountDownTimePresenter;
import com.muye.rocket.mvp.me.contract.SendSMSBindContract;
import com.muye.rocket.mvp.me.contract.SetSafePasswordContract;
import com.muye.rocket.mvp.me.presenter.SendSMSBindPresenter;
import com.muye.rocket.mvp.me.presenter.SetSafePasswordPresenter;
import com.muye.rocket.tools.Validator;

import butterknife.BindView;
import butterknife.OnClick;

/*
* 修改支付密码
* */
public class UpdateSafePasswordActivity extends BaseActivity implements SetSafePasswordContract.View, CountDownTimeContract.View, SendSMSBindContract.View {
    @BindView(R.id.old_edit_text)
    CustomEditText oldEditText;
    @BindView(R.id.new_edit_text)
    CustomEditText newEditText;
    @BindView(R.id.new_edit_text_)
    CustomEditText newEditText_;
    @BindView(R.id.id_card_edit_text)
    CustomEditText idCardEditText;
    @BindView(R.id.google_edit_text)
    CustomEditText googleEditText;
    @BindView(R.id.tvPhoneCode)
    TextView tvPhoneCode;
    @BindView(R.id.phone_vercode_text)
    CustomEditText phoneVercode;

    @BindView(R.id.btn_sure)
    Button btnSure;

    SetSafePasswordContract.Presenter mPresenter;
    CountDownTimeContract.Presenter timePresenter;
    SendSMSBindPresenter  smsBindPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_update_safe_password;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.modify_payment_password));
        initView();
    }

    @OnClick({R.id.forget_button,R.id.tvPhoneCode})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.forget_button:
                if (MMKVTools.hasBindPhone()) {
                    Intent intent = new Intent(this, ForgetSafePasswordStep1Activity.class);
                    startActivity(intent);
                } else {
                    showToast(getString(R.string.bind_phone_));
                }
                break;
            case R.id.tvPhoneCode:
                sendSMS();
                break;
        }
    }

    //发送验证码
    @SuppressLint("WrongConstant")
    private void sendSMS() {
        if (smsBindPresenter == null) {
            smsBindPresenter = new SendSMSBindPresenter(this);
        }
        if (MMKVTools.hasBindPhone()) {
            smsBindPresenter.sendSMS(MMKVTools.getArea(), MMKVTools.getPhone(), SendSMSBindContract.SMS_CODE_TYPE_UPDATEPAY);
        }else showToast(getString(R.string.bind_you_phone));

    }

    private void initView() {
        oldEditText.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String password = customEditText.getInputText();
                    if (TextUtils.isEmpty(password)) {
                        customEditText.showErrorText(getString(R.string.old_password_format_error));
                    } else {
                        if (!Validator.isPassword(password)) {
                            customEditText.showErrorText(getString(R.string.old_passord_error));
                        } else {
                            customEditText.hideErrorText();
                        }
                    }
                }
                return false;
            }
        });

        oldEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validator.isPassword(oldEditText.getInputText())) {
                    oldEditText.hideErrorText();
                }
            }
        });

        newEditText.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String password = customEditText.getInputText();
                    if (TextUtils.isEmpty(password)) {
                        customEditText.showErrorText(getString(R.string.input_6_12_pay_password_hint));
                    } else {
                        if (!Validator.isPassword(password)) {
                            customEditText.showErrorText(getString(R.string.pay_password_format_error));
                        } else {
                            customEditText.hideErrorText();
                        }
                    }
                }
                return false;
            }
        });

        newEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newEditText.setLevel(Validator.getPasswordLevel(newEditText.getInputText()));
                if (Validator.isPassword(newEditText.getInputText())) {
                    newEditText.hideErrorText();
                }
            }
        });

        newEditText_.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String password = customEditText.getInputText();
                    if (TextUtils.isEmpty(password)) {
                        customEditText.showErrorText(getString(R.string.confirm_pay_password_hint));
                    } else {
                        if (!Validator.isPassword(password)) {
                            customEditText.showErrorText(getString(R.string.pay_password_format_error));
                        } else {
                            customEditText.hideErrorText();
                        }
                    }
                }
                return false;
            }
        });

        newEditText_.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validator.isPassword(newEditText_.getInputText())) {
                    newEditText_.hideErrorText();
                }
            }
        });

        idCardEditText.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(customEditText.getInputText())) {
                        customEditText.showErrorText(getString(R.string.input_id_number_hint));
                    }
                }
                return false;
            }
        });

        idCardEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(idCardEditText.getInputText())) {
                    idCardEditText.hideErrorText();
                }
            }
        });

        googleEditText.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String code = customEditText.getInputText();
                    if (TextUtils.isEmpty(code)) {
                        googleEditText.showErrorText(getString(R.string.exc_wal_input_google_code));
                    } else {
                        if (code.length() != 6) {
                            googleEditText.showErrorText(getString(R.string.google_code_error));
                        } else {
                            googleEditText.hideErrorText();
                        }
                    }
                }
                return false;
            }
        });

        googleEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = googleEditText.getInputText();
                if (!TextUtils.isEmpty(code) && code.length() == 6) {
                    googleEditText.hideErrorText();
                }
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先校验手机验证码
                if (!TextUtils.isEmpty(phoneVercode.getInputText().trim())){
                    if (mPresenter == null) mPresenter = new SetSafePasswordPresenter(UpdateSafePasswordActivity.this);
                    mPresenter.fetchPhoneCode(phoneVercode.getInputText().trim(),MMKVTools.getPhone());
                }else showToast(getString(R.string.phone_ver_code_iput));
            }
        });
    }

    @Override
    public void onSubmitPayPasswordSuccess() {
        showToast(getString(R.string.reset_pay_password_success));
        finish();
    }

    /**
     * 验证手机验证码成功
     */
    @Override
    public void onVerifyPhoenCode() {
        //手机验证码校验成功，开始修改密码
        submitPayPassword();
    }

    private void submitPayPassword() {
        String oldPassword = oldEditText.getInputText();
        String password = newEditText.getInputText();
        String password_ = newEditText_.getInputText();
        String idCard = idCardEditText.getInputText();
        String googleCode = googleEditText.getInputText();

        if (TextUtils.isEmpty(oldPassword)) {
            oldEditText.showErrorText(getString(R.string.input_old_password_hint));
            return;
        } else {
            if (!Validator.isPassword(oldPassword)) {
                oldEditText.showErrorText(getString(R.string.old_passord_error));
                return;
            } else {
                oldEditText.hideErrorText();
            }
        }

        if (TextUtils.isEmpty(password)) {
            newEditText.showErrorText(getString(R.string.input_6_12_pay_password_hint));
            return;
        } else {
            if (!Validator.isPassword(password)) {
                newEditText.showErrorText(getString(R.string.pay_password_format_error));
                return;
            } else {
                newEditText.hideErrorText();
            }
        }

        if (TextUtils.isEmpty(password_)) {
            newEditText_.showErrorText(getString(R.string.confirm_pay_password_hint));
            return;
        } else {
            if (!Validator.isPassword(password_)) {
                newEditText_.showErrorText(getString(R.string.pay_password_format_error));
                return;
            } else if (!password_.equals(password)) {
                newEditText_.showErrorText(getString(R.string.inconsistent_password_entered_twice));
                return;
            } else {
                newEditText_.hideErrorText();
            }
        }

        if (TextUtils.isEmpty(idCardEditText.getInputText())) {
            idCardEditText.showErrorText(getString(R.string.input_id_number_hint));
            return;
        }

        if (mPresenter == null) {
            mPresenter = new SetSafePasswordPresenter(this);
        }
        //修改请求
        mPresenter.submitPayPassword(oldPassword, password, "", googleCode, idCard);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        super.onDestroy();
    }

    /**
     * 验证码发送成功，开始倒计时
     * @param area
     * @param phone
     */
    @Override
    public void onSendSMSSuccess(String area, String phone) {
        if (timePresenter == null) {
            timePresenter = new CountDownTimePresenter(this);
        }
        timePresenter.startCountDownTime(60);
    }

    @Override
    public void onSendSMSFail() {

    }

    @Override
    public void onBindPhone(String status, String phone) {

    }

    @Override
    public void onBindPhoneFail() {

    }

    @Override
    public void onSendEmailSuccess(String email) {}
    @Override
    public void onSendEmailFail() {}

    @Override
    public void onCountDownTimeStart() {
        tvPhoneCode.setEnabled(false);
    }

    @Override
    public void onCountDownTimeEnd() {
        tvPhoneCode.setEnabled(true);
        tvPhoneCode.setText(getString(R.string.reset_phone_vercode));
    }

    @Override
    public void showCountDownTime(int time) {
        tvPhoneCode.setEnabled(false);
        tvPhoneCode.setText(time + "s" + getString(R.string.reset_phone_vercode));
    }
}
