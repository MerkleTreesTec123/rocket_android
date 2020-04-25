package com.muye.rocket.mvp.me.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ifenduo.lib_base.widget.CustomEditText;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.event.ForgetSafePasswordEvent;
import com.muye.rocket.mvp.me.contract.ForgetSafePasswordContract;
import com.muye.rocket.mvp.me.presenter.ForgetSafePasswordPresenter;
import com.muye.rocket.tools.Validator;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class ForgetSafePasswordStep2Activity extends BaseActivity implements ForgetSafePasswordContract.View {
    @BindView(R.id.new_edit_text)
    CustomEditText newEditText;
    @BindView(R.id.new_edit_text_)
    CustomEditText newEditText_;
    @BindView(R.id.google_edit_text)
    CustomEditText googleEditText;

    @BindView(R.id.btn_sure)
    Button btnSure;
    ForgetSafePasswordContract.Presenter mPresenter;
    String mSMSCode;

    public static void openForgetSafePasswordStep2Activity(Context context, String smsCode) {
        Intent intent = new Intent(context, ForgetSafePasswordStep2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("smsCode", smsCode);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_forget_safe_password_step_2;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.reset_payment_password));
        initView();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mSMSCode = bundle.getString("smsCode", "");
        }
    }

    private void initView() {

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
                submitPayPassword();
            }
        });
    }

    private void submitPayPassword() {
        String password = newEditText.getInputText();
        String password_ = newEditText_.getInputText();
        String googleCode = googleEditText.getInputText();

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

        if (mPresenter == null) {
            mPresenter = new ForgetSafePasswordPresenter(this);
        }
        mPresenter.submitPayPassword(password,mSMSCode,googleCode);
    }


    @Override
    public void onSubmitPayPasswordSuccess() {
        EventBus.getDefault().post(new ForgetSafePasswordEvent());
        showToast(getString(R.string.reset_pay_password_success));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        super.onDestroy();
    }
}
