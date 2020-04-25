package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.InviteRecord;

import java.util.List;

public interface InviteRecordContract {
    interface View extends BaseView{
        void bindInviteRecord(List<InviteRecord> recordList);
    }
    interface Presenter extends RxPresenter{
        void fetchInviteRecord(int page);
    }
}
