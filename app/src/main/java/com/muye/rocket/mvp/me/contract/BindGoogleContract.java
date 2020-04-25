package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.GoogleDevice;

public interface BindGoogleContract {
    interface View extends BaseView {
        void bindGoogleDevice(GoogleDevice device);

        void onBindGoogleSuccess();
    }

    interface Presenter extends RxPresenter {
        void fetchGoogleDevice();

        void bindGoogle(String code, String key);
    }

}
