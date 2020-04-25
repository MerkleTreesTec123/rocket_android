package com.muye.rocket.entity.exc_wallet;

import android.text.TextUtils;

import java.math.BigDecimal;

public class ExcWithdrawSetting {
    private String id;
    private String coinId;
    private String levelVip;
    private String withdrawMax;
    private String withdrawMin;
    private String withdrawFee;
    private String withdrawTimes;
    private String withdrawDayLimit;
    private String gmtCreate;
    private String gmtModified;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getLevelVip() {
        return levelVip;
    }

    public void setLevelVip(String levelVip) {
        this.levelVip = levelVip;
    }

    public double getWithdrawMax() {
        if (TextUtils.isEmpty(withdrawMax)) return 0;
        return new BigDecimal(withdrawMax).doubleValue();
    }

    public void setWithdrawMax(String withdrawMax) {
        this.withdrawMax = withdrawMax;
    }

    public double getWithdrawMin() {
        if (TextUtils.isEmpty(withdrawMin)) return 0;
        return new BigDecimal(withdrawMin).doubleValue();
    }

    public void setWithdrawMin(String withdrawMin) {
        this.withdrawMin = withdrawMin;
    }

    public double getWithdrawFee() {
        if (TextUtils.isEmpty(withdrawFee)) return 0;
        return new BigDecimal(withdrawFee).doubleValue();
    }

    public void setWithdrawFee(String withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public String getWithdrawTimes() {
        return withdrawTimes;
    }

    public void setWithdrawTimes(String withdrawTimes) {
        this.withdrawTimes = withdrawTimes;
    }

    public String getWithdrawDayLimit() {
        return withdrawDayLimit;
    }

    public void setWithdrawDayLimit(String withdrawDayLimit) {
        this.withdrawDayLimit = withdrawDayLimit;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
