package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.C2CPayAccountInfo;
import com.muye.rocket.mvp.c2c.contract.C2CPayAccountContract;
import com.muye.rocket.net.CustomResourceSubscriber;

public class C2CPayAccountPresenter extends BasePresenter<C2CPayAccountContract.View> implements C2CPayAccountContract.Presenter {
    public C2CPayAccountPresenter(C2CPayAccountContract.View view) {
        super(view);
    }

    @Override
    public void fetchC2CPayAccountInfo() {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchC2CPayAccountInfo(MMKVTools.getToken())//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<C2CPayAccountInfo>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(C2CPayAccountInfo accountInfo) {
                                mView.hideLoadDialog();
                                super.onNext(accountInfo);
                                mView.bindC2CPayAccountInfo(accountInfo);
                            }
                        }));
    }
}
