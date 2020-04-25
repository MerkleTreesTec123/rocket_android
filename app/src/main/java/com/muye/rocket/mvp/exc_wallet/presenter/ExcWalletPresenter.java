package com.muye.rocket.mvp.exc_wallet.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ApiSignTools;
import com.muye.rocket.api.ExcWalletApiService;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWalletContract;

import java.util.HashMap;
import java.util.Map;

public class ExcWalletPresenter extends BasePresenter<ExcWalletContract.View> implements ExcWalletContract.Presenter {
    public ExcWalletPresenter(ExcWalletContract.View view) {
        super(view);
    }

    @Override
    public void fetchAssets() {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("base", "CNY");
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .fetchAssets(ApiSignTools.convertQueryMap(params))//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcAssetsEntity>() {
                            @Override
                            public void onError(ApiException exception) {
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindAssets(null);
                            }

                            @Override
                            public void onNext(ExcAssetsEntity assets) {
                                super.onNext(assets);
                                mView.bindAssets(assets);
                            }
                        }));
    }
}
