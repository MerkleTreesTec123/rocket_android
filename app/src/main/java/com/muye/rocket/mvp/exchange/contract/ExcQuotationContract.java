package com.muye.rocket.mvp.exchange.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exchange.ExcTradingArea;
import com.muye.rocket.entity.exchange.ExcTradingPair;

import java.util.List;

public interface ExcQuotationContract {
    interface View extends BaseView {
        void bindTradingArea(List<ExcTradingArea> tradingAreaList);

        void bindTradingPair(int tag, String tradingAreaName, List<ExcTradingPair> tradingPairList);

    }

    interface Presenter extends RxPresenter {
        void fetchTradingArea();

        void fetchTradingPairList(String tradingAreaCode,String tradingAreaName);
    }
}
