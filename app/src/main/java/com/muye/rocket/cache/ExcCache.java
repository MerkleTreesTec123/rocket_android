package com.muye.rocket.cache;

import android.text.TextUtils;


import com.muye.rocket.entity.exchange.ExcTradingPair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcCache {
    private static LinkedHashMap<String, Double> COIN_RMB_PRICE_CACHE = new LinkedHashMap<>();
    private static LinkedHashMap<String, List<ExcTradingPair>> TRADING_PAIR_CACHE = new LinkedHashMap<>();
    private static double USDT_2_RMB_CACHE = 6.8;

    public static double getUsdt2RmbCache() {
        return USDT_2_RMB_CACHE;
    }

    public static void saveUsdt2RmbCache(double usdt2Rmb) {
        USDT_2_RMB_CACHE = usdt2Rmb;
    }

    public static double getRMBPriceCache(String coinName) {
        if (TextUtils.isEmpty(coinName)) return 0;
        if (COIN_RMB_PRICE_CACHE.containsKey(coinName)) {
            return COIN_RMB_PRICE_CACHE.get(coinName);
        } else if ("USDT".equals(coinName)) {
            return USDT_2_RMB_CACHE;
        } else {
            return 0;
        }
    }

    public static boolean saveRMBPriceCache(String coinName, double rmbPrice) {
        if (TextUtils.isEmpty(coinName)) return false;
        COIN_RMB_PRICE_CACHE.put(coinName, rmbPrice);
        return true;
    }

    public static boolean containsRMBPriceCache(String coinName) {
        return COIN_RMB_PRICE_CACHE.containsKey(coinName);
    }

    public static void saveTradingPair(String tradingAreaName, List<ExcTradingPair> tradingPairList) {
        if (!TextUtils.isEmpty(tradingAreaName)) {
            List<ExcTradingPair> pairList = new ArrayList<>();
            if (tradingPairList != null && tradingPairList.size() > 0) {
                for (int i = 0; i < tradingPairList.size(); i++) {
                    ExcTradingPair tradingPair = tradingPairList.get(i);
                    if (tradingPair != null) {
                        ExcTradingPair pair = new ExcTradingPair();
                        pair.setTradeId(tradingPair.getTradeId());
                        pair.setBlock(tradingPair.getBlock());
                        pair.setLow(tradingPair.getLow());
                        pair.setP_open(tradingPair.getP_open());
                        pair.setHigh(tradingPair.getHigh());
                        pair.setBuySymbol(tradingPair.getBuySymbol());
                        pair.setSellSymbol(tradingPair.getSellSymbol());
                        pair.setP_new(tradingPair.getP_new());
                        pair.setTotal(tradingPair.getTotal());
                        pair.setChg(tradingPair.getChg());
                        pair.setBlockName(tradingPair.getBlockName());
                        pair.setBuyAppLogo(tradingPair.getBuyAppLogo());
                        pair.setBuyCoinId(tradingPair.getBuyCoinId());
                        pair.setBuyFee(tradingPair.getBuyFee());
                        pair.setBuyName(tradingPair.getBuyName());
                        pair.setBuyShortName(tradingPair.getBuyShortName());
                        pair.setId(tradingPair.getId());
                        pair.setSellAppLogo(tradingPair.getSellAppLogo());
                        pair.setSellCoinId(tradingPair.getSellCoinId());
                        pair.setSellFee(tradingPair.getSellFee());
                        pair.setSellName(tradingPair.getSellName());
                        pair.setSellShortName(tradingPair.getSellShortName());
                        pair.setSymbol(tradingPair.getSymbol());
                        pair.setTradeBlock(tradingPair.getTradeBlock());
                        pair.setTypeName(tradingPair.getTypeName());
                        pair.setBuy(tradingPair.getBuy());
                        pair.setSell(tradingPair.getSell());
                        pairList.add(pair);
                    }
                }
            }
            TRADING_PAIR_CACHE.put(tradingAreaName, pairList);
        }
    }

    public static List<ExcTradingPair> getTradingPairCache(String tradingAreaName) {
        if (hasTradingArea(tradingAreaName)) {
            return TRADING_PAIR_CACHE.get(tradingAreaName);
        } else {
            return new ArrayList<>();
        }
    }

    public static boolean hasTradingArea(String tradingAreaName) {
        return TRADING_PAIR_CACHE.containsKey(tradingAreaName);
    }

    public static Map<String, List<ExcTradingPair>> getTradingAreaCache() {
        Map<String, List<ExcTradingPair>> data = new LinkedHashMap<>();
        for (Map.Entry<String, List<ExcTradingPair>> entry : TRADING_PAIR_CACHE.entrySet()) {
            String key = entry.getKey();
            if (!TextUtils.isEmpty(key)) {
                List<ExcTradingPair> pairList = new ArrayList<>();
                List<ExcTradingPair> cacheList = entry.getValue();
                if (cacheList != null && cacheList.size() > 0) {
                    for (int i = 0; i < cacheList.size(); i++) {
                        ExcTradingPair tradingPair = cacheList.get(i);
                        if (tradingPair != null) {
                            ExcTradingPair pair = new ExcTradingPair();
                            pair.setTradeId(tradingPair.getTradeId());
                            pair.setBlock(tradingPair.getBlock());
                            pair.setLow(tradingPair.getLow());
                            pair.setP_open(tradingPair.getP_open());
                            pair.setHigh(tradingPair.getHigh());
                            pair.setBuySymbol(tradingPair.getBuySymbol());
                            pair.setSellSymbol(tradingPair.getSellSymbol());
                            pair.setP_new(tradingPair.getP_new());
                            pair.setTotal(tradingPair.getTotal());
                            pair.setChg(tradingPair.getChg());
                            pair.setBlockName(tradingPair.getBlockName());
                            pair.setBuyAppLogo(tradingPair.getBuyAppLogo());
                            pair.setBuyCoinId(tradingPair.getBuyCoinId());
                            pair.setBuyFee(tradingPair.getBuyFee());
                            pair.setBuyName(tradingPair.getBuyName());
                            pair.setBuyShortName(tradingPair.getBuyShortName());
                            pair.setId(tradingPair.getId());
                            pair.setSellAppLogo(tradingPair.getSellAppLogo());
                            pair.setSellCoinId(tradingPair.getSellCoinId());
                            pair.setSellFee(tradingPair.getSellFee());
                            pair.setSellName(tradingPair.getSellName());
                            pair.setSellShortName(tradingPair.getSellShortName());
                            pair.setSymbol(tradingPair.getSymbol());
                            pair.setTradeBlock(tradingPair.getTradeBlock());
                            pair.setTypeName(tradingPair.getTypeName());
                            pair.setBuy(tradingPair.getBuy());
                            pair.setSell(tradingPair.getSell());
                            pairList.add(pair);
                        }
                    }
                }
                data.put(key, pairList);
            }
        }
        return data;
    }

    public static ExcTradingPair getTradingPairByCoinID(String coinID) {
        ExcTradingPair tradingPair = null;
        if (TRADING_PAIR_CACHE != null && TRADING_PAIR_CACHE.size() > 0) {

            //如果coinID为空返回第一个，否则匹配sellCoinID
            for (Map.Entry<String, List<ExcTradingPair>> entry : TRADING_PAIR_CACHE.entrySet()) {
                List<ExcTradingPair> tradingPairList = entry.getValue();
                if (tradingPairList != null && tradingPairList.size() > 0) {
                    for (int i = 0; i < tradingPairList.size(); i++) {
                        ExcTradingPair tradingPair_ = tradingPairList.get(i);
                        if (tradingPair_ != null) {
                            if (TextUtils.isEmpty(coinID)) {
                                return tradingPair_;
                            } else {
                                if (coinID.equals(tradingPair_.getSellCoinId())) {
                                    return tradingPair_;
                                }
                            }
                        }
                    }
                }
            }

            //根据sellCoinID没匹配到
            //如果coinID为空返回第一个，否则匹配buyCoinID
            for (Map.Entry<String, List<ExcTradingPair>> entry : TRADING_PAIR_CACHE.entrySet()) {
                List<ExcTradingPair> tradingPairList = entry.getValue();
                if (tradingPairList != null && tradingPairList.size() > 0) {
                    for (int i = 0; i < tradingPairList.size(); i++) {
                        ExcTradingPair tradingPair_ = tradingPairList.get(i);
                        if (tradingPair_ != null) {
                            if (TextUtils.isEmpty(coinID)) {
                                return tradingPair_;
                            } else {
                                if (coinID.equals(tradingPair_.getBuyCoinId())) {
                                    return tradingPair_;
                                }
                            }
                        }
                    }
                }
            }

        }
        return null;
    }
}
