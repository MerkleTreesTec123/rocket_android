package com.muye.rocket.mvp.account.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ifenduo.lib_base.widget.CustomEditText;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.mvp.account.contract.RegisterContract;
import com.muye.rocket.mvp.account.presenter.RegisterPresenter;
import com.muye.rocket.tools.Validator;

import butterknife.BindView;
import butterknife.OnClick;

/*
* 设置支付密码
* */
public class PayPasswordActivity extends BaseActivity implements RegisterContract.View {

    @BindView(R.id.password_edit_text)
    CustomEditText passwordEditText;
    @BindView(R.id.password_edit_text_)
    CustomEditText passwordEditText_;

    String mPhoneArea;
    String mPhone;
    String mSMSCode;
    String mLoginPassword;
    String mInviteCode;
    RegisterContract.Presenter mRegisterPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_pay_password;
    }

    public static void openPayPasswordActivity(Context context, String phoneArea, String phone, String SMSCode, String loginPassword, String inviteCode) {
        Intent intent = new Intent(context, PayPasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("phoneArea", phoneArea);
        bundle.putString("phone", phone);
        bundle.putString("SMSCode", SMSCode);
        bundle.putString("loginPassword", loginPassword);
        bundle.putString("inviteCode", inviteCode);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        initView();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mPhoneArea = bundle.getString("phoneArea", "");
            mPhone = bundle.getString("phone", "");
            mSMSCode = bundle.getString("SMSCode", "");
            mLoginPassword = bundle.getString("loginPassword", "");
            mInviteCode = bundle.getString("inviteCode", "");
        }
    }

    @OnClick({R.id.tv_next_step})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_next_step) {
            setPayPassword();
        }
    }

    private void initView() {
        passwordEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordEditText.getInputText();
                passwordEditText.setLevel(Validator.getPasswordLevel(password));
                if (Validator.isPassword(password)) {
                    passwordEditText.hideErrorText();
                }
                passwordEditText.setCleanButtonEnable(!TextUtils.isEmpty(password));
            }
        });

        passwordEditText_.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordEditText.getInputText();
                String password_ = passwordEditText_.getInputText();
                if (!TextUtils.isEmpty(password_)) {
                    if (password_.equals(password)) {
                        passwordEditText_.hideErrorText();
                    } else {
                        passwordEditText_.showErrorText(getString(R.string.inconsistent_password_entered_twice));
                    }
                } else {
                    passwordEditText_.showErrorText(getString(R.string.confirm_pay_password_hint));
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String password = customEditText.getInputText();
                    if (TextUtils.isEmpty(password)) {
                        customEditText.showErrorText(getString(R.string.input_pay_password_hint));
                    } else {
                        if (!Validator.isPassword(customEditText.getInputText())) {
                            customEditText.showErrorText(getString(R.string.pay_password_format_error));
                        } else if (customEditText.getInputText().equals(mLoginPassword)) {
                            customEditText.showErrorText(getString(R.string.pay_password_cant_not_equals_login_password));
                        } else {
                            customEditText.hideErrorText();
                        }
                    }
                }
                return false;
            }
        });

        passwordEditText_.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String password_ = customEditText.getInputText();
                    if (TextUtils.isEmpty(password_)) {
                        customEditText.showErrorText(getString(R.string.confirm_pay_password_hint));
                    } else {
                        String password = passwordEditText.getInputText();
                        if (Validator.isPassword(password) && !password_.equals(password)) {
                            customEditText.showErrorText(getString(R.string.inconsistent_password_entered_twice));
                        } else {
                            customEditText.hideErrorText();
                        }
                    }
                }
                return false;
            }
        });
    }

    private void setPayPassword() {
        String password = passwordEditText.getInputText();
        String password_ = passwordEditText_.getInputText();

        if (TextUtils.isEmpty(password)) {
            passwordEditText.showErrorText(getString(R.string.input_pay_password_hint));
            return;
        } else {
            if (!Validator.isPassword(password)) {
                passwordEditText.showErrorText(getString(R.string.pay_password_format_error));
                return;
            } else if (password.equals(mLoginPassword)) {
                passwordEditText.showErrorText(getString(R.string.pay_password_cant_not_equals_login_password));
                return;
            } else {
                passwordEditText.hideErrorText();
            }
        }

        if (TextUtils.isEmpty(password_)) {
            passwordEditText_.showErrorText(getString(R.string.confirm_pay_password_hint));
            return;
        } else {
            if (!password_.equals(password)) {
                passwordEditText_.showErrorText(getString(R.string.inconsistent_password_entered_twice));
                return;
            } else {
                passwordEditText_.hideErrorText();
            }
        }

        submitRegister(password);
    }

    private void submitRegister(String payPassword) {
        if (mRegisterPresenter == null) {
            mRegisterPresenter = new RegisterPresenter(this);
        }
        if (!TextUtils.isEmpty(mPhoneArea)) {
            mRegisterPresenter.submitRegister(mPhoneArea, mPhone, mSMSCode, mLoginPassword, payPassword, mInviteCode);
        }else {
            mRegisterPresenter.submitEmailRegister(mPhone, mSMSCode, mLoginPassword, payPassword, mInviteCode);
        }
    }

    /*
    * 注册成功跳转设置手势密码页面
    * */
    @Override
    public void onRegisterSuccess() {
        CreateLockActivity.openCreateLockActivity(this,"regist");//set
    }

    @Override
    protected void onDestroy() {
        if (mRegisterPresenter != null) {
            mRegisterPresenter.dropView();
        }
        super.onDestroy();
    }

}
