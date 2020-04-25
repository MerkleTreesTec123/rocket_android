package com.muye.rocket.event;

public class AddExcAddressEvent {
    private String coinID;

    public AddExcAddressEvent(String coinID) {
        this.coinID = coinID;
    }

    public String getCoinID() {
        return coinID;
    }

    public void setCoinID(String coinID) {
        this.coinID = coinID;
    }
}
