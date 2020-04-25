package com.muye.rocket.mvp.exc_wallet.contract;

import android.support.annotation.StringDef;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.exc_wallet.ExcWalletRecord;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface ExcWalletRecordContract {
    String RECORD_TYPE_RECHARGE = "1";
    String RECORD_TYPE_WITHDRAW = "2";
    String RECORD_TYPE_ASSETS_2_C2C = "7";
    String RECORD_TYPE_C2C_2_ASSETS = "8";

    interface View extends BaseView {
        void bindWalletRecord(List<ExcWalletRecord> recordList, @RecordType String recordType);
    }

    interface Presenter extends RxPresenter {
        void fetchWalletRecord(String coinID, @RecordType String recordType, int page);
    }

    @Retention(SOURCE)
    @StringDef({RECORD_TYPE_RECHARGE, RECORD_TYPE_WITHDRAW, RECORD_TYPE_ASSETS_2_C2C, RECORD_TYPE_C2C_2_ASSETS})
    @interface RecordType {

    }
}
