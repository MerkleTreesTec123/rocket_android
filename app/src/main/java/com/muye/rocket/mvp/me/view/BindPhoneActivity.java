package com.muye.rocket.mvp.me.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.widget.CustomEditText;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.PhoneArea;
import com.muye.rocket.mvp.account.contract.CountDownTimeContract;
import com.muye.rocket.mvp.account.contract.PhoneAreaContract;
import com.muye.rocket.mvp.account.presenter.CountDownTimePresenter;
import com.muye.rocket.mvp.account.presenter.PhoneAreaPresenter;
import com.muye.rocket.mvp.account.view.PhoneAreaActivity;
import com.muye.rocket.mvp.me.contract.SendSMSBindContract;
import com.muye.rocket.mvp.me.presenter.SendSMSBindPresenter;
import com.muye.rocket.tools.Validator;
import com.muye.rocket.widget.PasswordEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

// 绑定手机号
public class BindPhoneActivity extends BaseActivity implements CustomEditText.OnLeftActionClickListener, PhoneAreaContract.View, SendSMSBindContract.View, CountDownTimeContract.View {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.email_bind_text)
    CustomEditText emailEditText;

    @BindView(R.id.phone_bind_text)
    CustomEditText phoneEditText;
    @BindView(R.id.phone_code_text)
    PasswordEditText passwordEditText;
    @BindView(R.id.send_button)
    TextView sendButton;
    @BindView(R.id.error_text_view)
    TextView errorTextView;
    @BindView(R.id.bind_phone)
    Button bindPhone;

    PhoneArea mArea;
    SendSMSBindContract.Presenter mSendSMSPresenter;
    PhoneAreaContract.Presenter mPhoneAreaPresenter;
    CountDownTimePresenter mCountDownTimePresenter;
    private String phone;
    private String email;
    private int mType;


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.bind_phone;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        phoneEditText.setOnLeftActionClickListener(this);
        setupInputListener();
        fetchPhoneArea();
        setupVerCodeInputListener();
    }

    public static void openBindPEActivity(Context context, int type) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mType = bundle.getInt("type", 0);
        }

        if (mType == 0){
            tvTitle.setText(getString(R.string.bind_phone_num));
            phoneEditText.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.GONE);
        }else {
            tvTitle.setText(getString(R.string.bind_email_num));
            phoneEditText.setVisibility(View.GONE);
            emailEditText.setVisibility(View.VISIBLE);
        }
    }

    private void setupInputListener() {
        phoneEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validator.isMobile(phoneEditText.getInputText())) {
                    phoneEditText.hideErrorText();
                }
                setSendSMSButtonEnable();
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
                if (Validator.isEmail(emailEditText.getInputText())){
                    emailEditText.hideErrorText();
                }
                setSendSMSButtonEnable();
                emailEditText.setCleanButtonEnable(!TextUtils.isEmpty(emailEditText.getInputText()));
            }
        });
    }


    private void setupVerCodeInputListener() {
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = passwordEditText.getPasswordString();
                if (code.length() == passwordEditText.getMaxCount()) {
                    errorTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setSendSMSButtonEnable() {
        String phone = phoneEditText.getInputText();
        String email = emailEditText.getInputText();
        if (mType == 0){
            sendButton.setEnabled((Validator.isMobile(phone)));
        }else {
            sendButton.setEnabled((Validator.isEmail(email)));
        }
    }

    private void fetchPhoneArea() {
        if (mPhoneAreaPresenter == null) {
            mPhoneAreaPresenter = new PhoneAreaPresenter(this);
        }
        mPhoneAreaPresenter.fetchPhoneArea();
    }

    @Override
    public void onLeftActionClick(CustomEditText customEditText, TextView leftActionButton) {
        openActivity(BindPhoneActivity.this, PhoneAreaActivity.class, 1, null);
    }

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

    @OnClick({R.id.send_button,R.id.back_button,R.id.bind_phone})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.send_button:
                sendSMS();
                break;
            case R.id.back_button:
                onClickBack();
                break;
            case R.id.bind_phone:
                // 校验验证码并绑定
                checkCode();
                break;
        }
    }

    private void sendSMS() {
        phone = phoneEditText.getInputText().trim();
        email = emailEditText.getInputText().trim();
        if (mType == 0){
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
        }else {
            if (TextUtils.isEmpty(email)) {
                emailEditText.showErrorText(getString(R.string.input_email_hint));
                return;
            }

            if (!Validator.isEmail(email)) {
                emailEditText.showErrorText(getString(R.string.enter_ok_email_address));
                return;
            }
        }
        if (mSendSMSPresenter == null) {
            mSendSMSPresenter = new SendSMSBindPresenter(this);
        }
        if (mType == 0){
            mSendSMSPresenter.sendSMS(String.valueOf(mArea.getCode()), phone, SendSMSBindContract.SMS_CODE_TYPE_BIND_PHONE);
        }else {
//            mSendSMSPresenter.sendEmail(email,"1");
            mSendSMSPresenter.sendEmail(MMKVTools.getToken(),email);
        }
    }

    private void checkCode() {
        String mPhone = phoneEditText.getInputText().trim();
        String mEmail = emailEditText.getInputText().trim();
        String code = passwordEditText.getPasswordString();

        if (mType == 0){
            if (TextUtils.isEmpty(mPhone)) {
                phoneEditText.showErrorText(getString(R.string.input_phone_hint));
                return;
            }
            if (TextUtils.isEmpty(phone)){
                showToast(getString(R.string.first_get_phone_vertcode));
                return;
            }
            if (!phone.equals(mPhone)){
                showToast(getString(R.string.error_code_phone));
                return;
            }
        }else {
            if (TextUtils.isEmpty(mEmail)) {
                emailEditText.showErrorText(getString(R.string.input_email_hint));
                return;
            }
            if (TextUtils.isEmpty(email)){
                showToast(getString(R.string.first_get_phone_vertcode));
                return;
            }
            if (!email.equals(mEmail)){
                showToast(getString(R.string.error_code_email));
                return;
            }
        }
        if (code.length() < passwordEditText.getMaxCount()) {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(R.string.sms_code_error_hint);
            return;
        }
        if (mSendSMSPresenter == null) {
            mSendSMSPresenter = new SendSMSBindPresenter(this);
        }
        if (mType == 0){
            mSendSMSPresenter.bindPhone(MMKVTools.getToken(), String.valueOf(mArea.getCode()), code, mPhone);
        }else {
            mSendSMSPresenter.bindEmail(MMKVTools.getToken(), code, mEmail);
        }

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

    private void startCountDownTime() {
        if (mCountDownTimePresenter == null) {
            mCountDownTimePresenter = new CountDownTimePresenter(this);
        }
        mCountDownTimePresenter.startCountDownTime(60);
    }

    /*
    * 发送短信验证码成功
    * */
    @Override
    public void onSendSMSSuccess(String area, String phone) {
        startCountDownTime();
        showToast(getString(R.string.send_code_ok));
    }

    @Override
    public void onSendSMSFail() {

    }

    /*
    * 发送邮箱验证码成功
    * */
    @Override
    public void onSendEmailSuccess(String email) {
        startCountDownTime();
        showToast(getString(R.string.send_code_ok));
    }

    @Override
    public void onSendEmailFail() {

    }

    @Override
    public void onBindPhone(String status,String phone) {
        showToast(getString(R.string.bind_ok));

        //空的说明是邮箱
        if (TextUtils.isEmpty(status)){
            MMKVTools.saveEmail(phone);
        }else {
            MMKVTools.saveArea(status);
            MMKVTools.savePhone(phone);
        }
        finish();
    }

    @Override
    public void onBindPhoneFail() {

    }


    @Override
    protected void onDestroy() {
        if (mPhoneAreaPresenter != null) {
            mPhoneAreaPresenter.dropView();
        }
        if (mSendSMSPresenter != null) {
            mSendSMSPresenter.dropView();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCountDownTimeStart() {
        sendButton.setEnabled(false);
    }

    @Override
    public void onCountDownTimeEnd() {
        sendButton.setEnabled(true);
        sendButton.setText(getString(R.string.send_code));
    }

    @Override
    public void showCountDownTime(int time) {
//        send_code
        sendButton.setText(time+"s 后重新发送");
    }
}
