package com.muye.rocket.mvp.exc_wallet.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ExcWalletApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.exc_wallet.AddressInfo;
import com.muye.rocket.mvp.exc_wallet.contract.ExcAddressInoContract;

public class ExcAddressInoPresenter extends BasePresenter<ExcAddressInoContract.View> implements ExcAddressInoContract.Presenter {

    public ExcAddressInoPresenter(ExcAddressInoContract.View view) {
        super(view);
    }

    @Override
    public void fetchAddressInfo(String address) {
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .fetchAddressInfo(MMKVTools.getToken(), address)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<AddressInfo>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.bindAddressInfo(null);
                            }

                            @Override
                            public void onNext(AddressInfo addressInfo) {
                                super.onNext(addressInfo);
                                mView.bindAddressInfo(addressInfo);
                            }
                        }));
    }
}
