package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.C2CTransactionInfo;
import com.muye.rocket.mvp.c2c.contract.C2CTransactionListContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.ArrayList;
import java.util.List;

public class C2CTransactionListPresenter extends BasePresenter<C2CTransactionListContract.View> implements C2CTransactionListContract.Presenter {
    public C2CTransactionListPresenter(C2CTransactionListContract.View view) {
        super(view);
    }

    @Override
    public void fetchC2CTransactionInfo(String token, String coinID, String releaseID, String type, int page) {
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchMyReleaseList(token, coinID, releaseID, type, page)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<C2CTransactionInfo>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindListC2CTransactionInfo(new ArrayList<>());
                            }

                            @Override
                            public void onNext(List<C2CTransactionInfo> transactionInfoList) {
                                super.onNext(transactionInfoList);
                                mView.bindListC2CTransactionInfo(transactionInfoList);
                            }
                        }));
    }
}
