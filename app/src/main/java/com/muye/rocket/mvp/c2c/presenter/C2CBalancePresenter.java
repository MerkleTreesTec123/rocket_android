package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.C2CCoinBalance;
import com.muye.rocket.mvp.c2c.contract.C2CBalanceContract;
import com.muye.rocket.net.CustomResourceSubscriber;

public class C2CBalancePresenter extends BasePresenter<C2CBalanceContract.View> implements C2CBalanceContract.Presenter {
    public C2CBalancePresenter(C2CBalanceContract.View view) {
        super(view);
    }

    @Override
    public void fetchCoinBalance(String coinID) {
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchCoinBalance(MMKVTools.getToken(), coinID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<C2CCoinBalance>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindCoinBalance(coinID, "", 0, 0);
                            }

                            @Override
                            public void onNext(C2CCoinBalance balance) {
                                super.onNext(balance);
                                if (balance != null) {
                                    mView.bindCoinBalance(coinID, balance.getCoinname(), balance.getTotal(), balance.getFrozen());
                                } else {
                                    mView.bindCoinBalance(coinID, "", 0, 0);
                                }
                            }
                        }));
    }
}
