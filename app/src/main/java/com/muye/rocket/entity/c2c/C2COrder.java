package com.muye.rocket.entity.c2c;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ifenduo.common.tool.DateTimeTool;
import com.ifenduo.lib_base.tools.NumberUtil;

public class C2COrder {
    private String id;
    private String cid;
    private String uid;
    private String nums;
    private String inputtime;
    private String status;
    private String type;
    private String pay_type;
    private String note;
    private String reason;
    private String payvoucher;
    private String updatetime;
    private String paycode;
    private String endtime;
    private String trpay_type;
    private String price;
    private String coinname;
    private String coin_id;
    private String bank;
    private String alipay;
    private String wxpay;
    private String add_uid;
    private String add_username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getNums() {
        return NumberUtil.string2Double(nums);
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getInputtime() {
        long inputtime_ = 0;
        if (!TextUtils.isEmpty(inputtime)) {
            inputtime_ = Long.parseLong(inputtime);
        }
        return DateTimeTool.getYYYYMMDDHHMMSS(inputtime_);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPayvoucher() {
        return payvoucher;
    }

    public void setPayvoucher(String payvoucher) {
        this.payvoucher = payvoucher;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getPaycode() {
        return paycode;
    }

    public void setPaycode(String paycode) {
        this.paycode = paycode;
    }

    public long getEndtime() {
        if(TextUtils.isEmpty(endtime)){
            return 0;
        }
        return Long.parseLong(endtime);
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTrpay_type() {
        return trpay_type;
    }

    public void setTrpay_type(String trpay_type) {
        this.trpay_type = trpay_type;
    }

    public double getPrice() {
        return NumberUtil.string2Double(price);
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCoinname() {
        return coinname;
    }

    public void setCoinname(String coinname) {
        this.coinname = coinname;
    }

    public String getCoin_id() {
        return coin_id;
    }

    public void setCoin_id(String coin_id) {
        this.coin_id = coin_id;
    }

    public C2CPayAccount getBank() {
        if (TextUtils.isEmpty(bank) || "null".equals(bank)) return null;
        Gson gson = new Gson();
        return gson.fromJson(bank, C2CPayAccount.class);
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public C2CPayAccount getAlipay() {
        if (TextUtils.isEmpty(alipay) || "null".equals(alipay)) return null;
        Gson gson = new Gson();
        return gson.fromJson(alipay, C2CPayAccount.class);
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public C2CPayAccount getWxpay() {
        if (TextUtils.isEmpty(wxpay) || "null".equals(wxpay)) return null;
        Gson gson = new Gson();
        return gson.fromJson(wxpay, C2CPayAccount.class);
    }

    public void setWxpay(String wxpay) {
        this.wxpay = wxpay;
    }

    public String getAdd_uid() {
        return add_uid;
    }

    public void setAdd_uid(String add_uid) {
        this.add_uid = add_uid;
    }

    public String getAdd_username() {
        return add_username;
    }

    public void setAdd_username(String add_username) {
        this.add_username = add_username;
    }
}
