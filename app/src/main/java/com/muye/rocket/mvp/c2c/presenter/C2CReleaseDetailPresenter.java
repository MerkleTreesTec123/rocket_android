package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.C2CReleaseEntity;
import com.muye.rocket.mvp.c2c.contract.C2CReleaseDetailContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.List;

public class C2CReleaseDetailPresenter extends BasePresenter<C2CReleaseDetailContract.View> implements C2CReleaseDetailContract.Presenter {
    public C2CReleaseDetailPresenter(C2CReleaseDetailContract.View view) {
        super(view);
    }

    @Override
    public void fetchReleaseDetail(String token, String releaseID) {
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchReleaseDetail(token, releaseID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<C2CReleaseEntity>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindReleaseDetail(null);
                            }

                            @Override
                            public void onNext(List<C2CReleaseEntity> releaseEntityList) {
                                super.onNext(releaseEntityList);
                                C2CReleaseEntity releaseEntity = null;
                                if (releaseEntityList != null && releaseEntityList.size() > 0) {
                                    releaseEntity = releaseEntityList.get(0);
                                }
                                mView.bindReleaseDetail(releaseEntity);
                            }
                        }));
    }
}
