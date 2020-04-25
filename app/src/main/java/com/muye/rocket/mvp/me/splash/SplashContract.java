package com.muye.rocket.mvp.me.splash;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface SplashContract {

    interface View extends BaseView {
        void saveCode(int code);
    }

    interface Presenter extends RxPresenter {
        void getOpenWs();
    }

}
