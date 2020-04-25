package com.muye.rocket.entity.ieo;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class IEORecord implements Parcelable{
    private String id;
    private String uid;
    private String round;
    private String proid;
    private String buy_coin_id;
    private String buy_coin_name;
    private String get_coin_id;
    private String get_coin_name;
    private String amount;
    private String get_amount;
    private String lock_amount;
    private String release_amount;
    private String inputtime;
    private String release_day;
    private String release_today;
    private String par_coin_name;
    private String platform_coin_name;

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getRound() {
        return round;
    }

    public String getProid() {
        return proid;
    }

    public String getBuy_coin_id() {
        return buy_coin_id;
    }

    public String getBuy_coin_name() {
        return buy_coin_name;
    }

    public String getGet_coin_id() {
        return get_coin_id;
    }

    public String getGet_coin_name() {
        return get_coin_name;
    }

    public String getAmount() {
        return amount;
    }

    public String getGet_amount() {
        return get_amount;
    }

    public String getLock_amount() {
        return lock_amount;
    }

    public String getRelease_amount() {
        return release_amount;
    }

    public long getInputtime() {
        if(TextUtils.isEmpty(inputtime))return 0;
        return Long.parseLong(inputtime);
    }

    public String getRelease_day() {
        return release_day;
    }

    public String getRelease_today() {
        return release_today;
    }

    public String getPar_coin_name() {
        return par_coin_name;
    }

    public String getPlatform_coin_name() {
        return platform_coin_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.round);
        dest.writeString(this.proid);
        dest.writeString(this.buy_coin_id);
        dest.writeString(this.buy_coin_name);
        dest.writeString(this.get_coin_id);
        dest.writeString(this.get_coin_name);
        dest.writeString(this.amount);
        dest.writeString(this.get_amount);
        dest.writeString(this.lock_amount);
        dest.writeString(this.release_amount);
        dest.writeString(this.inputtime);
        dest.writeString(this.release_day);
        dest.writeString(this.release_today);
        dest.writeString(this.par_coin_name);
        dest.writeString(this.platform_coin_name);
    }

    public IEORecord() {
    }

    protected IEORecord(Parcel in) {
        this.id = in.readString();
        this.uid = in.readString();
        this.round = in.readString();
        this.proid = in.readString();
        this.buy_coin_id = in.readString();
        this.buy_coin_name = in.readString();
        this.get_coin_id = in.readString();
        this.get_coin_name = in.readString();
        this.amount = in.readString();
        this.get_amount = in.readString();
        this.lock_amount = in.readString();
        this.release_amount = in.readString();
        this.inputtime = in.readString();
        this.release_day = in.readString();
        this.release_today = in.readString();
        this.par_coin_name = in.readString();
        this.platform_coin_name = in.readString();
    }

    public static final Parcelable.Creator<IEORecord> CREATOR = new Parcelable.Creator<IEORecord>() {
        @Override
        public IEORecord createFromParcel(Parcel source) {
            return new IEORecord(source);
        }

        @Override
        public IEORecord[] newArray(int size) {
            return new IEORecord[size];
        }
    };
}
