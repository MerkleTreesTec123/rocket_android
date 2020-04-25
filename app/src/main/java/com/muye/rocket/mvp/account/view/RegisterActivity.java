package com.muye.rocket.mvp.account.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ifenduo.lib_base.widget.CustomEditText;
import com.muye.rocket.R;
import com.muye.rocket.api.RURLConfig;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.PhoneArea;
import com.muye.rocket.mvp.WebBrowser;
import com.muye.rocket.mvp.account.contract.PhoneAreaContract;
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
 * 注册
 * */
public class RegisterActivity extends BaseActivity implements SendSMSContract.View, PhoneAreaContract.View, CustomEditText.OnLeftActionClickListener, CountTimerCallBack {
    @BindView(R.id.phone_edit_text)
    CustomEditText phoneEditText;
    @BindView(R.id.email_edit_text)
    CustomEditText emailEditText;
    @BindView(R.id.phoneView)
    View phoneView;
    @BindView(R.id.emailView)
    View emailView;
    @BindView(R.id.emailRegist)
    TextView emailRegist;
    @BindView(R.id.phoneRegist)
    TextView phoneRegist;
    @BindView(R.id.agreement_text_view)
    TextView agreementTextView;
    @BindView(R.id.login_hint)
    TextView loginHint;
    PhoneArea mArea;
    SendSMSContract.Presenter mSendSMSPresenter;
    PhoneAreaContract.Presenter mPhoneAreaPresenter;
    private int registType;

