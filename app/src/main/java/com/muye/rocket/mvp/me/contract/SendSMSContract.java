package com.muye.rocket.mvp.me.contract;

import android.support.annotation.StringDef;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface SendSMSContract {

    String SMS_CODE_TYPE_REGISTER = "112";
    String SMS_CODE_TYPE_CHECK_REGISTER = "2";
    String SMS_CODE_TYPE_FORGET_LOGIN_PWD = "109";
    String SMS_CODE_TYPE_FORGET_PAY_PWD = "107";
    String SMS_CODE_TYPE_BIND_PHONE = "1";


    interface View extends BaseView {
        void onSendSMSSuccess(String area, String phone);

        void onSendSMSFail();

        void onCheckCodeSuccess(String code);

        void onCheckCodeFail();

        void onSendEmailSuccess(String email);

        void onSendEmailFail();

    }

    interface Presenter extends RxPresenter {
        void sendSMS(String area, String phone, @SMSCodeType String type);

        void sendEmail(String email,String type);

        void checkCode(String phone, String code, @SMSCodeType String type);

        void checkEmailCode(String phone, String code,String type);
    }

    @StringDef({SMS_CODE_TYPE_REGISTER, SMS_CODE_TYPE_FORGET_LOGIN_PWD,
            SMS_CODE_TYPE_FORGET_PAY_PWD, SMS_CODE_TYPE_CHECK_REGISTER,
            SMS_CODE_TYPE_BIND_PHONE})
    @Retention(SOURCE)
    @interface SMSCodeType {
    }


}
