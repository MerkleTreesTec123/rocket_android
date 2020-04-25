package com.muye.rocket.entity;
// 查询我将要抽奖的结果
public class MillionCatMyData {

    private int status;
    private int num;
    private int type;
    private String date;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MillionCatMyData{" +
                "status=" + status +
                ", num=" + num +
                ", type=" + type +
                ", date='" + date + '\'' +
                '}';
    }
}
