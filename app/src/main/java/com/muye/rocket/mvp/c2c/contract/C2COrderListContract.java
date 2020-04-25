package com.muye.rocket.mvp.c2c.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.C2COrder;

import java.util.List;

public interface C2COrderListContract {
    interface View extends BaseView {
        void bindOrderList(List<C2COrder> orderList);
    }

    interface Presenter extends RxPresenter {
        void fetchOrderList(String toke, String orderType, String coinID, String releaseID, String orderID, int page);
    }
}
