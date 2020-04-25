package com.muye.rocket.mvp.exchange.presenter;

import android.text.TextUtils;

import com.ifenduo.lib_base.api.BaseURLConfig;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ApiSignTools;
import com.muye.rocket.api.ExcApiService;
import com.muye.rocket.api.ExcURLConfig;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.exc_wallet.DealOperaEntity;
import com.muye.rocket.entity.exchange.ExcTradingPairBalance;
import com.muye.rocket.mvp.exchange.contract.ExcBalanceContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExcBalancePresenter extends BasePresenter<ExcBalanceContract.View> implements ExcBalanceContract.Presenter {
    public ExcBalancePresenter(ExcBalanceContract.View view) {
        super(view);
    }

    /**
     * 交易对余额，可用余额
     * /v1/market/userassets.html
     * 刚进入, 直接获取数据，下次每隔4 秒获取一次数据
     * @param tradingPairID
     * @param isDelay
     */
    @Override
    public void fetchBalance(String tradingPairID, boolean isDelay) {
        if (TextUtils.isEmpty(tradingPairID)) return;
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("tradeid", tradingPairID);
        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST",
                BaseURLConfig.HOST, ExcURLConfig.URL_EXC_TRADING_PAIR_BALANCE, params);
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchTradingPairBalance(params)//方法
                        .delay(isDelay ? 4 : 0, TimeUnit.SECONDS)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcTradingPairBalance>() {
                            @Override
                            public void onError(ApiException exception) {
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindBalance(tradingPairID, null);
                            }

                            @Override
                            public void onNext(ExcTradingPairBalance balance) {
                                super.onNext(balance);
                                mView.bindBalance(tradingPairID, balance);
                            }
                        }));
    }

    /**
     * 是否可以买卖接口  fetchDealOperat()
     */
    @Override
    public void fetchDealRealt() {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                .fetchDealOperat(MMKVTools.getUID(),MMKVTools.getToken())
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribeWith(new CustomResourceSubscriber<DealOperaEntity>(){
                    @Override
                    public void onError(ApiException exception) {
                        if (mView != null)
                            mView.hideLoadDialog();
//                        mView.onError(exception.getCode(), exception.getDisplayMessage());
                    }

                    @Override
                    public void onNext(DealOperaEntity dealOperaEntity) {
                        if (mView != null)
                            mView.hideLoadDialog();
                        super.onNext(dealOperaEntity);
                        mView.isDeal(dealOperaEntity);
                    }
                }));
    }
}
