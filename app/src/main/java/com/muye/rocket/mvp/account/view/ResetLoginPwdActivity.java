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
import com.muye.rocket.entity.PhoneArea;
import com.muye.rocket.mvp.account.contract.ForgetPasswordContract;
import com.muye.rocket.mvp.account.contract.PhoneAreaContract;
import com.muye.rocket.mvp.account.presenter.ForgetPasswordPresenter;
import com.muye.rocket.mvp.account.presenter.PhoneAreaPresenter;
import com.muye.rocket.mvp.me.contract.SendSMSContract;
import com.muye.rocket.mvp.me.presenter.SendSMSPresenter;
import com.muye.rocket.tools.CountTimer;
import com.muye.rocket.tools.CountTimerCallBack;
import com.muye.rocket.tools.Validator;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 重置登录密码
 * */
public class ResetLoginPwdActivity extends BaseActivity implements CustomEditText.OnLeftActionClickListener, PhoneAreaContract.View, SendSMSContract.View, CountTimerCallBack, ForgetPasswordContract.View {
    @BindView(R.id.phone_edit_text)
    CustomEditText phoneEditText;//手机号
    @BindView(R.id.new_edit_text)
    CustomEditText newEditText;//新密码
    @BindView(R.id.new_edit_text_)
    CustomEditText newEditText_;//确认密码
    @BindView(R.id.google_edit_text)
    CustomEditText googleEditText;//谷歌验证码
    @BindView(R.id.phone_vercode_text)
    CustomEditText phoneVercodeText;//验证码
    @BindView(R.id.tvPhoneCode)
    TextView tvPhoneCode;//获取验证码
    @BindView(R.id.btn_reset)
    Button btnReset;//重置

    PhoneArea mArea;
    PhoneAreaContract.Presenter mPhoneAreaPresenter;
    SendSMSContract.Presenter mSendSMSPresenter;
    ForgetPasswordContract.Presenter mPresenter;
    private CountTimer countTimer;
    private String mPhoneArea;
    private String mPhone;
    private String mCode;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_reset_login_pwd;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        phoneEditText.setOnLeftActionClickListener(this);

        initView();
        fetchPhoneArea();//获取地区

        countTimer = new CountTimer(60000, 1000, this);
    }

    private void initView() {
        phoneEditText.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String phone = customEditText.getInputText();
                    if (TextUtils.isEmpty(phone)) {
                        customEditText.showErrorText(getString(R.string.input_phone_hint));
                    } else {
                        if (!Validator.isMobile(phone)) {
                            customEditText.showErrorText(getString(R.string.input_correct_phone));
                        } else {
                            customEditText.hideErrorText();
                        }
                    }
                }
                return false;
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

        phoneEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validator.isMobile(phoneEditText.getInputText())) {
                    phoneEditText.hideErrorText();
                }

                phoneEditText.setCleanButtonEnable(!TextUtils.isEmpty(phoneEditText.getInputText()));
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
    }

    /*
    * 获取地区
    * */
    private void fetchPhoneArea() {
        if (mPhoneAreaPresenter == null) {
            mPhoneAreaPresenter = new PhoneAreaPresenter(this);
        }
        mPhoneAreaPresenter.fetchPhoneArea();
    }

    /*
     * 发送验证码
     * */
    private void sendSMS() {
        String phone = phoneEditText.getInputText();
        if (mArea == null) {
            phoneEditText.showErrorText(getString(R.string.please_select_country_region));
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.showErrorText(getString(R.string.input_phone_hint));
            return;
        }

        if (!Validator.isMobile(phone)) {
            phoneEditText.showErrorText(getString(R.string.please_enter_the_correct_phone_number));
            return;
        }

        tvPhoneCode.setEnabled(false);
        countTimer.start();

        if (mSendSMSPresenter == null) {
            mSendSMSPresenter = new SendSMSPresenter(this);
        }
        mSendSMSPresenter.sendSMS(String.valueOf(mArea.getCode()), phone, SendSMSContract.SMS_CODE_TYPE_FORGET_LOGIN_PWD);
    }

    /*
    * 显示区域
    * */
    private void bindArea(PhoneArea area) {
        if (area == null) {
            area = new PhoneArea();
            area.setCode(86);
            area.setEn("China");
            area.setZh("中国");
            area.setLocale("CN");
        }
        mArea = area;
        phoneEditText.setLeftActionButtonText("+" + mArea.getCode());
    }

    /*
    * 地区获取成功
    * */
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

    /*
    * 获取区域缓存
    * */
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

    /*
    * 获取本地区域
    * */
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

    /*
    * 校验code
    * */
    private void checkCode() {
        if (TextUtils.isEmpty(mPhone)){
            showToast(getString(R.string.get_code_first));
            return;
        }
        String code = phoneVercodeText.getInputText();
        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.exc_wal_input_sms_code_hint));
            return;
        } else if (code.length() != 6) {
            showToast(getString(R.string.sms_code_error_hint));
            return;
        }
        if (mSendSMSPresenter == null) {
            mSendSMSPresenter = new SendSMSPresenter(this);
        }
        mSendSMSPresenter.checkCode(mPhone, code, SendSMSContract.SMS_CODE_TYPE_FORGET_LOGIN_PWD);
    }

    /*
    * 提交密码
    * */
    private void submitPassword() {
        String password = newEditText.getInputText();
        String password_ = newEditText_.getInputText();
        String googleCode = googleEditText.getInputText();

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
            mPresenter = new ForgetPasswordPresenter(this);
        }
        mPresenter.submitPassword(mPhoneArea, mPhone, password, mCode, googleCode);
    }

    @Override
    public void onSendSMSSuccess(String area, String phone) {
        mPhoneArea = area;
        mPhone = phone;
    }

    @Override
    public void onSendSMSFail() {
        countTimer.onFinish();
    }

    /*
    * 验证码校验成功
    * */
    @Override
    public void onCheckCodeSuccess(String code) {
        mCode = code;
        submitPassword();//提交密码
    }

    /*
     * 验证码校验失败
     * */
    @Override
    public void onCheckCodeFail() {
        showToast(getString(R.string.sms_code_error_hint));
    }

    @Override
    public void onSendEmailSuccess(String email) {

    }

    @Override
    public void onSendEmailFail() {

    }

    /*
    * 密码提交成功
    * */
    @Override
    public void onSubmitPasswordSuccess() {
        showToast(getString(R.string.update_password_success));
        finish();
    }

    @OnClick({R.id.tvPhoneCode, R.id.btn_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvPhoneCode://发送验证码
                sendSMS();
                break;
            case R.id.btn_reset:
                checkCode();//先校验code
                break;
        }
    }

    /*
    * 区域点击
    * */
    @Override
    public void onLeftActionClick(CustomEditText customEditText, TextView leftActionButton) {
        openActivity(this, PhoneAreaActivity.class, 1, null);
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

    @Override
    public void timerDone() {
        tvPhoneCode.setEnabled(true);
        tvPhoneCode.setText(getString(R.string.get_phone_ver_code));
    }

    @Override
    public void timerDoing(long millisUntilFinished) {
        tvPhoneCode.setText(getString(R.string.resend) + millisUntilFinished / 1000 + "s");
    }

    @Override
    protected void onDestroy() {
        if (mPhoneAreaPresenter != null){
            mPhoneAreaPresenter.dropView();
        }
        if (mSendSMSPresenter != null) {
            mSendSMSPresenter.dropView();
        }
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        if (countTimer != null){
            countTimer.onFinish();
        }
        super.onDestroy();
    }


}
