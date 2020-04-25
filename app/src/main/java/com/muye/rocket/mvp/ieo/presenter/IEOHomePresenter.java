package com.muye.rocket.mvp.ieo.presenter;


import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.cache.IEOCache;
import com.muye.rocket.entity.ieo.IEOHomeEntity;
import com.muye.rocket.entity.ieo.IEOProgress;
import com.muye.rocket.entity.ieo.IEOReleaseEntity;
import com.muye.rocket.mvp.ieo.contract.IEOHomeContract;

import java.util.concurrent.TimeUnit;

public class IEOHomePresenter extends BasePresenter<IEOHomeContract.View> implements IEOHomeContract.Presenter {
    boolean isFirstIEORelease;

    public IEOHomePresenter(IEOHomeContract.View view) {
        super(view);
        isFirstIEORelease = true;
    }

    @Override
    public void fetchHomeData(boolean isDelay) {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchIEOHomeData()//方法
                        .delay(isDelay ? 2 : 0, TimeUnit.SECONDS)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<IEOHomeEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.bindIEOHomeData(null);
                            }

                            @Override
                            public void onNext(IEOHomeEntity homeEntity) {
                                super.onNext(homeEntity);
                                mView.bindIEOHomeData(homeEntity);
                            }
                        }));
    }

    @Override
    public void submitBuyIEO(String password, String parID, String phase, String ufoNum) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .submitBuyIEO(MMKVTools.getToken(), password, parID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onBuyFail(exception.getCode(), exception.getDisplayMessage());
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                super.onNext(entity);
                                mView.hideLoadDialog();
                                mView.onBuySuccess(phase, ufoNum);
                            }
                        }));
    }

    @Override
    public void fetchIEOProgress() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchIEOProgress(MMKVTools.getToken())//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<IEOProgress>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.bindIEOProgress(null);
                            }

                            @Override
                            public void onNext(IEOProgress progress) {
                                super.onNext(progress);
                                mView.bindIEOProgress(progress);
                            }
                        }));
    }

    @Override
    public void fetchIEORelease() {
        boolean isDelay = true;
        if (isFirstIEORelease) {
            isDelay = false;
            isFirstIEORelease = false;
        }
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchIEORelease(MMKVTools.getToken())//方法
                        .delay(isDelay ? 2 : 0, TimeUnit.SECONDS)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<IEOReleaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                IEOCache.saveIEOReleased(0);
                                IEOCache.saveIEOUnReleased(0);
                                mView.bindIEORelease(null);
                            }

                            @Override
                            public void onNext(IEOReleaseEntity releaseEntity) {
                                super.onNext(releaseEntity);
                                if (releaseEntity != null) {
                                    IEOCache.saveIEOReleased(releaseEntity.getRelease_amount());
                                    IEOCache.saveIEOUnReleased(releaseEntity.getLock_amount());
                                } else {
                                    IEOCache.saveIEOReleased(0);
                                    IEOCache.saveIEOUnReleased(0);
                                }
                                mView.bindIEORelease(releaseEntity);
                            }
                        }));
    }

    /**
     * 查询要显示的webUrl
     */
    @Override
    public void fetchShowUrl() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .showSplanUrl()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<String>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.showUrl(null);
                            }

                            @Override
                            public void onNext(String webUrl) {
                                super.onNext(webUrl);
                                mView.showUrl(webUrl);
                            }
                        }));
    }
}
