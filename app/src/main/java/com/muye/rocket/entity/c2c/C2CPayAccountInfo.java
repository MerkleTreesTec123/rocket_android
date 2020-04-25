package com.muye.rocket.entity.c2c;

import android.text.TextUtils;

import com.google.gson.Gson;

public class C2CPayAccountInfo {

    private String id;
    private String alipay;
    private String wxpay;
    private String uid;
    private String bank;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public C2CPayAccount getAlipay() {
        if (TextUtils.isEmpty(alipay) || "null".equals(alipay)) {
            return null;
        }else {
            Gson gson=new Gson();
           return gson.fromJson(alipay,C2CPayAccount.class);
        }
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public C2CPayAccount getWxpay() {
        if (TextUtils.isEmpty(wxpay) || "null".equals(wxpay)) {
            return null;
        }else {
            Gson gson=new Gson();
            return gson.fromJson(wxpay,C2CPayAccount.class);
        }
    }

    public void setWxpay(String wxpay) {
        this.wxpay = wxpay;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public C2CPayAccount getBank() {
        if (TextUtils.isEmpty(bank) || "null".equals(bank)) {
            return null;
        }else {
            Gson gson=new Gson();
            return gson.fromJson(bank,C2CPayAccount.class);
        }
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
