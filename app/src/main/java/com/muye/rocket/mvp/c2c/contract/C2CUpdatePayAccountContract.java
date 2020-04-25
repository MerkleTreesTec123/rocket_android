package com.muye.rocket.mvp.c2c.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface C2CUpdatePayAccountContract {
    interface View extends BaseView {
        void onSubmitC2CPayAccountSuccess();
    }

    interface Presenter extends RxPresenter {
        void submitC2CPayAccount(String bankInfo, String alipayInfo, String wechatInfo, String password);
    }
}
