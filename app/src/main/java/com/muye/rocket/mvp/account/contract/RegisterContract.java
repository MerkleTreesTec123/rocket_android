package com.muye.rocket.mvp.account.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface RegisterContract {

    interface View extends BaseView {
        void onRegisterSuccess();
    }

    interface Presenter extends RxPresenter {
        void submitRegister(String areaCode, String phone, String smsCode, String password, String payPassword, String inviteCode);
        void submitEmailRegister(String phone, String smsCode, String password, String payPassword, String inviteCode);
    }
}
