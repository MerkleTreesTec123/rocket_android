package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.WalletEntity;

public interface WalletContract {
    interface View extends BaseView {
        void bindWallet(WalletEntity walletEntity);
    }

    interface Presenter extends RxPresenter {
        void fetchWallet();
    }
}
