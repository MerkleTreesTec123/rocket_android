package com.muye.rocket.entity.exc_wallet;

import java.util.List;

public class ExcAddressListEntity {
    private List<ExcAddress> withdrawAddress;
    private String appLogo;

    public List<ExcAddress> getWithdrawAddress() {
        return withdrawAddress;
    }

    public void setWithdrawAddress(List<ExcAddress> withdrawAddress) {
        this.withdrawAddress = withdrawAddress;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }
}
