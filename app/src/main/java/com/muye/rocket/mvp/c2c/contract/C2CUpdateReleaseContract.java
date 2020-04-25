package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface C2CUpdateReleaseContract {

    interface View extends BaseView {
        void onUpdateReleaseStatusSuccess(String status);
    }

    interface Presenter extends RxPresenter {
        void updateReleaseStatus(String id, String status);
    }
}
