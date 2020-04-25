package com.muye.rocket.mvp.exc_wallet.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exc_wallet.ExcAssetsDetail;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.entity.exc_wallet.ExcWithdrawSetting;

import java.util.List;

public interface ExcWithdrawContract {
    interface View extends BaseView {
        void bindCoinList(List<ExcAssetsEntity.WalletBean> coinList);

        void bindBalance(ExcAssetsDetail assetsDetail);

        void bindWithdrawSetting(ExcWithdrawSetting withdrawSetting);

        void onWithdrawSuccess();
    }

    interface Presenter extends RxPresenter {
        void fetchCoinList();

        void fetchBalance(String coinID);

        void fetchWithdrawSetting(String coinID);

        void submitWithdraw(String coinID, String num, String addressID, String tag, String SMSCode,
                            String password, String googleCode);
    }
}
