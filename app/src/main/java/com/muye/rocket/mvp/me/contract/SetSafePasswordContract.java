package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface SetSafePasswordContract {
    interface View extends BaseView {
        void onSubmitPayPasswordSuccess();
        void onVerifyPhoenCode();
    }

    interface Presenter extends RxPresenter {
        void submitPayPassword(String oldPassword, String password, String smsCode, String googleCode, String idCard);
        void fetchPhoneCode(String code,String phone);
    }
}
