package com.muye.rocket.entity.exchange;

import android.text.TextUtils;

import java.math.BigDecimal;

public class ExcOrder {

    private String types;
    private String leftcount;
    private String fees;
    private String last;
    private String count;
    private String successamount;
    private String source;
    private String type;
    private String price;
    private String buysymbol;
    private String id;
    private String time;
    private String sellsymbol;
    private String status;

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public double getLeftcount() {
        if (TextUtils.isEmpty(leftcount)) return 0;
        return new BigDecimal(leftcount).doubleValue();
    }

    public void setLeftcount(String leftcount) {
        this.leftcount = leftcount;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public double getCount() {
        if (TextUtils.isEmpty(count)) return 0;
        return new BigDecimal(count).doubleValue();
    }

    public void setCount(String count) {
        this.count = count;
    }

    public double getSuccessamount() {
        if (TextUtils.isEmpty(successamount)) return 0;
        return new BigDecimal(successamount).doubleValue();
    }

    public void setSuccessamount(String successamount) {
        this.successamount = successamount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBuysymbol() {
        return buysymbol;
    }

    public void setBuysymbol(String buysymbol) {
        this.buysymbol = buysymbol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSellsymbol() {
        return sellsymbol;
    }

    public void setSellsymbol(String sellsymbol) {
        this.sellsymbol = sellsymbol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
