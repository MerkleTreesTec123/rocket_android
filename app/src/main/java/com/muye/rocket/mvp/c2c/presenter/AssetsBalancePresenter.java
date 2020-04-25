package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.C2CCoinBalance;
import com.muye.rocket.mvp.c2c.contract.AssetsBalanceContract;
import com.muye.rocket.net.CustomResourceSubscriber;

public class AssetsBalancePresenter extends BasePresenter<AssetsBalanceContract.View> implements AssetsBalanceContract.Presenter {
    public AssetsBalancePresenter(AssetsBalanceContract.View view) {
        super(view);
    }

    @Override
    public void fetchAssetsBalance(String coinID) {
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchAssetsBalance(MMKVTools.getToken(), coinID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<C2CCoinBalance>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindAssetsBalance(coinID, "", 0, 0);
                            }

                            @Override
                            public void onNext(C2CCoinBalance balance) {
                                super.onNext(balance);
                                if (balance != null) {
                                    mView.bindAssetsBalance(coinID, balance.getCoinname(), balance.getTotal(), balance.getFrozen());
                                } else {
                                    mView.bindAssetsBalance(coinID, "", 0, 0);
                                }
                            }
                        }));
    }
}
