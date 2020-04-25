package com.muye.rocket.entity.exchange;

import android.text.TextUtils;

import java.math.BigDecimal;

public class ExcTradingPairBalance {


    private String score;
    private BuyCoinBean buyCoin;
    private SellCoinBean sellCoin;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public BuyCoinBean getBuyCoin() {
        return buyCoin;
    }

    public void setBuyCoin(BuyCoinBean buyCoin) {
        this.buyCoin = buyCoin;
    }

    public SellCoinBean getSellCoin() {
        return sellCoin;
    }

    public void setSellCoin(SellCoinBean sellCoin) {
        this.sellCoin = sellCoin;
    }

    public static class BuyCoinBean {
        private String total;
        private String frozen;
        private String borrow;
        private String id;

        public double getTotal() {
            if (TextUtils.isEmpty(total)) return 0;
            return new BigDecimal(total).doubleValue();
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getFrozen() {
            return frozen;
        }

        public void setFrozen(String frozen) {
            this.frozen = frozen;
        }

        public String getBorrow() {
            return borrow;
        }

        public void setBorrow(String borrow) {
            this.borrow = borrow;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class SellCoinBean {
        private String total;
        private String frozen;
        private String borrow;
        private String id;

        public double getTotal() {
            if (TextUtils.isEmpty(total)) return 0;
            return new BigDecimal(total).doubleValue();
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getFrozen() {
            return frozen;
        }

        public void setFrozen(String frozen) {
            this.frozen = frozen;
        }

        public String getBorrow() {
            return borrow;
        }

        public void setBorrow(String borrow) {
            this.borrow = borrow;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
