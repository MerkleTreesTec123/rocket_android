package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.SafeSettingEntity;

public interface SafeCenterContract {
    interface View extends BaseView {
        void bindRealNameStatus(SafeSettingEntity safeSetting);
    }

    interface Presenter extends RxPresenter {
        void fetchRealNameStatus();
    }
}
