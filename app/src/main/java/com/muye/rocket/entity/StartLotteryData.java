package com.muye.rocket.entity;

public class StartLotteryData {

    private int status;
    private String msg;
    private int num;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "StartLotteryData{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", num=" + num +
                '}';
    }
}
