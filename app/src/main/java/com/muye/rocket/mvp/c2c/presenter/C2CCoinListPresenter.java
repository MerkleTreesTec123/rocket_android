package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.C2CCoin;
import com.muye.rocket.mvp.c2c.contract.C2CCoinListContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.List;

public class C2CCoinListPresenter extends BasePresenter<C2CCoinListContract.View> implements C2CCoinListContract.Presenter {
    public C2CCoinListPresenter(C2CCoinListContract.View view) {
        super(view);
    }

    @Override
    public void fetchCoinList() {
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchCoinList()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<C2CCoin>>() {
                            @Override
                            public void onError(ApiException exception) {
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindCoinList(null);
                            }

                            @Override
                            public void onNext(List<C2CCoin> coinList) {
                                super.onNext(coinList);
                                mView.bindCoinList(coinList);
                            }
                        }));
    }
}
