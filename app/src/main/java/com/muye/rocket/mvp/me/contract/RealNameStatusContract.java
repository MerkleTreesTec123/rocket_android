package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.SafeSettingEntity;

public interface RealNameStatusContract {
    interface View extends BaseView {
        void bindRealNameStatus(SafeSettingEntity safeSetting);
        void bindRealResult(String reason);
    }

    interface Presenter extends RxPresenter {
        void fetchRealNameStatus();
        void fetchReject();
    }
}
