package com.muye.rocket.mvp.ieo.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.ieo.IEORecordDetail;

public interface IEORecordDetailContract {
    interface View extends BaseView {
        void bindIEORecordDetail(IEORecordDetail recordDetail);
    }

    interface Presenter extends RxPresenter {
        void fetchIEORecordDetail(String id, int page);
    }
}
