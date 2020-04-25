package com.muye.rocket.mvp.exc_wallet.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ExcWalletApiService;
import com.muye.rocket.entity.exc_wallet.ExcAssetsDetail;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWalletDetailContract;

public class ExcWalletDetailPresenter extends BasePresenter<ExcWalletDetailContract.View> implements ExcWalletDetailContract.Presenter {
    public ExcWalletDetailPresenter(ExcWalletDetailContract.View view) {
        super(view);
    }

    @Override
    public void fetchAssetsDetail(String coinID) {
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .fetchAssetsDetail(MMKVTools.getToken(), coinID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcAssetsDetail>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindAssetsDetail(null);
                            }

                            @Override
                            public void onNext(ExcAssetsDetail assetsDetail) {
                                super.onNext(assetsDetail);
                                mView.bindAssetsDetail(assetsDetail);
                            }
                        }));
    }
}
