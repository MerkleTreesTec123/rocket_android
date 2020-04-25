package com.muye.rocket.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class PhoneArea implements Parcelable {

    /**
     * en : Zaire
     * zh : 扎伊尔
     * locale : ZR
     * code : 243
     */

    private String en;
    private String zh;
    private String locale;
    private int code;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.en);
        dest.writeString(this.zh);
        dest.writeString(this.locale);
        dest.writeInt(this.code);
    }

    public PhoneArea() {
    }

    protected PhoneArea(Parcel in) {
        this.en = in.readString();
        this.zh = in.readString();
        this.locale = in.readString();
        this.code = in.readInt();
    }

    public static final Creator<PhoneArea> CREATOR = new Creator<PhoneArea>() {
        @Override
        public PhoneArea createFromParcel(Parcel source) {
            return new PhoneArea(source);
        }

        @Override
        public PhoneArea[] newArray(int size) {
            return new PhoneArea[size];
        }
    };
}
