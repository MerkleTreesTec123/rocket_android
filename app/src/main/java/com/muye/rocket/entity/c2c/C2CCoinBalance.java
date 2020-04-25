package com.muye.rocket.entity.c2c;

import com.ifenduo.lib_base.tools.NumberUtil;

public class C2CCoinBalance {

    private String id;
    private String uid;
    private String coin_id;
    private String total;
    private String frozen;
    private String flag;
    private String type;
    private String inputtime;
    private String updatetime;
    private String version;
    private String status;
    private String coinname;
    private String icon;
    private String createtime;
    private String cash_fee;
    private String cashfee_status;
    private String cashfee_min;
    private String convert_fee;
    private String convertfee_status;
    private String convertfee_min;
    private String usdt_rate;
    private String cash;
    private String orderbys;
    private String recharge_address;
    private String chaintype;
    private String re_id;
    private String cash_fee_coin_id;
    private String convert_fee_coin_id;

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

    public double getTotal() {
        return NumberUtil.string2Double(total);
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public double getFrozen() {
        return NumberUtil.string2Double(frozen);
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoinname() {
        return coinname;
    }

    public void setCoinname(String coinname) {
        this.coinname = coinname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(String cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getCashfee_status() {
        return cashfee_status;
    }

    public void setCashfee_status(String cashfee_status) {
        this.cashfee_status = cashfee_status;
    }

    public String getCashfee_min() {
        return cashfee_min;
    }

    public void setCashfee_min(String cashfee_min) {
        this.cashfee_min = cashfee_min;
    }

    public String getConvert_fee() {
        return convert_fee;
    }

    public void setConvert_fee(String convert_fee) {
        this.convert_fee = convert_fee;
    }

    public String getConvertfee_status() {
        return convertfee_status;
    }

    public void setConvertfee_status(String convertfee_status) {
        this.convertfee_status = convertfee_status;
    }

    public String getConvertfee_min() {
        return convertfee_min;
    }

    public void setConvertfee_min(String convertfee_min) {
        this.convertfee_min = convertfee_min;
    }

    public String getUsdt_rate() {
        return usdt_rate;
    }

    public void setUsdt_rate(String usdt_rate) {
        this.usdt_rate = usdt_rate;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getOrderbys() {
        return orderbys;
    }

    public void setOrderbys(String orderbys) {
        this.orderbys = orderbys;
    }

    public String getRecharge_address() {
        return recharge_address;
    }

    public void setRecharge_address(String recharge_address) {
        this.recharge_address = recharge_address;
    }

    public String getChaintype() {
        return chaintype;
    }

    public void setChaintype(String chaintype) {
        this.chaintype = chaintype;
    }

    public String getRe_id() {
        return re_id;
    }

    public void setRe_id(String re_id) {
        this.re_id = re_id;
    }

    public String getCash_fee_coin_id() {
        return cash_fee_coin_id;
    }

    public void setCash_fee_coin_id(String cash_fee_coin_id) {
        this.cash_fee_coin_id = cash_fee_coin_id;
    }

    public String getConvert_fee_coin_id() {
        return convert_fee_coin_id;
    }

    public void setConvert_fee_coin_id(String convert_fee_coin_id) {
        this.convert_fee_coin_id = convert_fee_coin_id;
    }
}
