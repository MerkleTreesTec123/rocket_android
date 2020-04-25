package com.muye.rocket.mvp.c2c.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.C2CCoinBalance;

public interface C2CBalanceByAssetsContract {
    interface View extends BaseView {
        void bindCoinBalance(C2CCoinBalance balance);
    }

    interface Presenter extends RxPresenter {
        void fetchCoinBalance(String assetsCoinID);
    }
}
