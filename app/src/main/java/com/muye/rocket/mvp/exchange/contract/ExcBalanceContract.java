package com.muye.rocket.mvp.exchange.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exc_wallet.DealOperaEntity;
import com.muye.rocket.entity.exchange.ExcTradingPairBalance;

public interface ExcBalanceContract {
    interface View extends BaseView{
        void bindBalance(String tradingPairID, ExcTradingPairBalance balance);
        //添加控制是否可以点击买卖
        void isDeal(DealOperaEntity operaEntity);
    }

    interface Presenter extends RxPresenter {
        void fetchBalance(String tradingPairID, boolean isDelay);
        void fetchDealRealt();
    }
}
