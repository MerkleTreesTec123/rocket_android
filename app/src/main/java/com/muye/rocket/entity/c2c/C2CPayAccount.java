package com.muye.rocket.entity.c2c;

import android.os.Parcel;
import android.os.Parcelable;

public class C2CPayAccount implements Parcelable {
    private String username;
    private String account;
    private String url;

    private String bannumber;
    private String banname;
    private String fromban;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBannumber() {
        return bannumber;
    }

    public void setBannumber(String bannumber) {
        this.bannumber = bannumber;
    }

    public String getBanname() {
        return banname;
    }

    public void setBanname(String banname) {
        this.banname = banname;
    }

    public String getFromban() {
        return fromban;
    }

    public void setFromban(String fromban) {
        this.fromban = fromban;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.account);
        dest.writeString(this.url);
        dest.writeString(this.bannumber);
        dest.writeString(this.banname);
        dest.writeString(this.fromban);
    }

    public C2CPayAccount() {
    }

    protected C2CPayAccount(Parcel in) {
        this.username = in.readString();
        this.account = in.readString();
        this.url = in.readString();
        this.bannumber = in.readString();
        this.banname = in.readString();
        this.fromban = in.readString();
    }

    public static final Creator<C2CPayAccount> CREATOR = new Creator<C2CPayAccount>() {
        @Override
        public C2CPayAccount createFromParcel(Parcel source) {
            return new C2CPayAccount(source);
        }

        @Override
        public C2CPayAccount[] newArray(int size) {
            return new C2CPayAccount[size];
        }
    };
}
