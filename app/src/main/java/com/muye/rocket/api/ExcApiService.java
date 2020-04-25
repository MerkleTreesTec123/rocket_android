package com.muye.rocket.api;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_http.response.Response;
import com.muye.rocket.entity.exc_wallet.DealOperaEntity;
import com.muye.rocket.entity.exchange.ExcCoinIntroduction;
import com.muye.rocket.entity.exchange.ExcOrderEntity;
import com.muye.rocket.entity.exchange.ExcTradingArea;
import com.muye.rocket.entity.exchange.ExcTradingPair;
import com.muye.rocket.entity.exchange.ExcTradingPairBalance;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;
import com.muye.rocket.entity.exchange.ExcUSDT2RMBRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ExcApiService {
    /**
     * 获取交易区
     *
     * @return
     */
    @GET(ExcURLConfig.URL_EXC_TRADING_AREA)
    Observable<Response<List<ExcTradingArea>>> fetchTradingArea();

    /**
     * 获取交易对列表
     *
     * @param tradingArea 交易区code
     * @return
     */
    @GET(ExcURLConfig.URL_EXC_TRADING_PAIR_LIST)
    Observable<Response<List<ExcTradingPair>>> fetchTradingPairList(@Query("symbol") String tradingArea);


    /**
     * 获取交易对信息
     *
     * @param tradingPairID
     * @return
     */
    @GET(ExcURLConfig.URL_EXC_TRADING_PAIR_INFO)
    Observable<Response<List<ExcTradingPair>>> fetchTradingPairInfo(@Query("symbol") String tradingPairID);

    /**
     * 获取交易对详情
     *
     * @param tradingPairID
     * @return
     */
    @GET(ExcURLConfig.URL_EXC_TRADING_PAIR_DETAIL)
    Observable<Response<ExcTradingPairDetail>> fetchTradingPairDetail(@Query("symbol") String tradingPairID,
                                                                      @Query("successcount") int count);

    /**
     * USDT兑RMB汇率
     *
     * @return
     */
    @GET(ExcURLConfig.URL_EXC_USDT_2_RMB_RATE)
    Observable<Response<ExcUSDT2RMBRate>> fetchUSDT2RMBRate();

    /**
     * K线
     *
     * @param tradingPairID
     * @param time
     * @return
     */
    @GET(ExcURLConfig.URL_EXC_KLINE)
    Observable<Response<List<List<BigDecimal>>>> fetchKLineData(@Query("symbol") String tradingPairID,
                                                                @Query("step") String time);

    /**
     * 订单列表
     *
     * @param params 参数：
     *               symbol 交易对 如：btc_usdt
     *               type 0全部、1当前订单、2历史订单
     *               token
     * @return
     */
    @GET(ExcURLConfig.URL_EXC_ORDER_LIST)
    Observable<Response<ExcOrderEntity>> fetchExcOrder(@QueryMap Map<String, String> params);

    /**
     * 交易对余额
     *
     * @param params 参数：
     *               tradeid 交易对ID
     *               token
     * @return
     */
    @FormUrlEncoded
    @POST(ExcURLConfig.URL_EXC_TRADING_PAIR_BALANCE)
    Observable<Response<ExcTradingPairBalance>> fetchTradingPairBalance(@FieldMap Map<String, String> params);

    /**
     * 下单
     *
     * @param params 参数:
     *               token
     *               symbol 交易对名称
     *               tradeAmount 数量
     *               tradePrice 价格
     *               type buy 表示买，sell表示卖
     *               tradePwd 密码
     * @return
     */
    @FormUrlEncoded
    @POST(ExcURLConfig.URL_EXC_SUBMIT_EXCHANGE)
    Observable<Response<BaseEntity>> submitExchange(@FieldMap Map<String, String> params);

    /**
     * 取消订单
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(ExcURLConfig.URL_CANCEL_ORDER)
    Observable<Response<BaseEntity>> cancelOrder(@FieldMap Map<String, String> params);

    /**
     * 币种简介
     * @param coinID
     * @return
     */
    @GET(ExcURLConfig.URL_EXC_COIN_INTRODUCTION)
    Observable<Response<ExcCoinIntroduction>> fetchCoinIntroduction(@Query("coinid") String coinID);

    /**
     * 买卖是否可以操作
     */
    @FormUrlEncoded
    @POST(ExcURLConfig.URL_CAN_DEAL)
    Observable<Response<DealOperaEntity>> fetchDealOperat(@Field("uid") String uid,@Field("token") String token);
}
