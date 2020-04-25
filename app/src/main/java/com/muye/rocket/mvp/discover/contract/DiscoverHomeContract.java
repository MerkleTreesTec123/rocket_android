package com.muye.rocket.mvp.discover.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.DiscoverBannerEntity;
import com.muye.rocket.entity.DiscoverEntity;
import com.muye.rocket.entity.DiscoverHomeEntity;

import java.util.List;

public interface DiscoverHomeContract {
    interface View extends BaseView {
        void bindBanner(List<DiscoverBannerEntity> bannerList);

        void binRecommend(List<DiscoverEntity> recommendData);

        void bindHomeList(List<DiscoverHomeEntity> homeEntityList);
    }

    interface Presenter extends RxPresenter {
        void fetchBanner();

        void fetchRecommend();

        void fetchHomeList();
    }
}
