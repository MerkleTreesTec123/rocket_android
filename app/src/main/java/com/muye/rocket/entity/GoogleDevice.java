package com.muye.rocket.entity;

public class GoogleDevice {
    private String qecode;
    private String code;
    private String totpKey;
    private String device_name;

    public String getQecode() {
        return qecode;
    }

    public void setQecode(String qecode) {
        this.qecode = qecode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTotpKey() {
        return totpKey;
    }

    public void setTotpKey(String totpKey) {
        this.totpKey = totpKey;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }
}
