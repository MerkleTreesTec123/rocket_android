package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface UpdateOrderStatusContract {
    interface View extends BaseView {
        void onUpdateOrderStatusSuccess();
    }

    interface Presenter extends RxPresenter {
        void updateOrderStatus(String orderID, String payType, String reason, String imageURL, String status);
    }
}
