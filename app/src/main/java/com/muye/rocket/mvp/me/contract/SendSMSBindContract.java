package com.muye.rocket.mvp.me.contract;

import android.support.annotation.StringDef;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface SendSMSBindContract {
    String SMS_CODE_TYPE_BIND_PHONE = "1";
    String SMS_CODE_TYPE_UPDATEPAY = "2";

    interface View extends BaseView {
        void onSendSMSSuccess(String area, String phone);
        void onSendSMSFail();
        void onBindPhone(String status,String phone);
        void onBindPhoneFail();

        void onSendEmailSuccess(String email);
        void onSendEmailFail();
    }

    interface Presenter extends RxPresenter {
        void sendSMS(String area, String phone, @SendSMSBindContract.SMSCodeTypes String type);

//        void sendEmail(String email,String type);
        void sendEmail(String token,String email);

        void bindPhone(String token,String area,String code,String phone);

        void bindEmail(String token,String code,String email);
    }

    @StringDef({SMS_CODE_TYPE_BIND_PHONE})
    @Retention(SOURCE)
    @interface SMSCodeTypes {
    }
}
