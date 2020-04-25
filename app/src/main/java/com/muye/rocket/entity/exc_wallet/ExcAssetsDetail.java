package com.muye.rocket.entity.exc_wallet;

import android.text.TextUtils;

import java.math.BigDecimal;

public class ExcAssetsDetail {

    private String uid;
    private String coin_id;
    private String total;
    private String frozen;
    private String coin_name;
    private String app_logo;
    private String is_withdraw;
    private String is_recharge;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCoin_id() {
        return coin_id;
    }

    public void setCoin_id(String coin_id) {
        this.coin_id = coin_id;
    }

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

    public String getCoin_name() {
        return coin_name;
    }

    public void setCoin_name(String coin_name) {
        this.coin_name = coin_name;
    }

    public String getApp_logo() {
        return app_logo;
    }

    public void setApp_logo(String app_logo) {
        this.app_logo = app_logo;
    }

    public boolean getIs_withdraw() {
        return "1".equals(is_withdraw);
    }

    public void setIs_withdraw(String is_withdraw) {
        this.is_withdraw = is_withdraw;
    }

    public boolean getIs_recharge() {
        return "1".equals(is_recharge);
    }

    public void setIs_recharge(String is_recharge) {
        this.is_recharge = is_recharge;
    }
}
