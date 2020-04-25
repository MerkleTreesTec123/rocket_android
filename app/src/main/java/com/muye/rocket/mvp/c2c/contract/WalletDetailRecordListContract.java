package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.WalletDetailRecord;

import java.util.List;

public interface WalletDetailRecordListContract {
    interface View extends BaseView {
        void bindWalletDetailRecord(List<WalletDetailRecord> recordList);
    }

    interface Presenter extends RxPresenter {
        void fetchWalletDetailRecord(String coinID, String type, int page);
    }
}
