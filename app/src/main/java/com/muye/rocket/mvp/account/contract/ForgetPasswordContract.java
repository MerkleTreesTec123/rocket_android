package com.muye.rocket.mvp.account.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface ForgetPasswordContract {
    interface View extends BaseView {
        void onSubmitPasswordSuccess();
    }

    interface Presenter extends RxPresenter {
        void submitPassword(String area, String phone, String password, String smsCode, String googleCode);
    }
}
