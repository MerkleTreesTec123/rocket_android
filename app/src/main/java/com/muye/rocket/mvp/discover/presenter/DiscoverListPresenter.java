package com.muye.rocket.mvp.discover.presenter;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.DiscoverEntity;
import com.muye.rocket.mvp.discover.contract.DiscoverListContract;

import java.util.List;

public class DiscoverListPresenter extends BasePresenter<DiscoverListContract.View> implements DiscoverListContract.Presenter {
    public DiscoverListPresenter(DiscoverListContract.View view) {
        super(view);
    }

    @Override
    public void fetchDiscoverList(String catID, int page) {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchDiscoverList(catID, page)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<DiscoverEntity>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindDiscoverList(null);
                            }

                            @Override
                            public void onNext(List<DiscoverEntity> entityList) {
                                super.onNext(entityList);
                                mView.bindDiscoverList(entityList);
                            }
                        }));
    }
}
