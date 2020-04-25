package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface ForgetSafePasswordContract {
    interface View extends BaseView {
        void onSubmitPayPasswordSuccess();
    }

    interface Presenter extends RxPresenter {
        void submitPayPassword(String password, String smsCode, String googleCode);
    }
}
