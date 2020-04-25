package com.muye.rocket.mvp.exc_wallet.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exc_wallet.ExcAssetsDetail;

public interface ExcWalletDetailContract {
    interface View extends BaseView {
        void bindAssetsDetail(ExcAssetsDetail assetsDetail);
    }

    interface Presenter extends RxPresenter {
        void fetchAssetsDetail(String coinID);
    }
}
