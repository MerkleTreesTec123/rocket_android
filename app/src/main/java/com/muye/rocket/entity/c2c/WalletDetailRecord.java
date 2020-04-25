package com.muye.rocket.entity.c2c;

import android.text.TextUtils;

import com.ifenduo.common.tool.DateTimeTool;
import com.ifenduo.lib_base.tools.NumberUtil;

public class WalletDetailRecord {

    /**
     * id : 760
     * uid : 279
     * wallet_id :
     * coin_id : 1
     * coin_name : BTC
     * value : -0.5000000000
     * balance : 0.0000000000
     * note : 投入理财
     * type : 4
     * inputtime : 1561016559
     * cid : 0
     * status : 1
     */

    private String id;
    private String uid;
    private String wallet_id;
    private String coin_id;
    private String coin_name;
    private String value;
    private String balance;
    private String note;
    private String type;
    private String inputtime;
    private String cid;
    private String status;

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

    public String getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getCoin_id() {
        return coin_id;
    }

    public void setCoin_id(String coin_id) {
        this.coin_id = coin_id;
    }

    public String getCoin_name() {
        return coin_name;
    }

    public void setCoin_name(String coin_name) {
        this.coin_name = coin_name;
    }

    public double getValue() {
        return NumberUtil.string2Double(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
