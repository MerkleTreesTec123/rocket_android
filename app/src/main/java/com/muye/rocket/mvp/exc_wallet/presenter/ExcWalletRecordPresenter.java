package com.muye.rocket.mvp.exc_wallet.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ExcWalletApiService;
import com.muye.rocket.entity.exc_wallet.ExcWalletRecord;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWalletRecordContract;

import java.util.List;

public class ExcWalletRecordPresenter extends BasePresenter<ExcWalletRecordContract.View> implements ExcWalletRecordContract.Presenter {
    public ExcWalletRecordPresenter(ExcWalletRecordContract.View view) {
        super(view);
    }

    @Override
    public void fetchWalletRecord(String coinID, String recordType, int page) {
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .fetchWalletRecord(MMKVTools.getToken(), coinID, recordType, page)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<ExcWalletRecord>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindWalletRecord(null, recordType);
                            }

                            @Override
                            public void onNext(List<ExcWalletRecord> recordList) {
                                super.onNext(recordList);
                                mView.bindWalletRecord(recordList, recordType);
                            }
                        }));
    }
}
