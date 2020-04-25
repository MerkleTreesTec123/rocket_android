package com.muye.rocket.entity.exc_wallet;

public class DealOperaEntity {

    private int sell;
    private int buy;
    private String sell_msg;
    private String buy_msg;

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public String getSell_msg() {
        return sell_msg;
    }

    public void setSell_msg(String sell_msg) {
        this.sell_msg = sell_msg;
    }

    public String getBuy_msg() {
        return buy_msg;
    }

    public void setBuy_msg(String buy_msg) {
        this.buy_msg = buy_msg;
    }

    @Override
    public String toString() {
        return "DealOperaEntity{" +
                "sell=" + sell +
                ", buy=" + buy +
                ", sell_msg='" + sell_msg + '\'' +
                ", buy_msg='" + buy_msg + '\'' +
                '}';
    }
}
