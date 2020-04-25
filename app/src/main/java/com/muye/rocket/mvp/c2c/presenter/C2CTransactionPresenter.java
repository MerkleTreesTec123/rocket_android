package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.mvp.c2c.contract.C2CTransactionContract;
import com.muye.rocket.net.CustomResourceSubscriber;

public class C2CTransactionPresenter extends BasePresenter<C2CTransactionContract.View> implements C2CTransactionContract.Presenter {
    public C2CTransactionPresenter(C2CTransactionContract.View view) {
        super(view);
    }

    @Override
    public void submitTransaction(String releaseID, String num, String type, String payType, String password) {
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .submitTransaction(MMKVTools.getToken(), releaseID, num, type, payType, password)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<String>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(String orderID) {
                                super.onNext(orderID);
                                mView.onSubmitTransactionSuccess(orderID);
                            }
                        }));
    }
}
