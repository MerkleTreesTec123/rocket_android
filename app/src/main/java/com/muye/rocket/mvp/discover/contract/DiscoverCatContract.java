package com.muye.rocket.mvp.discover.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.DiscoverCat;

import java.util.List;

public interface DiscoverCatContract {
    interface View extends BaseView{
        void bindCatList(List<DiscoverCat> catList);
    }

    interface Presenter extends RxPresenter{
        void fetchCatList();
    }
}
