package com.muye.rocket.mvp.exchange.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ExcApiService;
import com.muye.rocket.entity.exchange.ExcUSDT2RMBRate;
import com.muye.rocket.mvp.exchange.contract.ExcUSDT2RMBContract;

public class ExcUSDT2RMBPresenter extends BasePresenter<ExcUSDT2RMBContract.View> implements ExcUSDT2RMBContract.Presenter {
    public ExcUSDT2RMBPresenter(ExcUSDT2RMBContract.View view) {
        super(view);
    }

    @Override
    public void fetchUSDT2RMBRate() {
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchUSDT2RMBRate()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcUSDT2RMBRate>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(ExcUSDT2RMBRate rate) {
                                super.onNext(rate);
                                mView.bindUSDT2RMBRate(rate.getCNY());
                            }
                        }));
    }

}
