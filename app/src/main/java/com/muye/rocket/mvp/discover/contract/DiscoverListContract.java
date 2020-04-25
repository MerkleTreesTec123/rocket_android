package com.muye.rocket.mvp.discover.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.DiscoverEntity;

import java.util.List;

public interface DiscoverListContract {
    interface View extends BaseView {
        void bindDiscoverList(List<DiscoverEntity> discoverEntityList);
    }

    interface Presenter extends RxPresenter {
        void fetchDiscoverList(String catID, int page);
    }
}
