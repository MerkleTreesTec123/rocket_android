package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.exc_wallet.RocketBalance;

import java.util.List;

public interface WithDrawContract {

    interface View extends BaseView {
        void withDrawCat(int status);
        void searchRocketBalance(List<RocketBalance> balanceList,BindWalletBean walletBean);
    }

    interface Presenter extends RxPresenter {
        void fetchWithDrawCat(String catId,String coinName,String rocketUid,String num,String password);
        void fetchRocketBalance(BindWalletBean walletBean);
    }

}
