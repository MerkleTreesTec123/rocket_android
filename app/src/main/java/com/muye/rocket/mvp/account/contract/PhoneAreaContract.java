package com.muye.rocket.mvp.account.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.PhoneArea;

import java.util.List;

public interface PhoneAreaContract {
    interface View extends BaseView {
        void bindPhoneArea(List<PhoneArea> areaList);
    }

    interface Presenter extends RxPresenter {
        void fetchPhoneArea();
    }
}
