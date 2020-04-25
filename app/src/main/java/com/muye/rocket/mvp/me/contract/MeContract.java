package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.SafeSettingEntity;

public interface MeContract {
    interface View extends BaseView {
        void bindSafeSetting(SafeSettingEntity safeSetting);
    }

    interface Presenter extends RxPresenter {
        void fetchSafeSetting();
    }
}
