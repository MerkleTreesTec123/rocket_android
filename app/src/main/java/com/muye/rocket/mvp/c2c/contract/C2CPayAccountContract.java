package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.C2CPayAccountInfo;

public interface C2CPayAccountContract {
    interface View extends BaseView {
        void bindC2CPayAccountInfo(C2CPayAccountInfo accountInfo);
    }

    interface Presenter extends RxPresenter {
        void fetchC2CPayAccountInfo();
    }
}
