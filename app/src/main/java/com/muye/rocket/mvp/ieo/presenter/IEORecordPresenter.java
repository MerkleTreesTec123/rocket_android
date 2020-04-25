package com.muye.rocket.mvp.ieo.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.ieo.IEORecord;
import com.muye.rocket.mvp.ieo.contract.IEORecordContract;

import java.util.List;

public class IEORecordPresenter extends BasePresenter<IEORecordContract.View> implements IEORecordContract.Presenter {
    public IEORecordPresenter(IEORecordContract.View view) {
        super(view);
    }

    @Override
    public void fetchRecord(int page) {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchIEORecord(MMKVTools.getToken(), page)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<IEORecord>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindRecord(null);
                            }

                            @Override
                            public void onNext(List<IEORecord> recordList) {
                                super.onNext(recordList);
                                mView.bindRecord(recordList);
                            }
                        }));
    }
}
