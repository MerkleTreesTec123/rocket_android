package com.muye.rocket.event;

public class PowerRuleEvent {
    private String content;

    public PowerRuleEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
