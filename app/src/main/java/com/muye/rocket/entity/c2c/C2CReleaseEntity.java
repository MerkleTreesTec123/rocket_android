package com.muye.rocket.entity.c2c;

import android.text.TextUtils;

import com.ifenduo.common.tool.DateTimeTool;
import com.ifenduo.lib_base.tools.NumberUtil;

public class C2CReleaseEntity {

    private String id;
    private String uid;
    private String coin_id;
    private String coinname;
    private String price;
    private String nums;
    private String inputtime;
    private String status;
    private String pay_type;
    private String type;
    private String updatetime;
    private String min;
    private String max;
    private String totalnums;
    private String countryid;
    private String currency;
    private String username;
    private String alipay;
    private String wxpay;
    private String bank;
    private String flag;
    private String currency_symbol;
    private String country_name_cn;
    private String country_name_en;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCoinname() {
        return coinname;
    }

    public void setCoinname(String coinname) {
        this.coinname = coinname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getNums() {
        return NumberUtil.string2Double(nums);
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getInputtime() {
        long time = 0;
        if (!TextUtils.isEmpty(inputtime)) {
            time = Long.parseLong(inputtime);
        }
        return DateTimeTool.getYYYYMMDDHHMMSS(time);
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public double getMin() {
        return NumberUtil.string2Double(min);
    }

    public void setMin(String min) {
        this.min = min;
    }

    public double getMax() {
        return NumberUtil.string2Double(max);
    }

    public void setMax(String max) {
        this.max = max;
    }

    public double getTotalnums() {
        return NumberUtil.string2Double(totalnums);
    }

    public void setTotalnums(String totalnums) {
        this.totalnums = totalnums;
    }

    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getWxpay() {
        return wxpay;
    }

    public void setWxpay(String wxpay) {
        this.wxpay = wxpay;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getCountry_name_cn() {
        return country_name_cn;
    }

    public void setCountry_name_cn(String country_name_cn) {
        this.country_name_cn = country_name_cn;
    }

    public String getCountry_name_en() {
        return country_name_en;
    }

    public void setCountry_name_en(String country_name_en) {
        this.country_name_en = country_name_en;
    }

    public String getPercentage() {
        if (getTotalnums() == 0) return "0.00%";
        return NumberUtil.formatMoney((getNums() / getTotalnums() * 100), 2) + "%";
    }

}
