package com.muye.rocket.mvp.exchange.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ifenduo.lib_base.api.BaseURLConfig;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ApiSignTools;
import com.muye.rocket.api.ExcApiService;
import com.muye.rocket.api.ExcURLConfig;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.exchange.ExcOrderEntity;
import com.muye.rocket.mvp.exchange.contract.ExcOrderContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExcOrderPresenter extends BasePresenter<ExcOrderContract.View> implements ExcOrderContract.Presenter {
    public ExcOrderPresenter(ExcOrderContract.View view) {
        super(view);
    }

    /**
     * 查询买入 、 卖出 界面下面的订单信息
     *  symbol  交易对，CAT_USDT  ， BTC_USDT 。。
     *  type   0 : 全部 ， 1：当前 ， 2 : 历史
     *  刚进入 直接进行请求，然后 每隔4秒请求一次
     * @param sellName
     * @param buyName
     * @param type
     * @param isDelay
     */
    @Override
    public void fetchOrder(String sellName, String buyName, String type, boolean isDelay) {
        if (TextUtils.isEmpty(sellName) || TextUtils.isEmpty(buyName) || TextUtils.isEmpty(type))
            return;
        Map<String, String> map = new HashMap<>();
        map.put("token", MMKVTools.getToken());
        map.put("symbol", sellName.toLowerCase() + "_" + buyName.toLowerCase());
        map.put("type", type);
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchExcOrder(ApiSignTools.convertQueryMap(map))//方法
                        .delay(isDelay ? 4 : 0, TimeUnit.SECONDS)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcOrderEntity>() {
                            @Override
                            public void onError(ApiException exception) {
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindOrder(sellName, buyName, null, type);
                            }

                            @Override
                            public void onNext(ExcOrderEntity orderEntity) {
                                super.onNext(orderEntity);
                                mView.bindOrder(sellName, buyName, orderEntity, type);
                            }
                        }));
    }

    @Override
    public void cancelOrder(String orderID) {
        if (TextUtils.isEmpty(orderID)) return;
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("id", orderID);
        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST",
                BaseURLConfig.HOST, ExcURLConfig.URL_CANCEL_ORDER, params);
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .cancelOrder(params)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                super.onNext(entity);
                                mView.hideLoadDialog();
                                mView.onCancelOrderSuccess();
                            }
                        }));
    }
}
