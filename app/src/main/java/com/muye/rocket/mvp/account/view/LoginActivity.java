package com.muye.rocket.mvp.account.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.muye.rocket.entity.LoginEntity;
import com.muye.rocket.entity.PhoneArea;
import com.muye.rocket.event.RegisterEvent;
import com.muye.rocket.mvp.account.contract.LoginContract;
import com.muye.rocket.mvp.account.contract.PhoneAreaContract;
import com.muye.rocket.mvp.account.presenter.LoginPresenter;
import com.muye.rocket.mvp.account.presenter.PhoneAreaPresenter;
import com.muye.rocket.mvp.home.view.HomeActivity;
import com.muye.rocket.tools.Validator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements PhoneAreaContract.View, CustomEditText.OnLeftActionClickListener, LoginContract.View {
    @BindView(R.id.phone_edit_text)
    CustomEditText phoneEditText;
    @BindView(R.id.password_edit_text)
    CustomEditText passwordEditText;
    @BindView(R.id.register_button)
    TextView registerButton;
    @BindView(R.id.submit_button)
    Button submitButton;

    PhoneArea mArea;
    PhoneAreaContract.Presenter mPhoneAreaPresenter;
    LoginContract.Presenter mLoginPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        EventBus.getDefault().register(this);
        phoneEditText.setOnLeftActionClickListener(this);
        setupFocusChangeListener();
        setupInputListener();
        fetchPhoneArea();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initInput();
    }

    private void initInput() {
        phoneEditText.getEditText().setText(MMKVTools.getUserName());
        passwordEditText.getEditText().setText(MMKVTools.getLoginPassword());
    }

    private void setupFocusChangeListener() {
        phoneEditText.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String phone = customEditText.getInputText();
                    if (TextUtils.isEmpty(phone)) {
                        customEditText.showErrorText(getString(R.string.input_phone_email));
                    } else {
                        if (Validator.isMobile(phone)) {   // 是手机号
                            customEditText.hideErrorText();
                        }else if (Validator.isEmail(phone)){// 是邮箱
                            customEditText.hideErrorText();
                        } else {                             // 都不是
                            customEditText.showErrorText(getString(R.string.input_correct_phone));
                        }
                    }
                }
                return false;
            }
        });

        passwordEditText.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String password = customEditText.getInputText();
                    if (TextUtils.isEmpty(password)) {
                        customEditText.showErrorText(getString(R.string.input_password_hint));
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
    }

    private void setupInputListener() {
        phoneEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validator.isMobile(phoneEditText.getInputText()) || Validator.isEmail(phoneEditText.getInputText())) {
                    phoneEditText.hideErrorText();
                }
                setSubmitButtonEnable();

                phoneEditText.setCleanButtonEnable(!TextUtils.isEmpty(phoneEditText.getInputText()));
            }
        });

        passwordEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validator.isPassword(passwordEditText.getInputText())) {
                    passwordEditText.hideErrorText();
                }
                setSubmitButtonEnable();
            }
        });
    }

    private void setSubmitButtonEnable() {
        String phone = phoneEditText.getInputText();
        String password = passwordEditText.getInputText();
        submitButton.setEnabled((Validator.isMobile(phone)||Validator.isEmail(phone)) && Validator.isPassword(password));
    }

    private void fetchPhoneArea() {
        if (mPhoneAreaPresenter == null) {
            mPhoneAreaPresenter = new PhoneAreaPresenter(this);
        }
        mPhoneAreaPresenter.fetchPhoneArea();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null && data.getExtras() != null) {
                    PhoneArea area = data.getExtras().getParcelable(PhoneAreaActivity.BUNDLE_KEY_AREA);
                    if (area != null) {
                        bindArea(area);
                    }
                }
            }
        }
    }

    private PhoneArea getPhoneAreaByCache(List<PhoneArea> areaList) {
        PhoneArea phoneArea = null;
        String cacheAreaCode = MMKVTools.getArea();
        String phone = MMKVTools.getArea();

        if (!TextUtils.isEmpty(cacheAreaCode) && !TextUtils.isEmpty(phone) && areaList != null && areaList.size() > 0) {
            int cacheAreaCode_ = -1;
            try {
                cacheAreaCode_ = Integer.parseInt(cacheAreaCode);
                if (cacheAreaCode_ > 0) {
                    for (PhoneArea area : areaList) {
                        if (area != null && cacheAreaCode_ == area.getCode()) {
                            phoneArea = area;
                            break;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return phoneArea;
            }
        }
        return phoneArea;
    }

    private PhoneArea getPhoneAreaByLocale(List<PhoneArea> areaList) {
        PhoneArea phoneArea = null;
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        if (!TextUtils.isEmpty(locale.getCountry()) && areaList != null && areaList.size() > 0) {
            for (PhoneArea area : areaList) {
                if (area != null && locale.getCountry().equals(area.getLocale())) {
                    phoneArea = area;
                    break;
                }
            }
        }
        return phoneArea;
    }

    @Override
    public void bindPhoneArea(List<PhoneArea> areaList) {
        PhoneArea phoneArea = getPhoneAreaByCache(areaList);

        if (phoneArea == null) {
            phoneArea = getPhoneAreaByLocale(areaList);
        }

        if (phoneArea == null) {
            phoneArea = new PhoneArea();
            phoneArea.setCode(86);
            phoneArea.setEn("China");
            phoneArea.setZh("中国");
            phoneArea.setLocale("CN");
        }
        bindArea(phoneArea);
    }

    private void bindArea(PhoneArea area) {
        if (area == null) {
            area = new PhoneArea();
            area.setCode(86);
            area.setEn("China");
            area.setZh("中国");
            area.setLocale("CN");
        }
        mArea = area;
//        phoneEditText.setLeftActionButtonText("+" + mArea.getCode());
//        setSubmitButtonEnable();
    }


    @OnClick({R.id.register_button, R.id.submit_button, R.id.tv_forget_pwd})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.register_button) {//注册
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.submit_button) {//登录
            submitLogin();
        } else if (view.getId() == R.id.tv_forget_pwd) {//忘记密码
            Intent intent = new Intent(LoginActivity.this, ResetLoginPwdActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onLeftActionClick(CustomEditText customEditText, TextView leftActionButton) {
        openActivity(LoginActivity.this, PhoneAreaActivity.class, 1, null);
    }

    private void submitLogin() {
        String phone = phoneEditText.getInputText();
        String password = passwordEditText.getInputText();
        if (mLoginPresenter == null) {
            mLoginPresenter = new LoginPresenter(this);
        }
        mLoginPresenter.submitLogin("", phone, password);
    }

    @Override
    public void onLoginSuccess(LoginEntity entity) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterSuccess(RegisterEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mLoginPresenter != null) {
            mLoginPresenter.dropView();
        }
        if (mPhoneAreaPresenter != null) {
            mPhoneAreaPresenter.dropView();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}
