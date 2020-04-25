package com.muye.rocket.entity.exc_wallet;

import android.os.Parcel;
import android.os.Parcelable;

public class ExcAddress implements Parcelable {
    private String fid;
    private String fcoinid;
    private String fadderess;
    private String fuid;
    private String fcreatetime;
    private String version;
    private String init;
    private String fremark;
    private String appLogo;
    private String coinName;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFcoinid() {
        return fcoinid;
    }

    public void setFcoinid(String fcoinid) {
        this.fcoinid = fcoinid;
    }

    public String getFadderess() {
        return fadderess;
    }

    public void setFadderess(String fadderess) {
        this.fadderess = fadderess;
    }

    public String getFuid() {
        return fuid;
    }

    public void setFuid(String fuid) {
        this.fuid = fuid;
    }

    public String getFcreatetime() {
        return fcreatetime;
    }

    public void setFcreatetime(String fcreatetime) {
        this.fcreatetime = fcreatetime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getFremark() {
        return fremark;
    }

    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fid);
        dest.writeString(this.fcoinid);
        dest.writeString(this.fadderess);
        dest.writeString(this.fuid);
        dest.writeString(this.fcreatetime);
        dest.writeString(this.version);
        dest.writeString(this.init);
        dest.writeString(this.fremark);
        dest.writeString(this.appLogo);
        dest.writeString(this.coinName);
    }

    public ExcAddress() {
    }

    protected ExcAddress(Parcel in) {
        this.fid = in.readString();
        this.fcoinid = in.readString();
        this.fadderess = in.readString();
        this.fuid = in.readString();
        this.fcreatetime = in.readString();
        this.version = in.readString();
        this.init = in.readString();
        this.fremark = in.readString();
        this.appLogo = in.readString();
        this.coinName = in.readString();
    }

    public static final Parcelable.Creator<ExcAddress> CREATOR = new Parcelable.Creator<ExcAddress>() {
        @Override
        public ExcAddress createFromParcel(Parcel source) {
            return new ExcAddress(source);
        }

        @Override
        public ExcAddress[] newArray(int size) {
            return new ExcAddress[size];
        }
    };
}
