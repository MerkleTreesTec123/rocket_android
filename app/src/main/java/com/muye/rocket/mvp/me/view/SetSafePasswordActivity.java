package com.muye.rocket.mvp.me.view;

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
import com.muye.rocket.mvp.me.contract.SetSafePasswordContract;
import com.muye.rocket.mvp.me.presenter.SetSafePasswordPresenter;
import com.muye.rocket.tools.Validator;

import butterknife.BindView;

/*
* 设置支付密码
* */
public class SetSafePasswordActivity extends BaseActivity implements SetSafePasswordContract.View {
    @BindView(R.id.title_hint_text_view)
    TextView titleHintTextView;
    @BindView(R.id.new_edit_text)
    CustomEditText newEditText;
    @BindView(R.id.new_edit_text_)
    CustomEditText newEditText_;
    @BindView(R.id.google_edit_text)
    CustomEditText googleEditText;

    @BindView(R.id.btn_sure)
    Button btnSure;

    SetSafePasswordContract.Presenter mPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_set_safe_password;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.create_payment_password));
        initView();
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
                if (passwordIsEnable()) {
                    submitPayPassword();
                }
            }
        });
    }

    private boolean passwordIsEnable() {
        String password = newEditText.getInputText();
        String password_ = newEditText_.getInputText();
        if (TextUtils.isEmpty(password)) {
            newEditText.showErrorText(getString(R.string.input_6_12_pay_password_hint));
            return false;
        } else {
            if (!Validator.isPassword(password)) {
                newEditText.showErrorText(getString(R.string.pay_password_format_error));
                return false;
            } else {
                newEditText.hideErrorText();
            }
        }


        if (TextUtils.isEmpty(password_)) {
            newEditText_.showErrorText(getString(R.string.confirm_pay_password_hint));
            return false;
        } else {
            if (!Validator.isPassword(password_)) {
                newEditText_.showErrorText(getString(R.string.pay_password_format_error));
                return false;
            } else if (!password_.equals(password)) {
                newEditText_.showErrorText(getString(R.string.inconsistent_password_entered_twice));
                return false;
            } else {
                newEditText_.hideErrorText();
            }
        }
        return true;
    }

    private void submitPayPassword() {
        if (mPresenter == null) {
            mPresenter = new SetSafePasswordPresenter(this);
        }
        mPresenter.submitPayPassword("", newEditText.getInputText(), "", googleEditText.getInputText(), "");
    }

    @Override
    public void onSubmitPayPasswordSuccess() {
        MMKVTools.savePayPasswordStatus(true);
        finish();
    }

    @Override
    public void onVerifyPhoenCode() {

    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        super.onDestroy();
    }
}
