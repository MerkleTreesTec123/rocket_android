package com.muye.rocket.mvp.ieo.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.ieo.IEOHomeEntity;
import com.muye.rocket.entity.ieo.IEOProgress;
import com.muye.rocket.entity.ieo.IEOReleaseEntity;

public interface IEOHomeContract {
    interface View extends BaseView {
        void bindIEOHomeData(IEOHomeEntity homeEntity);

        void onBuySuccess(String phase,String ufoNum);

        void onBuyFail(int code, String message);

        void bindIEOProgress(IEOProgress progress);

        void bindIEORelease(IEOReleaseEntity releaseEntity);

        void showUrl(String webUrl);
    }

    interface Presenter extends RxPresenter {
        void fetchHomeData(boolean isDelay);

        void submitBuyIEO(String password, String parID,String phase, String ufoNum);

        void fetchIEOProgress();

        void fetchIEORelease();

        void fetchShowUrl();
    }
}
