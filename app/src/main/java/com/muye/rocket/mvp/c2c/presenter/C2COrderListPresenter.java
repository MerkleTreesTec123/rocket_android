package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.C2COrder;
import com.muye.rocket.mvp.c2c.contract.C2COrderListContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.List;

public class C2COrderListPresenter extends BasePresenter<C2COrderListContract.View> implements C2COrderListContract.Presenter {
    public C2COrderListPresenter(C2COrderListContract.View view) {
        super(view);
    }

    @Override
    public void fetchOrderList(String toke, String orderType, String coinID, String releaseID, String orderID, int page) {
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchOrderList(toke, orderType, coinID, releaseID, orderID, page)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<C2COrder>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindOrderList(null);
                            }

                            @Override
                            public void onNext(List<C2COrder> orderList) {
                                super.onNext(orderList);
                                mView.bindOrderList(orderList);
                            }
                        }));
    }
}
