package com.muye.rocket.mvp.home.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.BannerEntity;
import com.muye.rocket.entity.MarqueeEntity;
import com.muye.rocket.entity.ieo.IEOReleaseEntity;

import java.util.List;

public interface HomeContract {
    interface View extends BaseView {
        void bindBanner(List<BannerEntity> bannerEntityList);
        void bindAdBanner(List<BannerEntity> bannerEntityList);

        void bindMarqueeData(List<MarqueeEntity> marqueeList);

        void bindIEORelease(IEOReleaseEntity releaseEntity);

        void isShowTrader(Integer isShow);
    }

    interface Presenter extends RxPresenter {
        void fetchBanner();
        void fetchAdBanner();
        void fetchMarquee();

        void fetchIEORelease();

        void fetchShow();

        void saveUserPushId();
    }
}