    @BindView(R.id.cet_pwd)
    CustomEditText cetPwd;//密码
    @BindView(R.id.cet_sure_pwd)
    CustomEditText cetSurePwd;//确认密码
    @BindView(R.id.cet_code)
    CustomEditText cetCode;//验证码
    @BindView(R.id.cet_invite_code)
    CustomEditText cetInviteCode;//邀请码(非必填)
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;//发送验证码
    private CountTimer countTimer;
    private String mPhoneArea;
    private String mPhone;
    private String mCode;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);

        String agreementText = "<font color=\"#8284A8\">" + getString(R.string.the_registrar_agrees_to_the)
                + "</font>" + " " + "<u><font color=\"#01AA78\" >" + getString(R.string.platform_user_agreement) + "</font></u>";
        agreementTextView.setText(Html.fromHtml(agreementText));

        /*
         * 地区
         * */
        phoneEditText.setOnLeftActionClickListener(this);

        countTimer = new CountTimer(60000, 1000, this);

        initView();
        fetchPhoneArea();
    }

    private void initView() {
        cetPwd.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String password = customEditText.getInputText();
                    if (TextUtils.isEmpty(password)) {
                        customEditText.showErrorText(getString(R.string.input_login_password_hint));
                    } else {
                        if (!Validator.isPassword(customEditText.getInputText())) {
                            customEditText.showErrorText(getString(R.string.login_password_format_error));
                        } else {
                            customEditText.hideErrorText();
                        }
                    }
                }
                return false;
            }
        });
        cetSurePwd.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String password_ = customEditText.getInputText();
                    if (TextUtils.isEmpty(password_)) {
                        customEditText.showErrorText(getString(R.string.confirm_login_password));
                    } else {
                        String password = cetPwd.getInputText();
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
        cetCode.setOnFocusChangeListener(new CustomEditText.OnFocusChangeListener() {
            @Override
            public boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus) {
                if (!hasFocus) {
                    String code = customEditText.getInputText();
                    if (TextUtils.isEmpty(code)) {
                        customEditText.showErrorText(getString(R.string.sms_code_error_hint));
                    } else {
                        if (code.length() != cetCode.getMaxCount()) {
                            customEditText.showErrorText(getString(R.string.sms_code_error_hint));
                        } else {
                            customEditText.hideErrorText();
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
        emailEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validator.isEmail(emailEditText.getInputText())) {
                    emailEditText.hideErrorText();
                }
                emailEditText.setCleanButtonEnable(!TextUtils.isEmpty(emailEditText.getInputText()));
            }
        });
        cetPwd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = cetPwd.getInputText();
                String password_ = cetSurePwd.getInputText();
                cetPwd.setLevel(Validator.getPasswordLevel(password));
                if (Validator.isPassword(password)) {
                    cetPwd.hideErrorText();
                }
                if (!TextUtils.isEmpty(password_) && password_.equals(password)) {
                    cetSurePwd.hideErrorText();
                }

                cetPwd.setCleanButtonEnable(!TextUtils.isEmpty(password));
            }
        });
        cetSurePwd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = cetPwd.getInputText();
                String password_ = cetSurePwd.getInputText();

                if (!TextUtils.isEmpty(password) && password.equals(password_)) {
                    cetSurePwd.hideErrorText();
                }

                cetSurePwd.setCleanButtonEnable(!TextUtils.isEmpty(password_));
            }
        });
        cetCode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = cetCode.getInputText();
                if (code.length() == cetCode.getMaxCount()) {
                    cetCode.hideErrorText();
                }
            }
        });
    }

    /*
     * 获取手机号码地区
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
        String phone = phoneEditText.getInputText().trim();
        String email = emailEditText.getInputText().trim();
        if (registType == 0) {
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
        } else if (registType == 1) {
            if (TextUtils.isEmpty(email)) {
                emailEditText.showErrorText(getString(R.string.input_email_hint));
                return;
            }
            if (!Validator.isEmail(email)) {
                emailEditText.showErrorText(getString(R.string.enter_ok_email_address));
                return;
            }
        }

        tvSendCode.setEnabled(false);
        tvSendCode.setBackgroundResource(R.drawable.shape_e5e5e5_radius_4);
        tvSendCode.setTextColor(getResources().getColor(R.color.black));
        countTimer.start();

        if (mSendSMSPresenter == null) {
            mSendSMSPresenter = new SendSMSPresenter(this);
        }
        // 手机号
        if (registType == 0)
            mSendSMSPresenter.sendSMS(String.valueOf(mArea.getCode()), phone, SendSMSContract.SMS_CODE_TYPE_REGISTER);
        //邮箱
        if (registType == 1)
            mSendSMSPresenter.sendEmail(email, "1");
    }

    /*
     * 设置地区
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
     * 校验验证码
     * */
    private void checkCode() {
        if (TextUtils.isEmpty(mPhone)) {//
            showToast(getString(R.string.get_code_first));
            return;
        }

        String code = cetCode.getInputText();
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
        if (!TextUtils.isEmpty(mPhoneArea)) {
            mSendSMSPresenter.checkCode(mPhone, code, SendSMSContract.SMS_CODE_TYPE_CHECK_REGISTER);
        } else {
            mSendSMSPresenter.checkEmailCode(mPhone, code, "1");
        }
    }

    /*
     * 获取地区成功
     * */
    @Override
    public void bindPhoneArea(List<PhoneArea> areaList) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        PhoneArea phoneArea = null;

        if (!TextUtils.isEmpty(locale.getCountry()) && areaList != null && areaList.size() > 0) {
            for (PhoneArea area : areaList) {
                if (area != null && locale.getCountry().equals(area.getLocale())) {
                    phoneArea = area;
                    break;
                }
            }
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
     * 短信验证码发送成功
     * */
    @Override
    public void onSendSMSSuccess(String area, String phone) {
        mPhoneArea = area;
        mPhone = phone;
    }

    /*
     * 短信验证码发送失败
     * */
    @Override
    public void onSendSMSFail() {
        countTimer.onFinish();
    }

    /**
     * 邮箱验证码接收成功
     *
     * @param email
     */
    @Override
    public void onSendEmailSuccess(String email) {
        mPhoneArea = "";
        mPhone = email;
    }

    @Override
    public void onSendEmailFail() {
        countTimer.onFinish();
    }

    /*
     * 验证码校验
     * */
    @Override
    public void onCheckCodeSuccess(String code) {
        mCode = code;
        setPayPassword();
    }

    @Override
    public void onCheckCodeFail() {

    }

    /*
     * 下一步（去设置支付密码）
     * */
    private void setPayPassword() {
        String password = cetPwd.getInputText();
        String password_ = cetSurePwd.getInputText();

        if (registType == 0) {
            if (TextUtils.isEmpty(phoneEditText.getInputText())) {
                phoneEditText.showErrorText(getString(R.string.please_enter_the_correct_phone_number));
                return;
            } else {
                phoneEditText.hideErrorText();
            }
        } else {
            if (TextUtils.isEmpty(emailEditText.getInputText())) {
                emailEditText.showErrorText(getString(R.string.enter_ok_email_address));
                return;
            } else {
                emailEditText.hideErrorText();
            }
        }

        if (TextUtils.isEmpty(password)) {
            cetPwd.showErrorText(getString(R.string.input_login_password_hint));
            return;
        } else {
            if (!Validator.isPassword(password)) {
                cetPwd.showErrorText(getString(R.string.login_password_format_error));
                return;
            } else {
                cetPwd.hideErrorText();
            }
        }

        if (TextUtils.isEmpty(password_)) {
            cetSurePwd.showErrorText(getString(R.string.confirm_login_password));
            return;
        } else {
            if (!password_.equals(password)) {
                cetSurePwd.showErrorText(getString(R.string.inconsistent_password_entered_twice));
                return;
            } else {
                cetSurePwd.hideErrorText();
            }
        }

        if (TextUtils.isEmpty(cetCode.getInputText())) {
            cetCode.showErrorText(getString(R.string.sms_code_error_hint));
            return;
        } else {
            cetCode.hideErrorText();
        }

        PayPasswordActivity.openPayPasswordActivity(RegisterActivity.this, mPhoneArea, mPhone, mCode, password, cetInviteCode.getInputText());
    }

    /*
     * 验证码
     * */
    @Override
    public void timerDone() {
        tvSendCode.setBackgroundResource(R.drawable.shape_01aa78_radius_2);
        tvSendCode.setEnabled(true);
        tvSendCode.setText("发送验证码");
        tvSendCode.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void timerDoing(long millisUntilFinished) {
        tvSendCode.setText(millisUntilFinished / 1000 + "秒");
    }

    @OnClick({R.id.agreement_text_view, R.id.tv_send_code, R.id.phoneTab, R.id.emailTab, R.id.tv_next_step})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.agreement_text_view) {//用户协议
            WebBrowser.openWebBrowser(this, RURLConfig.URL_PAGE_SERVICE_AGREEMENT, getString(R.string.platform_user_agreement));
        } else if (view.getId() == R.id.phoneTab) {//手机注册
            registType = 0;
            phoneView.setVisibility(View.VISIBLE);
            emailView.setVisibility(View.INVISIBLE);
            phoneRegist.setTextColor(getResources().getColor(R.color.color01AA78));
            phoneEditText.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.GONE);
            emailRegist.setTextColor(getResources().getColor(R.color.text_color_gray));
            loginHint.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.emailTab) {//邮件注册
            registType = 1;
            phoneView.setVisibility(View.INVISIBLE);
            emailView.setVisibility(View.VISIBLE);
            phoneRegist.setTextColor(getResources().getColor(R.color.text_color_gray));
            emailRegist.setTextColor(getResources().getColor(R.color.color01AA78));
            emailEditText.setVisibility(View.VISIBLE);
            phoneEditText.setVisibility(View.GONE);
            loginHint.setVisibility(View.GONE);
        } else if (view.getId() == R.id.tv_send_code) {//发送验证码
            sendSMS();
        } else if (view.getId() == R.id.tv_next_step) {//下一步（去设置支付密码）
            checkCode();//先校验验证码
        }
    }

    /*
     * 选择地区
     * */
    @Override
    public void onLeftActionClick(CustomEditText customEditText, TextView leftActionButton) {
        openActivity(RegisterActivity.this, PhoneAreaActivity.class, 1, null);
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
    protected void onDestroy() {
        if (mPhoneAreaPresenter != null) {
            mPhoneAreaPresenter.dropView();
        }
        if (mSendSMSPresenter != null) {
            mSendSMSPresenter.dropView();
        }
        if (countTimer != null) {
            countTimer.onFinish();
        }
        super.onDestroy();
    }

}
