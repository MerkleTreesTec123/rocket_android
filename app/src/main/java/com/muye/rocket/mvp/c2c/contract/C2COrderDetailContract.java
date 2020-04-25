package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.C2COrder;

public interface C2COrderDetailContract {
    interface View extends BaseView {
        void bindC2COrderDetail(C2COrder order);
    }

    interface Presenter extends RxPresenter {
        void fetchC2COrderDetail(String orderID);
    }
}
