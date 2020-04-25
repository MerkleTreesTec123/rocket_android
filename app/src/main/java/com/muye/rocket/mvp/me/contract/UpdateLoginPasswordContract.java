package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface UpdateLoginPasswordContract {
    interface View extends BaseView {
        void onSubmitLoginPasswordSuccess();
    }

    interface Presenter extends RxPresenter {
        void submitLoginPassword(String oldPassword, String password,  String googleCode);
    }
}
