package com.muye.rocket.mvp.ieo.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.ieo.IEORecord;

import java.util.List;

public interface IEORecordContract {
    interface View extends BaseView{
        void bindRecord(List<IEORecord> recordList);
    }

    interface Presenter extends RxPresenter{
        void fetchRecord(int page);
    }
}
