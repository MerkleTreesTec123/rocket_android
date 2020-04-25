package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.C2CReleaseEntity;
import com.muye.rocket.mvp.c2c.contract.C2CReleaseListContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.List;

public class C2CReleaseListPresenter extends BasePresenter<C2CReleaseListContract.View> implements C2CReleaseListContract.Presenter {
    public C2CReleaseListPresenter(C2CReleaseListContract.View view) {
        super(view);
    }

    @Override
    public void fetchReleaseList(String token, String coinID, String orderType, int page) {
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchReleaseList(token, coinID, orderType, page)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<C2CReleaseEntity>>() {
                            @Override
                            public void onError(ApiException exception) {
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindReleaseList(orderType, null);
                            }

                            @Override
                            public void onNext(List<C2CReleaseEntity> entityList) {
                                super.onNext(entityList);
                                mView.bindReleaseList(orderType,entityList);
                            }
                        }));
    }
}
