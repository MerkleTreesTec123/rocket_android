package com.muye.rocket.mvp.c2c.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.C2CReleaseEntity;

public interface C2CReleaseDetailContract {
    interface View extends BaseView {
        void bindReleaseDetail(C2CReleaseEntity releaseEntity);
    }

    interface Presenter extends RxPresenter {
        void fetchReleaseDetail(String token, String releaseID);
    }
}
