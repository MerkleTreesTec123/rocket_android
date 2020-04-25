package com.muye.rocket.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MillionInviteBean {

    @SerializedName("2019-11-17")
    private List<_$201911Bean> _$20191117;
    @SerializedName("2019-11-18")
    private List<_$201911Bean> _$20191118;
    @SerializedName("2019-11-19")
    private List<_$201911Bean> _$20191119;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<_$201911Bean> get_$20191119() {
        return _$20191119;
    }

    public void set_$20191119(List<_$201911Bean> _$20191119) {
        this._$20191119 = _$20191119;
    }

    public List<_$201911Bean> get_$20191117() {
        return _$20191117;
    }

    public void set_$20191117(List<_$201911Bean> _$20191117) {
        this._$20191117 = _$20191117;
    }

    public List<_$201911Bean> get_$20191118() {
        return _$20191118;
    }

    public void set_$20191118(List<_$201911Bean> _$20191118) {
        this._$20191118 = _$20191118;
    }

    public static class _$201911Bean {
        /**
         * fuid : 85193
         * fname : 13225853778
         * invite_date : 2019-10-31
         * rid : 174548
         * r_phone : 15720519937
         * r_email : null
         * r_status : 1
         * r_time : 2019-10-31 23:16:28
         */

        private String fuid;
        private String fname;
        private String invite_date;
        private String rid;
        private String r_phone;
        private String r_email;
        private int r_status;
        private String r_time;

        public String getFuid() {
            return fuid;
        }

        public void setFuid(String fuid) {
            this.fuid = fuid;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getInvite_date() {
            return invite_date;
        }

        public void setInvite_date(String invite_date) {
            this.invite_date = invite_date;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getR_phone() {
            return r_phone;
        }

        public void setR_phone(String r_phone) {
            this.r_phone = r_phone;
        }

        public String getR_email() {
            return r_email;
        }

        public void setR_email(String r_email) {
            this.r_email = r_email;
        }

        public int getR_status() {
            return r_status;
        }

        public void setR_status(int r_status) {
            this.r_status = r_status;
        }

        public String getR_time() {
            return r_time;
        }

        public void setR_time(String r_time) {
            this.r_time = r_time;
        }

        @Override
        public String toString() {
            return "_$201911Bean{" +
                    "fuid=" + fuid +
                    ", fname='" + fname + '\'' +
                    ", invite_date='" + invite_date + '\'' +
                    ", rid=" + rid +
                    ", r_phone='" + r_phone + '\'' +
                    ", r_email=" + r_email +
                    ", r_status=" + r_status +
                    ", r_time='" + r_time + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MillionInviteBean{" +
                "_$20191116=" + _$20191119 +
                ", _$20191117=" + _$20191117 +
                ", _$20191118=" + _$20191118 +
                '}';
    }
}
