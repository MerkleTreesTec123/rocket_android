package com.muye.rocket.mvp.exchange.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exchange.ExcOrder;

import java.util.List;

public interface ExcOrderListContract {
    interface View extends BaseView {
        void bindOrderList(List<ExcOrder> orderList, String type);
    }

    interface Presenter extends RxPresenter {
        void fetchOrderList(String type);
    }
}
