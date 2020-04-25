package com.muye.rocket.entity;

import android.text.TextUtils;

import com.sunfusheng.marqueeview.IMarqueeItem;

public class MarqueeEntity implements IMarqueeItem {
    private String id;
    private String catid;
    private String title;
    private String inputtime;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public CharSequence marqueeMessage() {
        return TextUtils.isEmpty(title) ? "" : title;
    }
}
