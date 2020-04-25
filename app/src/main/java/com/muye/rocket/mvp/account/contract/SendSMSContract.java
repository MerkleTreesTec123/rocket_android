package com.muye.rocket.mvp.account.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface SendSMSContract {
    interface View extends BaseView {
        void onSendSMSSuccess(String area, String phone);

        void onSendSMSFail(int code, String message);
    }

    interface Presenter extends RxPresenter {
        void sendNotSignatureSMS(String areaCode, String phone, String type);
    }
}
