package com.muye.rocket.mvp.exc_wallet.contract;

import android.support.annotation.StringDef;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface ExcSendSMS2BindPhoneContract {
    String SMS_TYPE_ADD_ADDRESS = "104";
    String SMS_TYPE_ADD_WITHDRAW = "104";

    interface View extends BaseView {
        void onSendSMSSuccess();

        void onSendSMSFail();
    }

    interface Presenter extends RxPresenter {
        void sendSMS(@SMSType String type);
    }

    @StringDef({SMS_TYPE_ADD_ADDRESS, SMS_TYPE_ADD_WITHDRAW})
    @Retention(SOURCE)
    @interface SMSType {

    }
}
