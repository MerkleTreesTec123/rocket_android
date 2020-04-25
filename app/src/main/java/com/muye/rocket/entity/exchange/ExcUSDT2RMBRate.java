package com.muye.rocket.entity.exchange;

import com.ifenduo.lib_base.tools.NumberUtil;

public class ExcUSDT2RMBRate {
    private String CNY;

    public double getCNY() {
        return NumberUtil.string2Double(CNY);
    }

    public void setCNY(String CNY) {
        this.CNY = CNY;
    }
}
