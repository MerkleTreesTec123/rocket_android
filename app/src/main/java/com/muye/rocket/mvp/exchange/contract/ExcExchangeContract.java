package com.muye.rocket.mvp.exchange.contract;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ExcExchangeContract {
    String EXC_ORDER_TYPE_ALL = "0";
    String EXC_ORDER_TYPE_CURRENT = "1";
    String EXC_ORDER_TYPE_HISTORY = "2";

    int EXCHANGE_TYPE_BUY = 1;
    int EXCHANGE_TYPE_SELL = 2;

    interface View extends BaseView {
        void bindTradingPairDetail(String tradingPairID, ExcTradingPairDetail tradingPairDetail);

//        void bindOrder(String sellName, String buyName, ExcOrderEntity orderEntity, @ExcOrderType String type, boolean interval);

//        void bindBalance(String tradingPairID, ExcTradingPairBalance balance, boolean interval);

        void onSubmitExchangeSuccess(String sellName, String buyName);

//        void onCancelOrderSuccess();
    }

    interface Presenter extends RxPresenter {
        void fetchTradingPairDetail(String tradingPairID);

//        void fetchOrder(String sellName, String buyName, @ExcOrderType String type, boolean isDelay, boolean interval);

//        void fetchBalance(String tradingPairID, boolean isDelay, boolean interval);

        void submitExchange(String sellName, String buyName, String num, String price, @ExchangeType int type, String password);

//        void cancelOrder(String orderID);
    }

    @StringDef({EXC_ORDER_TYPE_ALL, EXC_ORDER_TYPE_CURRENT, EXC_ORDER_TYPE_HISTORY})
    @Retention(RetentionPolicy.SOURCE)
    @interface ExcOrderType {

    }

    @IntDef({EXCHANGE_TYPE_BUY, EXCHANGE_TYPE_SELL})
    @Retention(RetentionPolicy.SOURCE)
    @interface ExchangeType {

    }
}
