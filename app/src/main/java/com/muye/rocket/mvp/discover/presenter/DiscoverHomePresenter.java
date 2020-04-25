package com.muye.rocket.mvp.discover.presenter;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.DiscoverBannerEntity;
import com.muye.rocket.entity.DiscoverEntity;
import com.muye.rocket.entity.DiscoverHomeEntity;
import com.muye.rocket.mvp.discover.contract.DiscoverHomeContract;

import java.util.List;

public class DiscoverHomePresenter extends BasePresenter<DiscoverHomeContract.View> implements DiscoverHomeContract.Presenter {
    public DiscoverHomePresenter(DiscoverHomeContract.View view) {
        super(view);
    }

    @Override
    public void fetchBanner() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchDiscoverBanner()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<DiscoverBannerEntity>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindBanner(null);
                            }

                            @Override
                            public void onNext(List<DiscoverBannerEntity> bannerList) {
                                super.onNext(bannerList);
                                mView.bindBanner(bannerList);
                            }
                        }));
    }

    @Override
    public void fetchRecommend() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchDiscoverRecommend()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<DiscoverEntity>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.binRecommend(null);
                            }

                            @Override
                            public void onNext(List<DiscoverEntity> recommendData) {
                                super.onNext(recommendData);
                                mView.binRecommend(recommendData);
                            }
                        }));
    }

    @Override
    public void fetchHomeList() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchDiscoverHomeList()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<DiscoverHomeEntity>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindHomeList(null);
                            }

                            @Override
                            public void onNext(List<DiscoverHomeEntity> homeEntityList) {
                                super.onNext(homeEntityList);
                                mView.bindHomeList(homeEntityList);
                            }
                        }));
    }
}
