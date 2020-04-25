package com.muye.rocket.mvp.exchange.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exchange.ExcCoinIntroduction;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;
import com.muye.rocket.entity.exchange.KLineEntity;

import java.util.List;

public interface TradingPairDetailContract {
    interface View extends BaseView {
        void bindKLineData(String tradingPairID, String time, List<KLineEntity> kLineData);

        void bindTradingPairDetail(String tradingPairID, ExcTradingPairDetail tradingPairDetail);

        void bindCoinIntroduction(ExcCoinIntroduction introduction);
    }

    interface Presenter extends RxPresenter {
        void fetchKLineData(String tradingPairID, String time, boolean isDelay);

        void fetchTradingPairDetail(String tradingPairID, boolean isDelay);

        void fetchCoinIntroduction(String coinID);
    }
}
