package com.muye.rocket.entity;

public class MyWinRecord {

    private String uid;
    private int status;
    private int num;
    private String nowdate;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNowdate() {
        return nowdate;
    }

    public void setNowdate(String nowdate) {
        this.nowdate = nowdate;
    }

    @Override
    public String toString() {
        return "MyWinRecord{" +
                "uid='" + uid + '\'' +
                ", status=" + status +
                ", num=" + num +
                ", nowdate='" + nowdate + '\'' +
                '}';
    }
}
