package com.muye.rocket.entity.exchange;

import java.io.Serializable;

/**
 * 交易区
 */
public class ExcTradingArea implements Serializable {

    /**
     * code : 1
     * name : USDT
     */

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
