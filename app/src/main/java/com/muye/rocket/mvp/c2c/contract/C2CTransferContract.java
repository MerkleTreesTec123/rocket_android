package com.muye.rocket.mvp.c2c.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface C2CTransferContract {
    interface View extends BaseView {
        void onTransferSuccess();
    }

    interface Presenter extends RxPresenter {
        void submitTransferSuccess(String coinID, String num, String type, String password);
    }
}
