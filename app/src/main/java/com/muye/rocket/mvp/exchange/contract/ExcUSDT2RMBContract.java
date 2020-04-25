package com.muye.rocket.mvp.exchange.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface ExcUSDT2RMBContract {
    interface View extends BaseView {
        void bindUSDT2RMBRate(double rate);
    }

    interface Presenter extends RxPresenter {
        void fetchUSDT2RMBRate();
    }
}
