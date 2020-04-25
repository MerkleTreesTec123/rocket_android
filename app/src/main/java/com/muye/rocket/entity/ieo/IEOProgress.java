package com.muye.rocket.entity.ieo;

import android.text.TextUtils;

public class IEOProgress {
    private String get_amount;
    private String total_amount;

    public String getGet_amount() {
        if (TextUtils.isEmpty(get_amount)) {
            return "0";
        }
        return get_amount;
    }

    public String getTotal_amount() {
        if (TextUtils.isEmpty(total_amount)) {
            return "0";
        }
        return total_amount;
    }

    public int getProgress() {
        if (TextUtils.isEmpty(get_amount) || TextUtils.isEmpty(total_amount)) {
            return 0;
        }
        double getCount = Double.parseDouble(get_amount);
        double totalAmount = Double.parseDouble(total_amount);
        if (totalAmount <= 0) return 0;
        return (int) (getCount / totalAmount * 100);
    }
}
