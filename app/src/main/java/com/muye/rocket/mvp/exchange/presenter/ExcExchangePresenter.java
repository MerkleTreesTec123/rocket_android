package com.muye.rocket.mvp.exchange.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ifenduo.lib_base.api.BaseURLConfig;
import com.muye.rocket.Constant;
import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ApiSignTools;
import com.muye.rocket.api.ExcApiService;
import com.muye.rocket.api.ExcURLConfig;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;
import com.muye.rocket.mvp.exchange.contract.ExcExchangeContract;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExcExchangePresenter extends BasePresenter<ExcExchangeContract.View> implements ExcExchangeContract.Presenter {
    String mTradingPairID;


    public ExcExchangePresenter(ExcExchangeContract.View view) {
        super(view);
    }

    /**
     * /real/market.html
     * 买入 /  卖出 界面 右侧上下两个区域数据
     * 第一次请求 直接进行，后面进行2 秒延迟请求一次
     * @param tradingPairID
     */
    @Override
    public void fetchTradingPairDetail(String tradingPairID) {
        if (TextUtils.isEmpty(tradingPairID)) return;
        boolean isFirst = !tradingPairID.equals(mTradingPairID);
        mTradingPairID = tradingPairID;
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchTradingPairDetail(tradingPairID, 100)//方法
                        .delay(isFirst ? 0 : 2, TimeUnit.SECONDS)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcTradingPairDetail>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.bindTradingPairDetail(tradingPairID, null);
                            }

                            @Override
                            public void onNext(ExcTradingPairDetail tradingPairDetail) {
                                super.onNext(tradingPairDetail);
                                String s = new Gson().toJson(tradingPairDetail);
                                Log.e("测试", "onNext: " + s );
                                mView.bindTradingPairDetail(tradingPairID, tradingPairDetail);
                            }
                        }));
    }


//    @Override
//    public void fetchOrder(String sellName, String buyName, @ExcExchangeContract.ExcOrderType String type, boolean isDelay, boolean interval) {
//        if (TextUtils.isEmpty(sellName) || TextUtils.isEmpty(buyName) || TextUtils.isEmpty(type))
//            return;
//        Map<String, String> map = new HashMap<>();
//        map.put("token", MMKVTools.getToken());
//        map.put("symbol", sellName.toLowerCase() + "_" + buyName.toLowerCase());
//        map.put("type", type);
//        mCompositeDisposable.add(
//                mRetrofit.create(ExcApiService.class)
//                        .fetchExcOrder(ApiSignTools.convertQueryMap(map))//方法
//                        .delay(isDelay ? 4 : 0, TimeUnit.SECONDS)
//                        .compose(ResponseTransformer.handleResult())//处理返回结果
//                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
//                        .subscribeWith(new CustomResourceSubscriber<ExcOrderEntity>() {
//                            @Override
//                            public void onError(ApiException exception) {
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
//                                mView.bindOrder(sellName, buyName, null, type, interval);
//                            }
//
//                            @Override
//                            public void onNext(ExcOrderEntity orderEntity) {
//                                super.onNext(orderEntity);
//                                mView.bindOrder(sellName, buyName, orderEntity, type, interval);
//                            }
//                        }));
//    }
//
//    @Override
//    public void fetchBalance(String tradingPairID, boolean isDelay, boolean interval) {
//        if (TextUtils.isEmpty(tradingPairID)) return;
//        Map<String, String> params = new HashMap<>();
//        params.put("token", MMKVTools.getToken());
//        params.put("tradeid", tradingPairID);
//        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST",
//                BaseURLConfig.HOST, ExcURLConfig.URL_EXC_TRADING_PAIR_BALANCE, params);
//        mCompositeDisposable.add(
//                mRetrofit.create(ExcApiService.class)
//                        .fetchTradingPairBalance(params)//方法
//                        .delay(isDelay ? 4 : 0, TimeUnit.SECONDS)
//                        .compose(ResponseTransformer.handleResult())//处理返回结果
//                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
//                        .subscribeWith(new CustomResourceSubscriber<ExcTradingPairBalance>() {
//                            @Override
//                            public void onError(ApiException exception) {
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
//                                mView.bindBalance(tradingPairID, null, interval);
//                            }
//
//                            @Override
//                            public void onNext(ExcTradingPairBalance balance) {
//                                super.onNext(balance);
//                                mView.bindBalance(tradingPairID, balance, interval);
//                            }
//                        }));
//    }

    @Override
    public void submitExchange(String sellName, String buyName, String num, String price, @ExcExchangeContract.ExchangeType int type, String password) {
        if (TextUtils.isEmpty(sellName) || TextUtils.isEmpty(buyName) || TextUtils.isEmpty(num) || TextUtils.isEmpty(price) || TextUtils.isEmpty(password))
            return;
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("symbol", sellName.toLowerCase() + "_" + buyName.toLowerCase());
        params.put("tradeAmount", num);
        params.put("tradePrice", price);
        params.put("type", ExcExchangeContract.EXCHANGE_TYPE_SELL == type ? "sell" : "buy");
        params.put("tradePwd", password);
        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST", BaseURLConfig.HOST, ExcURLConfig.URL_EXC_SUBMIT_EXCHANGE, params);
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .submitExchange(params)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                if (!TextUtils.isEmpty(exception.getDisplayMessage()) && exception.getDisplayMessage().startsWith("交易密码错误")) {
                                    Constant.PAY_PASSWORD = "";
                                }
                                mView.onSubmitExchangeSuccess("", "");
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                super.onNext(entity);
                                Constant.PAY_PASSWORD = password;
                                mView.hideLoadDialog();
                                mView.onSubmitExchangeSuccess(sellName, buyName);
                            }
                        }));
    }

//    @Override
//    public void cancelOrder(String orderID) {
//        if (TextUtils.isEmpty(orderID)) return;
//        Map<String, String> params = new HashMap<>();
//        params.put("token", MMKVTools.getToken());
//        params.put("id", orderID);
//        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST",
//                BaseURLConfig.HOST, ExcURLConfig.URL_CANCEL_ORDER, params);
//        mView.showLoadDialog("");
//        mCompositeDisposable.add(
//                mRetrofit.create(ExcApiService.class)
//                        .cancelOrder(params)//方法
//                        .compose(ResponseTransformer.handleResult())//处理返回结果
//                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
//                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
//                            @Override
//                            public void onError(ApiException exception) {
//                                mView.hideLoadDialog();
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
//                            }
//
//                            @Override
//                            public void onNext(BaseEntity entity) {
//                                super.onNext(entity);
//                                mView.hideLoadDialog();
//                                mView.onCancelOrderSuccess();
//                            }
//                        }));
//    }

    @Override
    public void dropView() {
        mTradingPairID = "";
        super.dropView();
    }
}
