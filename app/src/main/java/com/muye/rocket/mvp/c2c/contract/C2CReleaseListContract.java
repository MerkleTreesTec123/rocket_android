package com.muye.rocket.mvp.c2c.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.C2CReleaseEntity;

import java.util.List;

public interface C2CReleaseListContract {
    interface View extends BaseView {
        void bindReleaseList(String orderType, List<C2CReleaseEntity> entityList);
    }

    interface Presenter extends RxPresenter {
        void fetchReleaseList(String token, String coinID, String orderType, int page);
    }
}
