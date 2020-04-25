package com.muye.rocket.mvp.exc_wallet.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;

public interface ExcWalletContract {
    interface View extends BaseView {
        void bindAssets(ExcAssetsEntity assetsEntity);
    }

    interface Presenter extends RxPresenter {
        void fetchAssets();
    }
}
