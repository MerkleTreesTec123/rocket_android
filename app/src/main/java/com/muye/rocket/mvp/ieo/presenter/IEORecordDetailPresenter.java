package com.muye.rocket.mvp.ieo.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.ieo.IEORecordDetail;
import com.muye.rocket.mvp.ieo.contract.IEORecordDetailContract;

public class IEORecordDetailPresenter extends BasePresenter<IEORecordDetailContract.View> implements IEORecordDetailContract.Presenter {
    public IEORecordDetailPresenter(IEORecordDetailContract.View view) {
        super(view);
    }

    @Override
    public void fetchIEORecordDetail(String id, int page) {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchIEORecordDetail(MMKVTools.getToken(), id, page)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<IEORecordDetail>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindIEORecordDetail(null);
                            }

                            @Override
                            public void onNext(IEORecordDetail detail) {
                                super.onNext(detail);
                                mView.bindIEORecordDetail(detail);
                            }
                        }));
    }
}
