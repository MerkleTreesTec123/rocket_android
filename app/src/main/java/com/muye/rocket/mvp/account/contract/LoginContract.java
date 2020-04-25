package com.muye.rocket.mvp.account.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.LoginEntity;

public interface LoginContract {

    interface View extends BaseView {
        void onLoginSuccess(LoginEntity loginEntity);
    }

    interface Presenter extends RxPresenter {
        void submitLogin(String area, String phone, String password);
    }
}
