package com.muye.rocket.mvp.exc_wallet.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ExcWalletApiService;
import com.muye.rocket.entity.exc_wallet.ExcAddress;
import com.muye.rocket.entity.exc_wallet.ExcAddressListEntity;
import com.muye.rocket.mvp.exc_wallet.contract.ExcAddressListContract;

import java.util.List;

public class ExcAddressListPresenter extends BasePresenter<ExcAddressListContract.View> implements ExcAddressListContract.Presenter {
    public ExcAddressListPresenter(ExcAddressListContract.View view) {
        super(view);
    }

    @Override
    public void fetchAddress(String coinID) {
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .fetchAddress(MMKVTools.getToken(), coinID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcAddressListEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindAddress(null);
                            }

                            @Override
                            public void onNext(ExcAddressListEntity addressListEntity) {
                                super.onNext(addressListEntity);
                                List<ExcAddress> addressList = null;
                                if (addressListEntity != null) {
                                    addressList = addressListEntity.getWithdrawAddress();
                                }
                                mView.bindAddress(addressList);
                            }
                        }));
    }

    @Override
    public void deleteAddress(String addressID) {
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .deleteAddress(MMKVTools.getToken(), addressID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                super.onNext(entity);
                                mView.onDeleteAddressSuccess(addressID);
                            }
                        }));
    }
}
