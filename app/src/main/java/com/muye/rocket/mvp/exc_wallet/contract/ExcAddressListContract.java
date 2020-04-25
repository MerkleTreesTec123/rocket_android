package com.muye.rocket.mvp.exc_wallet.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exc_wallet.ExcAddress;

import java.util.List;

public interface ExcAddressListContract {
    interface View extends BaseView {
        void bindAddress(List<ExcAddress> addressList);

        void onDeleteAddressSuccess(String addressID);
    }

    interface Presenter extends RxPresenter {
        void fetchAddress(String coinID);

        void deleteAddress(String addressID);
    }
}
