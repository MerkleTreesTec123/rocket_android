package com.muye.rocket.entity.exchange;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易对详情
 */
public class WSExcTradingPairDetail implements Serializable {
    private String symbol;
    private String chg;
    private String p_new;
    private String buy;
    private String sell;
    private String sellSymbol;
    private String total;
    private String high;
    private String buySymbol;
    private DepthBean depth;
    private String p_open;
    private String low;
    private String block;
    private String tradeId;
    private List<TradesBean> trades;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getChg() {
        if (TextUtils.isEmpty(chg)) return "0";
        return chg;
    }

    public void setChg(String chg) {
        this.chg = chg;
    }

    public String getP_new() {
        if (TextUtils.isEmpty(p_new)) return "0";
        return p_new;
    }

    public void setP_new(String p_new) {
        this.p_new = p_new;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSellSymbol() {
        return sellSymbol;
    }

    public void setSellSymbol(String sellSymbol) {
        this.sellSymbol = sellSymbol;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getBuySymbol() {
        return buySymbol;
    }

    public void setBuySymbol(String buySymbol) {
        this.buySymbol = buySymbol;
    }

    public DepthBean getDepth() {
        return depth;
    }

    public void setDepth(DepthBean depth) {
        this.depth = depth;
    }

    public String getP_open() {
        return p_open;
    }

    public void setP_open(String p_open) {
        this.p_open = p_open;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public List<TradesBean> getTrades() {
        return trades;
    }

    public void setTrades(List<TradesBean> trades) {
        this.trades = trades;
    }

    public static class DepthBean  implements Serializable {

        private long date;
        private String asks;
        private String bids;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getAsks() {
            return asks;
        }

        public void setAsks(String asks) {
            this.asks = asks;
        }

        public String getBids() {
            return bids;
        }

        public void setBids(String bids) {
            this.bids = bids;
        }
    }

    public List<DepthItemEntity> buildDepthItemEntity(List<List<Double>> data) {
        List<DepthItemEntity> depthItemEntityList = new ArrayList<>();
        if (data != null && data.size() > 0) {
            double sum = 0;
            for (int i = 0; i < 5; i++) {
                if (i < data.size()) {
                    List<Double> doubleList = data.get(i);
                    if (doubleList != null && doubleList.size() == 2) {
                        sum += doubleList.get(1);
                    }
                } else {
                    break;
                }
            }

            double num = 0;
            for (int i = 0; i < 5; i++) {
                if (i < data.size()) {
                    List<Double> doubleList = data.get(i);
                    if (doubleList != null && doubleList.size() == 2) {
                        num += doubleList.get(1);
                        DepthItemEntity entity = new DepthItemEntity(doubleList.get(1), doubleList.get(0), sum == 0 ? 0 : (float) (num / sum));
                        depthItemEntityList.add(entity);
                    }
                } else {
                    break;
                }
            }
        }
        return depthItemEntityList;
    }

    public static class DepthItemEntity {
        private double num;
        private double price;
        private float progress;

        public DepthItemEntity(double num, double price, float progress) {
            this.num = num;
            this.price = price;
            this.progress = progress;
        }

        public double getNum() {
            return num;
        }

        public void setNum(double num) {
            this.num = num;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public float getProgress() {
            return progress;
        }

        public void setProgress(float progress) {
            this.progress = progress;
        }
    }
}
