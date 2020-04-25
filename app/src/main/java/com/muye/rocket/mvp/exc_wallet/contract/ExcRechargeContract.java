package com.muye.rocket.mvp.exc_wallet.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.entity.exc_wallet.ExcRechargeAddress;

import java.util.List;

public interface ExcRechargeContract {
    interface View extends BaseView {
        void bindCoinList(List<ExcAssetsEntity.WalletBean> coinList);

        void bindRechargeAddress(ExcRechargeAddress address);
    }

    interface Presenter extends RxPresenter {
        void fetchCoinList();

        void fetchAddress(String coinID);
    }
}
