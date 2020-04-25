package com.muye.rocket.mvp.discover.presenter;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.DiscoverCat;
import com.muye.rocket.mvp.discover.contract.DiscoverCatContract;

import java.util.List;

public class DiscoverCatPresenter extends BasePresenter<DiscoverCatContract.View> implements DiscoverCatContract.Presenter {
    public DiscoverCatPresenter(DiscoverCatContract.View view) {
        super(view);
    }

    @Override
    public void fetchCatList() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchDiscoverCat()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<DiscoverCat>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindCatList(null);
                            }

                            @Override
                            public void onNext(List<DiscoverCat> catList) {
                                super.onNext(catList);
                                mView.bindCatList(catList);
                            }
                        }));
    }
}
