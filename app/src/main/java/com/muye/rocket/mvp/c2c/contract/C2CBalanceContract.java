package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface C2CBalanceContract {

    interface View extends BaseView {
        void bindCoinBalance(String coinID, String coinName, double balance, double freeze);
    }

    interface Presenter extends RxPresenter {
        void fetchCoinBalance(String coinID);
    }
}
