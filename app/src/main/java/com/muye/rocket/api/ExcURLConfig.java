package com.muye.rocket.api;


public class ExcURLConfig {

    //交易区
    public static final String URL_EXC_TRADING_AREA = "/v1/market/area.html";
    //交易对列表
    public static final String URL_EXC_TRADING_PAIR_LIST = "/v1/market/list.html";
    //交易对信息
    public static final String URL_EXC_TRADING_PAIR_INFO = "/real/markets.html";
    //交易对信息
    public static final String URL_EXC_TRADING_PAIR_DETAIL = "/real/market.html";
    //USDT兑RMB的汇率
    public static final String URL_EXC_USDT_2_RMB_RATE = "/market/rate.html";
    //K线数据
    public static final String URL_EXC_KLINE = "/kline/fullperiod.html";
    //订单列表
    public static final String URL_EXC_ORDER_LIST = "/v1/entrust/list.html";
    //交易对余额
    public static final String URL_EXC_TRADING_PAIR_BALANCE = "/v1/market/userassets.html";
    //下单
    public static final String URL_EXC_SUBMIT_EXCHANGE = "/v1/entrust/place.html";
    //取消订单
    public static final String URL_CANCEL_ORDER = "/v1/entrust/cancel.html";
    //币种简介
    public static final String URL_EXC_COIN_INTRODUCTION = "/index.php?s=javaapi&c=tradeApi&m=coinInfo";
    //是否可以买卖
    public static final String URL_CAN_DEAL = "index.php?s=javaapi&c=Api&m=check_pay";
}
