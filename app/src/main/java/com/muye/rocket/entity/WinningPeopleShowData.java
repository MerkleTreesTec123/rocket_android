package com.muye.rocket.entity;

//中奖人结果
public class WinningPeopleShowData {

    private String rocket_username;
    private String num;

    public String getRocket_username() {
        return rocket_username;
    }

    public void setRocket_username(String rocket_username) {
        this.rocket_username = rocket_username;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "WinningPeopleShowData{" +
                "rocket_username='" + rocket_username + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
