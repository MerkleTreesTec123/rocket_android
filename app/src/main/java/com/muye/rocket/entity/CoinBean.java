package com.muye.rocket.entity;

public class CoinBean {


    /**
     * coinname : ETH
     * total : 0.0000000000
     * frozen : 0.0000000000
     * status : 1
     */

    private String coinname;
    private String total;
    private String frozen;
    private int status;

    public String getCoinname() {
        return coinname;
    }

    public void setCoinname(String coinname) {
        this.coinname = coinname;
    }

    public String getTotal() {
        return total;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CoinBean{" +
                "coinname='" + coinname + '\'' +
                ", total='" + total + '\'' +
                ", frozen='" + frozen + '\'' +
                ", status=" + status +
                '}';
    }
}
