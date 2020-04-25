package com.muye.rocket.event;

public class IEOCountDownEvent {
    private String title;
    private String hint;
    private long time;

    public IEOCountDownEvent(String title, String hint,long time) {
        this.title = title;
        this.hint=hint;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
