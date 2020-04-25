package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.c2c.C2COrder;
import com.muye.rocket.mvp.c2c.contract.C2COrderDetailContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.List;

public class C2COrderDetailPresenter extends BasePresenter<C2COrderDetailContract.View> implements C2COrderDetailContract.Presenter {
    public C2COrderDetailPresenter(C2COrderDetailContract.View view) {
        super(view);
    }

    @Override
    public void fetchC2COrderDetail(String orderID) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .fetchC2COrderDetail(MMKVTools.getToken(), orderID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<C2COrder>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindC2COrderDetail(null);
                            }

                            @Override
                            public void onNext(List<C2COrder> orderList) {
                                mView.hideLoadDialog();
                                super.onNext(orderList);
                                C2COrder order = null;
                                if (orderList != null && orderList.size() > 0) {
                                    order = orderList.get(0);
                                }
                                mView.bindC2COrderDetail(order);
                            }
                        }));
    }
}
