package com.muye.rocket.mvp.home.presenter;

import android.text.TextUtils;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.mvp.home.contract.SystemTimeContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.concurrent.TimeUnit;

public class SystemTimePresenter extends BasePresenter<SystemTimeContract.View> implements SystemTimeContract.Presenter {

    boolean isFirst;

    public SystemTimePresenter(SystemTimeContract.View view) {
        super(view);
        isFirst = true;
    }

    @Override
    public void fetchSystemTime() {
        boolean isDelay = !isFirst;
        if (isFirst) {
            isFirst = false;
        }
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchSystemTime()//方法
                        .delay(isDelay ? 2 : 0, TimeUnit.SECONDS)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<String>() {
                            @Override
                            public void onError(ApiException exception) {
                                fetchSystemTime();
                            }

                            @Override
                            public void onNext(String time) {
                                super.onNext(time);
                                if (TextUtils.isEmpty(time)) {
                                    mView.bindSystemTime(0);
                                } else {
                                    mView.bindSystemTime(Long.parseLong(time));
                                }
                            }
                        }));
    }

    @Override
    public void fetchSystemApi() {

    }


}
