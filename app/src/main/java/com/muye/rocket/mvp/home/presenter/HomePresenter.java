package com.muye.rocket.mvp.home.presenter;

import android.util.Log;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.cache.IEOCache;
import com.muye.rocket.entity.BannerEntity;
import com.muye.rocket.entity.MarqueeEntity;
import com.muye.rocket.entity.ieo.IEOReleaseEntity;
import com.muye.rocket.mvp.home.contract.HomeContract;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.muye.rocket.tools.SPUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    boolean isFirstIEORelease;

    public HomePresenter(HomeContract.View view) {
        super(view);
        isFirstIEORelease = true;
    }

    /**
     * 获取HomeFragment顶部的banner图
     */
    @Override
    public void fetchBanner() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchBanner()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<BannerEntity>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.bindBanner(null);
                            }

                            @Override
                            public void onNext(List<BannerEntity> bannerEntityList) {
                                super.onNext(bannerEntityList);
                                mView.bindBanner(bannerEntityList);
                            }
                        }));
    }

    /**
     * HomeFragment 中间的banner图
     */
    @Override
    public void fetchAdBanner() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchAdBanner()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<BannerEntity>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.bindAdBanner(null);
                            }

                            @Override
                            public void onNext(List<BannerEntity> bannerEntityList) {
                                super.onNext(bannerEntityList);
                                mView.bindAdBanner(bannerEntityList);
                            }
                        }));
    }

    /**
     * 文字轮播
     */
    @Override
    public void fetchMarquee() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchMarquee(MMKVTools.getUID())//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<MarqueeEntity>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.bindMarqueeData(null);
                            }

                            @Override
                            public void onNext(List<MarqueeEntity> marqueeList) {
                                super.onNext(marqueeList);
                                Log.e("測試", "onNext: " + marqueeList.size() );
                                mView.bindMarqueeData(marqueeList);
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
     * 测试通道，开启交易前测试，已开启，不用了
     */
    @Override
    public void fetchShow() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .showTrader()
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<Integer>() {
                            @Override
                            public void onError(ApiException exception) {
                                IEOCache.saveIEOReleased(0);
                                IEOCache.saveIEOUnReleased(0);
                                mView.isShowTrader(null);
                            }

                            @Override
                            public void onNext(Integer isShow) {
                                super.onNext(isShow);
                                mView.isShowTrader(isShow);
                            }
                        }));
    }

    /**
     * 保存到服务器用户的推送id
     * 只向服务器发送数据，不对返回数据做任何处理
     */
    @Override
    public void saveUserPushId() {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                .putUserRegistId(MMKVTools.getUID(), SPUtils.getRegistId(),"1")
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribeWith(new CustomResourceSubscriber<BaseEntity>(){
                    @Override
                    public void onError(ApiException exception) {

                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        super.onNext(baseEntity);
                    }
                })
        );
    }
}
