package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.WalletEntity;
import com.muye.rocket.mvp.c2c.contract.WalletContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.concurrent.TimeUnit;

public class WalletPresenter extends BasePresenter<WalletContract.View> implements WalletContract.Presenter {
    boolean isFirst;

    public WalletPresenter(WalletContract.View view) {
        super(view);
        isFirst = true;
    }

    @Override
    public void fetchWallet() {
        boolean isDelay = !isFirst;
        isFirst = false;
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchWallet(MMKVTools.getToken())//方法
                        .delay(isDelay ? 2 : 0, TimeUnit.SECONDS)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<WalletEntity>() {
                            @Override
                            public void onError(ApiException exception) {
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindWallet(null);
                            }

                            @Override
                            public void onNext(WalletEntity walletEntity) {
                                super.onNext(walletEntity);
                                mView.bindWallet(walletEntity);
                            }
                        }));
    }
}
