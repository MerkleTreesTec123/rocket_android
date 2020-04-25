package com.muye.rocket.mvp.me.splash;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.net.CustomResourceSubscriber;

public class SplashPresenter extends BasePresenter<SplashContract.View>  implements  SplashContract.Presenter{
    public SplashPresenter(SplashContract.View view) {
        super(view);
    }

    @Override
    public void getOpenWs() {
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .getOpenWs()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<Integer>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(Integer code) {
                                mView.saveCode(code);
                            }
                        }));
    }
}
