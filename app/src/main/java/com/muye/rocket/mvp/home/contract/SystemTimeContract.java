package com.muye.rocket.mvp.home.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface SystemTimeContract {

    interface View extends BaseView {
        void bindSystemTime(long time);
        void bindTimeTask(long time);
    }

    interface Presenter extends RxPresenter {
        void fetchSystemTime();
        void fetchSystemApi();
    }
}
