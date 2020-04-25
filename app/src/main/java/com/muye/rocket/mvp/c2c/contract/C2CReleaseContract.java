package com.muye.rocket.mvp.c2c.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface C2CReleaseContract {

    interface View extends BaseView {
        void onC2CReleaseSuccess(String releaseID);

        void onC2CReleaseFail(int code, String message);
    }


    interface Presenter extends RxPresenter {
        /**
         * @param coinID
         * @param coinName
         * @param price
         * @param num
         * @param min
         * @param max
         * @param payType  1、银行卡 2、支付宝 3、微信 4、银行卡支付宝 5、银行卡微信 6、支付宝微信 7、银行卡支付宝微信
         * @param type     1、买入 2卖出
         * @param password
         */
        void submitC2CRelease(String coinID, String coinName, String price, String num, String min,
                              String max, String payType, String type, String password);
    }
}
