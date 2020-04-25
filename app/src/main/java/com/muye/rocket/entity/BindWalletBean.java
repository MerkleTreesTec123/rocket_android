package com.muye.rocket.entity;

public class BindWalletBean {


    /**
     * id : 15
     * cat_uid : 491
     * rocket_uid : 211617
     * status : 1
     * create_time : 2019-11-06 16:01:27
     * cat_username : lilulilu
     * cat_phone : 13615513896
     * cat_realname : 1
     * cat_cardno : 342224111111111111
     * rocket_username : 17611268836
     * rocket_email : null
     * rocket_realname : 雷敏
     * rocket_card : 3433434367676643
     * system_type : 2
     * system_name : 好望角
     * active : 1
     */

    private int id;
    private String cat_uid;
    private String rocket_uid;
    private int status;
    private String create_time;
    private String cat_username;
    private String cat_phone;
    private String cat_realname;
    private String cat_cardno;
    private String rocket_username;
    private Object rocket_email;
    private String rocket_realname;
    private String rocket_card;
    private int system_type;
    private String system_name;
    private int active;
    private String usdt_y;
    private String cat_y;
    private String cag_y;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat_uid() {
        return cat_uid;
    }

    public void setCat_uid(String cat_uid) {
        this.cat_uid = cat_uid;
    }

    public String getRocket_uid() {
        return rocket_uid;
    }

    public void setRocket_uid(String rocket_uid) {
        this.rocket_uid = rocket_uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCat_username() {
        return cat_username;
    }

    public void setCat_username(String cat_username) {
        this.cat_username = cat_username;
    }

    public String getCat_phone() {
        return cat_phone;
    }

    public void setCat_phone(String cat_phone) {
        this.cat_phone = cat_phone;
    }

    public String getCat_realname() {
        return cat_realname;
    }

    public void setCat_realname(String cat_realname) {
        this.cat_realname = cat_realname;
    }

    public String getCat_cardno() {
        return cat_cardno;
    }

    public void setCat_cardno(String cat_cardno) {
        this.cat_cardno = cat_cardno;
    }

    public String getRocket_username() {
        return rocket_username;
    }

    public void setRocket_username(String rocket_username) {
        this.rocket_username = rocket_username;
    }

    public Object getRocket_email() {
        return rocket_email;
    }

    public void setRocket_email(Object rocket_email) {
        this.rocket_email = rocket_email;
    }

    public String getRocket_realname() {
        return rocket_realname;
    }

    public void setRocket_realname(String rocket_realname) {
        this.rocket_realname = rocket_realname;
    }

    public String getRocket_card() {
        return rocket_card;
    }

    public void setRocket_card(String rocket_card) {
        this.rocket_card = rocket_card;
    }

    public int getSystem_type() {
        return system_type;
    }

    public void setSystem_type(int system_type) {
        this.system_type = system_type;
    }

    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getUsdt_y() {
        return usdt_y;
    }

    public void setUsdt_y(String usdt_y) {
        this.usdt_y = usdt_y;
    }

    public String getCat_y() {
        return cat_y;
    }

    public void setCat_y(String cat_y) {
        this.cat_y = cat_y;
    }

    public String getCag_y() {
        return cag_y;
    }

    public void setCag_y(String cag_y) {
        this.cag_y = cag_y;
    }

    @Override
    public String toString() {
        return "BindWalletBean{" +
                "id=" + id +
                ", cat_uid='" + cat_uid + '\'' +
                ", rocket_uid='" + rocket_uid + '\'' +
                ", status=" + status +
                ", create_time='" + create_time + '\'' +
                ", cat_username='" + cat_username + '\'' +
                ", cat_phone='" + cat_phone + '\'' +
                ", cat_realname='" + cat_realname + '\'' +
                ", cat_cardno='" + cat_cardno + '\'' +
                ", rocket_username='" + rocket_username + '\'' +
                ", rocket_email=" + rocket_email +
                ", rocket_realname='" + rocket_realname + '\'' +
                ", rocket_card='" + rocket_card + '\'' +
                ", system_type=" + system_type +
                ", system_name='" + system_name + '\'' +
                ", active=" + active +
                ", usdt_y='" + usdt_y + '\'' +
                ", cat_y='" + cat_y + '\'' +
                ", cag_y='" + cag_y + '\'' +
                '}';
    }
}
