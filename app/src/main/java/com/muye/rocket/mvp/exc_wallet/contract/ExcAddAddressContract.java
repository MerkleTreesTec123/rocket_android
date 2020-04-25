package com.muye.rocket.mvp.exc_wallet.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;

import java.util.List;

public interface ExcAddAddressContract {
    interface View extends BaseView {
        void bindCoinList(List<ExcAssetsEntity.WalletBean> coinList);

        void onAddAddressSuccess(String coinID);
    }

    interface Presenter extends RxPresenter {
        void fetchCoinList();

        void submitAddAddress(String coinID, String address, String remark, String code, String password, String googleCode);
    }
}
