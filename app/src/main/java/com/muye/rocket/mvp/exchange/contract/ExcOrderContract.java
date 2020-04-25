package com.muye.rocket.mvp.exchange.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exchange.ExcOrderEntity;

public interface ExcOrderContract {
    interface View extends BaseView{
        void bindOrder(String sellName, String buyName, ExcOrderEntity orderEntity, @ExcExchangeContract.ExcOrderType String type);

        void onCancelOrderSuccess();
    }

    interface Presenter extends RxPresenter{
        void fetchOrder(String sellName, String buyName, @ExcExchangeContract.ExcOrderType String type, boolean isDelay);

        void cancelOrder(String orderID);
    }
}
