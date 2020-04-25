package com.muye.rocket.mvp.account.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface CountDownTimeContract {
    interface Presenter extends RxPresenter {
        void startCountDownTime(int totalSeconds);
    }

    interface View extends BaseView {
        void onCountDownTimeStart();

        void onCountDownTimeEnd();

        void showCountDownTime(int time);
    }
}
