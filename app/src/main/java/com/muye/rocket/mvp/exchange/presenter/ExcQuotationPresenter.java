package com.muye.rocket.mvp.exchange.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ExcApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.cache.ExcCache;
import com.muye.rocket.entity.exchange.ExcTradingArea;
import com.muye.rocket.entity.exchange.ExcTradingPair;
import com.muye.rocket.mvp.exchange.contract.ExcQuotationContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExcQuotationPresenter extends BasePresenter<ExcQuotationContract.View> implements ExcQuotationContract.Presenter {
    boolean isFirst;
    List<String> tradingAreaNameList;
    List<String> usdtAreaExcTradingPair;

    public ExcQuotationPresenter(ExcQuotationContract.View view) {
        super(view);
        isFirst = true;
        tradingAreaNameList = new ArrayList<>();
        usdtAreaExcTradingPair = new ArrayList<>();
    }

    /**
     * 获取交易区
     * /v1/market/area.html
     */
    @Override
    public void fetchTradingArea() {
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchTradingArea()//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<ExcTradingArea>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.bindTradingArea(new ArrayList<ExcTradingArea>());
                            }

                            @Override
                            public void onNext(List<ExcTradingArea> tradingAreaList) {
                                super.onNext(tradingAreaList);
                                mView.bindTradingArea(tradingAreaList);
                            }
                        }));
    }

    /**
     * 获取交易对列表
     * /v1/market/list.html
     *
     * @param tradingAreaCode
     * @param tradingAreaName
     */
    @Override
    public void fetchTradingPairList(String tradingAreaCode, String tradingAreaName) {
        boolean delay = !tradingAreaNameList.contains(tradingAreaCode);
        if (!tradingAreaNameList.contains(tradingAreaCode)) {
            tradingAreaNameList.add(tradingAreaCode);
        }

        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchTradingPairList(tradingAreaCode)//方法
                        .delay(delay ? 0 : 5, TimeUnit.SECONDS)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<ExcTradingPair>>() {
                            @Override
                            public void onError(ApiException exception) {
                                Log.e("测试接口", "onError: " + tradingAreaName);
                                //请求失败显示数据
                                String list = "[{\"blockName\":\"主板\",\"buyCoinId\":\"3\",\"buyFee\":\"0.003\",\"buyName\":\"USDT\",\"buyShortName\":\"USDT\",\"buySymbol\":\"0\",\"id\":\"1\",\"sellAppLogo\":\"https://rocket-static.oss-ap-southeast-1.aliyuncs.com/rocketcoin/upload/coin/db2b82acbd9b4d24af72b53315081a46bitcoin.png\",\"sellCoinId\":\"1\",\"sellFee\":\"0.003\",\"sellName\":\"BTC\",\"sellShortName\":\"BTC\",\"sellSymbol\":\"0\",\"tradeBlock\":\"1\",\"typeName\":\"对USDT交易区\"},{\"blockName\":\"主板\",\"buyCoinId\":\"3\",\"buyFee\":\"0.003\",\"buyName\":\"USDT\",\"buyShortName\":\"USDT\",\"buySymbol\":\"0\",\"id\":\"2\",\"sellAppLogo\":\"https://rocket-static.oss-ap-southeast-1.aliyuncs.com/rocketcoin/upload/coin/42698d52ad724e43a42ffdded5d40a37eth.png\",\"sellCoinId\":\"2\",\"sellFee\":\"0.003\",\"sellName\":\"ETH\",\"sellShortName\":\"ETH\",\"sellSymbol\":\"0\",\"tradeBlock\":\"1\",\"typeName\":\"对USDT交易区\"},{\"blockName\":\"主板\",\"buyCoinId\":\"3\",\"buyFee\":\"0.004\",\"buyName\":\"USDT\",\"buyShortName\":\"USDT\",\"buySymbol\":\"0\",\"id\":\"46\",\"sellAppLogo\":\"https://rocket-static.oss-ap-southeast-1.aliyuncs.com/rocketcoin/upload/coin/d6b227f5814a47b1abaa94cc6b89addfcat.png\",\"sellCoinId\":\"21\",\"sellFee\":\"0.004\",\"sellName\":\"CAT\",\"sellShortName\":\"CAT\",\"sellSymbol\":\"\",\"tradeBlock\":\"1\",\"typeName\":\"对USDT交易区\"}]";
                                List<ExcTradingPair> tempTradingPairList = new Gson().fromJson(list, new TypeToken<List<ExcTradingPair>>() {
                                }.getType());
                                String info = "[{\"symbol\":\"0\",\"chg\":0.42,\"p_new\":7404.68,\"buy\":7371.86,\"sell\":7416.61,\"sellSymbol\":\"BTC\",\"buySymbol\":\"USDT\",\"total\":10224.0818,\"high\":7416.61,\"p_open\":7373.57,\"low\":7371.86,\"block\":1,\"tradeId\":1},{\"symbol\":\"0\",\"chg\":0.01,\"p_new\":147.21,\"buy\":146.85,\"sell\":147.5,\"sellSymbol\":\"ETH\",\"buySymbol\":\"USDT\",\"total\":56605.4503,\"high\":147.5,\"p_open\":147.19,\"low\":146.85,\"block\":1,\"tradeId\":2},{\"symbol\":\"0\",\"chg\":13.16,\"p_new\":36.3550,\"buy\":28.134,\"sell\":36.3550,\"sellSymbol\":\"CAT\",\"buySymbol\":\"USDT\",\"total\":12156.0889,\"high\":36.3550,\"p_open\":36.3550,\"low\":18.5,\"block\":1,\"tradeId\":46}]";
                                List<ExcTradingPair> tempTradingPairInfo = new Gson().fromJson(info, new TypeToken<List<ExcTradingPair>>() {
                                }.getType());
                                mView.bindTradingPair(0, "USDT", buildTradingPair(tempTradingPairList, tempTradingPairInfo));
                            }

                            @Override
                            public void onNext(List<ExcTradingPair> tradingPairList) {
                                super.onNext(tradingPairList);
                                Log.e("测试接口", "onNext: " + tradingAreaName);
                                if (tradingAreaName.equals("USDT")) {
                                    String json = new Gson().toJson(tradingPairList);
                                    Log.e("测试", "onNext: " + json);

                                    String data = "[{\"symbol\":\"0\",\"chg\":0.42,\"p_new\":7404.68,\"buy\":7371.86,\"sell\":7416.61,\"sellSymbol\":\"BTC\",\"buySymbol\":\"USDT\",\"total\":10224.0818,\"high\":7416.61,\"p_open\":7373.57,\"low\":7371.86,\"block\":1,\"tradeId\":1},{\"symbol\":\"0\",\"chg\":0.01,\"p_new\":147.21,\"buy\":146.85,\"sell\":147.5,\"sellSymbol\":\"ETH\",\"buySymbol\":\"USDT\",\"total\":56605.4503,\"high\":147.5,\"p_open\":147.19,\"low\":146.85,\"block\":1,\"tradeId\":2},{\"symbol\":\"0\",\"chg\":13.16,\"p_new\":36.3550,\"buy\":28.134,\"sell\":36.3550,\"sellSymbol\":\"CAT\",\"buySymbol\":\"USDT\",\"total\":12156.0889,\"high\":36.3550,\"p_open\":36.3550,\"low\":18.5,\"block\":1,\"tradeId\":46}]";
                                    List<ExcTradingPair> tempTradingPairInfo = new Gson().fromJson(data, new TypeToken<List<ExcTradingPair>>() {
                                    }.getType());

                                    mView.bindTradingPair(0, "USDT", buildTradingPair(tradingPairList, tempTradingPairInfo));
                                }


                                //tag = 0表示接口1返回数据，1表示接口2（socket）返回
//                                mView.bindTradingPair(0, tradingAreaName, tradingPairList);

                                /*
                                 * 注释接口，走长连接
                                 * */
                                if (MMKVTools.isOpenWs() == 0) {
                                    // HomeFragment 下面的币种区域 列表数据
                                    fetchTradingPairInfo(tradingPairList, tradingAreaName);
                                }
                            }
                        }));
    }

    /**
     * /real/markets.html"
     * 获取交易对信息
     *
     * @param tradingPairList
     * @param tradingAreaName
     */
    private void fetchTradingPairInfo(List<ExcTradingPair> tradingPairList, String tradingAreaName) {
        String ids = buildTradingPairIds(tradingPairList);
        if (TextUtils.isEmpty(ids)) {
            mView.bindTradingPair(0, tradingAreaName, tradingPairList);
            return;
        }
        mCompositeDisposable.add(
                mRetrofit.create(ExcApiService.class)
                        .fetchTradingPairInfo(ids)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<ExcTradingPair>>() {
                            @Override
                            public void onError(ApiException exception) {
                                String json = "[{\"block\":\"1\",\"buy\":\"18.5\",\"buySymbol\":\"USDT\",\"chg\":\"15.62\",\"high\":\"18.5\",\"low\":\"0.0728\",\"p_new\":\"18.5\",\"p_open\":\"16\",\"sell\":\"0\",\"sellSymbol\":\"CAT\",\"symbol\":\"0\",\"total\":\"30882.6441\",\"tradeId\":\"46\"}]";
                                Gson gson = new Gson();
                                List<ExcTradingPair> tradingPairInfo = gson.fromJson(json, new TypeToken<List<ExcTradingPair>>() {
                                }.getType());
//                                mView.bindTradingPair(tradingAreaName, tradingPairList);
                                mView.bindTradingPair(1, tradingAreaName, buildTradingPair(tradingPairList, tradingPairInfo));
                            }

                            @Override
                            public void onNext(List<ExcTradingPair> tradingPairInfo) {
                                super.onNext(tradingPairInfo);
//                                SPUtils.setTradingPair(RApplication.getContext(), "tradingpair",tradingPairInfo);
                                mView.bindTradingPair(1, tradingAreaName, buildTradingPair(tradingPairList, tradingPairInfo));
                            }
                        }));
    }

    private List<ExcTradingPair> buildTradingPair(List<ExcTradingPair> tradingPairList, List<ExcTradingPair> tradingPairInfoList) {
        if (tradingPairList != null && tradingPairList.size() > 0 && tradingPairInfoList != null && tradingPairInfoList.size() > 0) {
            for (ExcTradingPair tradingPair : tradingPairList) {
                if (tradingPair != null && !TextUtils.isEmpty(tradingPair.getSellShortName()) && !TextUtils.isEmpty(tradingPair.getBuyShortName())) {
                    for (ExcTradingPair tradingPairInfo : tradingPairInfoList) {
                        if (tradingPairInfo != null && tradingPair.getSellShortName().equals(tradingPairInfo.getSellSymbol())
                                && tradingPair.getBuyShortName().equals(tradingPairInfo.getBuySymbol())) {
                            tradingPair.setChg(tradingPairInfo.getChg());
                            tradingPair.setTotal(tradingPairInfo.getTotal());
                            tradingPair.setP_new(tradingPairInfo.getP_new());
                            tradingPair.setBuy(tradingPairInfo.getBuy());
                            tradingPair.setSell(tradingPairInfo.getSell());
                            tradingPair.setSellSymbol(tradingPairInfo.getSellSymbol());
                            tradingPair.setBuySymbol(tradingPairInfo.getBuySymbol());
                            tradingPair.setHigh(tradingPairInfo.getHigh());
                            tradingPair.setP_open(tradingPairInfo.getP_open());
                            tradingPair.setLow(tradingPairInfo.getLow());
                            tradingPair.setBlock(tradingPairInfo.getBlock());
                            tradingPair.setTradeId(tradingPairInfo.getTradeId());
                            saveRMBPriceCache(tradingPair);
                            break;
                        }
                    }
                }
            }
        }
        return tradingPairList;
    }

    /**
     * 拼接交易对ID
     *
     * @param tradingPairList
     * @return
     */
    private String buildTradingPairIds(List<ExcTradingPair> tradingPairList) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (tradingPairList != null && tradingPairList.size() > 0) {
            int size = tradingPairList.size();
            for (int i = 0; i < size; i++) {
                ExcTradingPair tradingPair = tradingPairList.get(i);
                if (tradingPair != null && !TextUtils.isEmpty(tradingPair.getId())) {
                    stringBuilder.append(String.valueOf(tradingPair.getId()));
                    if (i != size - 1) {
                        stringBuilder.append(",");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    private void saveRMBPriceCache(ExcTradingPair tradingPair) {
        if (tradingPair != null && !TextUtils.isEmpty(tradingPair.getBuyShortName()) && !TextUtils.isEmpty(tradingPair.getSellShortName())) {
            if ("USDT".equals(tradingPair.getBuyShortName())) {
                if (!usdtAreaExcTradingPair.contains(tradingPair.getSellShortName())) {
                    usdtAreaExcTradingPair.add(tradingPair.getSellShortName());
                }
                double price = NumberUtil.string2Double(tradingPair.getP_new()) * ExcCache.getUsdt2RmbCache();
                ExcCache.saveRMBPriceCache(tradingPair.getSellShortName(), price);
            } else {
                if (!usdtAreaExcTradingPair.contains(tradingPair.getSellShortName()) && usdtAreaExcTradingPair.contains(tradingPair.getBuyShortName())) {
                    double price = ExcCache.getRMBPriceCache(tradingPair.getBuyShortName()) * NumberUtil.string2Double(tradingPair.getP_new());
                    ExcCache.saveRMBPriceCache(tradingPair.getSellShortName(), price);
                }
            }
        }
    }
}
