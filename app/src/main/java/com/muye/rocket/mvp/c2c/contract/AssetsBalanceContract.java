package com.muye.rocket.mvp.c2c.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface AssetsBalanceContract {
    interface View extends BaseView{
        void bindAssetsBalance(String coinID, String coinName, double balance, double freeze);
    }

    interface Presenter extends RxPresenter{
        void fetchAssetsBalance(String coinID);
    }
}
