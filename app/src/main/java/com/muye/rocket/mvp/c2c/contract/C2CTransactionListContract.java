package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.C2CTransactionInfo;

import java.util.List;

public interface C2CTransactionListContract {
    interface View extends BaseView {
        void bindListC2CTransactionInfo(List<C2CTransactionInfo> transactionInfoList);
    }

    interface Presenter extends RxPresenter {
        void fetchC2CTransactionInfo(String token, String coinID, String releaseID, String type, int page);
    }
}
