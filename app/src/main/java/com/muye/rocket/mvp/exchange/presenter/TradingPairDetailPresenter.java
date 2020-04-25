package com.muye.rocket.mvp.exchange.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ExcApiService;
import com.muye.rocket.cache.KLineCache;
import com.muye.rocket.entity.exchange.ExcCoinIntroduction;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;
import com.muye.rocket.entity.exchange.KLineEntity;
import com.muye.rocket.mvp.exchange.contract.TradingPairDetailContract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TradingPairDetailPresenter extends BasePresenter<TradingPairDetailContract.View> implements TradingPairDetailContract.Presenter {
    private static final String TAG = "PairDetailPresenter";

    public TradingPairDetailPresenter(TradingPairDetailContract.View view) {
        super(view);
    }

    /**
     * 获取K线图数据
     * 刚进入 延迟5秒开始请求，一次成功之后，每隔 4 秒请求一次
     * @param tradingPairID
     * @param time
     * @param isDelay
     */
    @Override
    public void fetchKLineData(String tradingPairID, @KLineCache.KLINE_TIME String time, boolean isDelay) {
        if (TextUtils.isEmpty(time) || TextUtils.isEmpty(tradingPairID)) return;
        Log.d(TAG, "fetchKLineData: time=" + time + " isDelay=" + isDelay);
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchKLineData(tradingPairID, time)//方法
                        .delay(isDelay ? 5 : 0, TimeUnit.SECONDS)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<List<BigDecimal>>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.bindKLineData(tradingPairID, time, new ArrayList<>());
                            }

                            @Override
                            public void onNext(List<List<BigDecimal>> data) {
                                super.onNext(data);
                                List<KLineEntity> kLineEntityList = new ArrayList<>();
                                if (data != null && data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        List<BigDecimal> bigDecimalList = data.get(i);
                                        if (bigDecimalList != null && bigDecimalList.size() == 6) {
                                            long date = bigDecimalList.get(0) != null ? bigDecimalList.get(0).longValue() : 0;
                                            float open = bigDecimalList.get(1) != null ? bigDecimalList.get(1).floatValue() : 0;
                                            float high = bigDecimalList.get(2) != null ? bigDecimalList.get(2).floatValue() : 0;
                                            float low = bigDecimalList.get(3) != null ? bigDecimalList.get(3).floatValue() : 0;
                                            float close = bigDecimalList.get(4) != null ? bigDecimalList.get(4).floatValue() : 0;
                                            float vol = bigDecimalList.get(5) != null ? bigDecimalList.get(5).floatValue() : 0;
                                            kLineEntityList.add(new KLineEntity(date, open, high, low, close, vol));
                                        }
                                    }
                                }
                                mView.bindKLineData(tradingPairID, time, kLineEntityList);
                            }
                        }));
    }

    /**
     * /real/market.html
     * 获取交易对详情
     *  第一次请求0 秒开始获取数据，请求成功之后再次调用每隔2秒请求一次
     * @param tradingPairID
     * @param isDelay
     */
    @Override
    public void fetchTradingPairDetail(String tradingPairID, boolean isDelay) {
        if (TextUtils.isEmpty(tradingPairID)) return;
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchTradingPairDetail(tradingPairID, 100)//方法
                        .delay(isDelay ? 2 : 0, TimeUnit.SECONDS)
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
                                mView.bindTradingPairDetail(tradingPairID, tradingPairDetail);
                            }
                        }));
    }

    /**
     * /index.php?s=javaapi&c=tradeApi&m=coinInfo
     * 币种简介
     * 只获取一次
     * @param coinID
     */
    @Override
    public void fetchCoinIntroduction(String coinID) {
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchCoinIntroduction(coinID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcCoinIntroduction>() {
                            @Override
                            public void onError(ApiException exception) {
//                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindCoinIntroduction(null);
                            }

                            @Override
                            public void onNext(ExcCoinIntroduction introduction) {
                                super.onNext(introduction);
                                mView.bindCoinIntroduction(introduction);
                            }
                        }));
    }

}
