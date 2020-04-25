package com.muye.rocket.entity.ieo;

import android.text.TextUtils;

import java.math.BigDecimal;

public class IEOReleaseEntity {
    private String lock_amount;
    private String release_amount;

    public double getLock_amount() {
        if (TextUtils.isEmpty(lock_amount)) return 0;
        return new BigDecimal(lock_amount).doubleValue();
    }

    public void setLock_amount(String lock_amount) {
        this.lock_amount = lock_amount;
    }

    public double getRelease_amount() {
        if (TextUtils.isEmpty(release_amount)) return 0;
        return new BigDecimal(release_amount).doubleValue();
    }

    public void setRelease_amount(String release_amount) {
        this.release_amount = release_amount;
    }
}
