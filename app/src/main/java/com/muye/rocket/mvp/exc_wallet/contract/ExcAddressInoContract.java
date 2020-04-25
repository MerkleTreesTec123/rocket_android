package com.muye.rocket.mvp.exc_wallet.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exc_wallet.AddressInfo;

public interface ExcAddressInoContract {
    interface View extends BaseView {
        void bindAddressInfo(AddressInfo addressInfo);
    }

    interface Presenter extends RxPresenter {
        void fetchAddressInfo(String address);
    }
}
