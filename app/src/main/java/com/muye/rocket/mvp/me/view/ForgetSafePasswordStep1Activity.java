package com.muye.rocket.mvp.me.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muye.rocket.base.BaseActivity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.event.ForgetSafePasswordEvent;
import com.muye.rocket.mvp.account.contract.CountDownTimeContract;
import com.muye.rocket.mvp.account.presenter.CountDownTimePresenter;
import com.muye.rocket.mvp.me.contract.SendSMSContract;
import com.muye.rocket.mvp.me.presenter.SendSMSPresenter;
import com.muye.rocket.widget.PasswordEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetSafePasswordStep1Activity extends BaseActivity implements SendSMSContract.View, CountDownTimeContract.View {
    @BindView(R.id.code_edit_text)
    PasswordEditText codeEditText;
    @BindView(R.id.title_hint_text_view)
    TextView titleHintTextView;
    @BindView(R.id.send_code)
    Button sendCode;
    SendSMSContract.Presenter mSendSMSPresenter;
    CountDownTimeContract.Presenter mCountDownPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_forget_safe_password_setp_1;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        EventBus.getDefault().register(this);
        setNavigationRight(getString(R.string.next_step), getResources().getColor(R.color.color292A49), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCode();
            }
        });
        sendCode.setEnabled(true);
    }

    @OnClick(R.id.send_code)
    public void onClick() {
        sendSMS();
    }

    private void sendSMS() {
        if (mSendSMSPresenter == null) {
            mSendSMSPresenter = new SendSMSPresenter(this);
        }
        mSendSMSPresenter.sendSMS(MMKVTools.getArea(), MMKVTools.getPhone(), SendSMSContract.SMS_CODE_TYPE_FORGET_PAY_PWD);
    }

    private void checkCode() {
        String code = codeEditText.getText().toString();
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
        mSendSMSPresenter.checkCode(MMKVTools.getPhone(), code, SendSMSContract.SMS_CODE_TYPE_FORGET_PAY_PWD);
    }


    @Override
    public void onSendSMSSuccess(String area, String phone) {
        titleHintTextView.setText(getString(R.string.sms_send_to_phone) + "    +" + MMKVTools.getArea() + " " + MMKVTools.getPhone());
        if (mCountDownPresenter == null) {
            mCountDownPresenter = new CountDownTimePresenter(this);
        }
        mCountDownPresenter.startCountDownTime(60);
    }

    @Override
    public void onSendSMSFail() {
        sendCode.setEnabled(true);
        sendCode.setText(R.string.resend);
    }

    @Override
    public void onCheckCodeSuccess(String code) {
        if (MMKVTools.hasBindPhone()) {
            ForgetSafePasswordStep2Activity.openForgetSafePasswordStep2Activity(ForgetSafePasswordStep1Activity.this, code);
        }else {
            showToast(getString(R.string.bind_phone_));
        }
    }

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

    @Override
    public void onCountDownTimeStart() {
        sendCode.setEnabled(false);
    }

    @Override
    public void onCountDownTimeEnd() {
        sendCode.setEnabled(true);
        sendCode.setText(R.string.resend);
    }

    @Override
    public void showCountDownTime(int time) {
        sendCode.setEnabled(false);
        sendCode.setText(getString(R.string.resend) + " " + time + "s");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onForgetSafePasswordSuccess(ForgetSafePasswordEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mSendSMSPresenter != null) {
            mSendSMSPresenter.dropView();
        }
        if (mCountDownPresenter != null) {
            mCountDownPresenter.dropView();
        }
        super.onDestroy();
    }
}
