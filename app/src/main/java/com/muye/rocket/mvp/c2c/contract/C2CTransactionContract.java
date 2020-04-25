package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface C2CTransactionContract {
    interface View extends BaseView {
        void onSubmitTransactionSuccess(String orderID);
    }

    interface Presenter extends RxPresenter {
        void submitTransaction(String releaseID, String num, String type, String payType, String password);
    }
}
