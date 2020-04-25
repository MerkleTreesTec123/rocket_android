package com.muye.rocket.mvp.me.view;

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
import com.muye.rocket.mvp.me.contract.UpdateLoginPasswordContract;
import com.muye.rocket.mvp.me.presenter.UpdateLoginPasswordPresenter;
import com.muye.rocket.tools.Validator;

import butterknife.BindView;

/*
* 修改登录密码
* */
public class UpdateLoginPasswordActivity extends BaseActivity implements UpdateLoginPasswordContract.View {
    @BindView(R.id.old_edit_text)
    CustomEditText oldEditText;
    @BindView(R.id.new_edit_text)
    CustomEditText newEditText;
    @BindView(R.id.new_edit_text_)
    CustomEditText newEditText_;
    @BindView(R.id.google_edit_text)
    CustomEditText googleEditText;

    @BindView(R.id.btn_sure)
    Button btnSure;
    UpdateLoginPasswordContract.Presenter mPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_update_login_password;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.change_password));
        initView();
    }


    private void initView() {
        oldEditText.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String password = customEditText.getInputText();
                    if (TextUtils.isEmpty(password)) {
                        customEditText.showErrorText(getString(R.string.input_old_password_hint));
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
                            customEditText.showErrorText(getString(R.string.login_password_format_error));
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
                        customEditText.showErrorText(getString(R.string.confirm_login_password));
                    } else {
                        if (!Validator.isPassword(password)) {
                            customEditText.showErrorText(getString(R.string.login_password_format_error));
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

    @Override
    public void onSubmitLoginPasswordSuccess() {
        showToast(getString(R.string.update_password_success));
        finish();
    }

    private void submitPayPassword() {
        String oldPassword = oldEditText.getInputText();
        String password = newEditText.getInputText();
        String password_ = newEditText_.getInputText();
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
                newEditText.showErrorText(getString(R.string.login_password_format_error));
                return;
            } else {
                newEditText.hideErrorText();
            }
        }

        if (TextUtils.isEmpty(password_)) {
            newEditText_.showErrorText(getString(R.string.confirm_login_password));
            return;
        } else {
            if (!Validator.isPassword(password_)) {
                newEditText_.showErrorText(getString(R.string.login_password_format_error));
                return;
            } else if (!password_.equals(password)) {
                newEditText_.showErrorText(getString(R.string.inconsistent_password_entered_twice));
                return;
            } else {
                newEditText_.hideErrorText();
            }
        }

        if (mPresenter == null) {
            mPresenter = new UpdateLoginPasswordPresenter(this);
        }
        mPresenter.submitLoginPassword(oldPassword, password,  googleCode);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        super.onDestroy();
    }
}
