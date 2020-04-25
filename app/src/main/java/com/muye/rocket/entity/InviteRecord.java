package com.muye.rocket.entity;

import android.text.TextUtils;

public class InviteRecord {
    private String id;
    private String uid;
    private String username;
    private String rid;
    private String rname;
    private String regtime;
    private String type;
    private String cost;

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getRid() {
        return rid;
    }

    public String getRname() {
        return rname;
    }

    public long getRegtime() {
        if(TextUtils.isEmpty(regtime))return 0;
        return Long.parseLong(regtime);
    }

    public String getType() {
        return type;
    }

    public String getCost() {
        return cost;
    }
}
