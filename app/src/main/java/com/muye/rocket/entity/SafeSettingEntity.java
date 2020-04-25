package com.muye.rocket.entity;

import android.text.TextUtils;

public class SafeSettingEntity {
    private String securityLevel;
    private String device_name;
    private boolean bindTrade;
    private String bindcount;
    private FuserBean fuser;
    private IdentityBean identity;
    private String loginName;
    private String phoneString;
    private boolean bindLogin;

    public int getSecurityLevel() {
        if(TextUtils.isEmpty(securityLevel))return 0;
        return Integer.parseInt(securityLevel);
    }
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public boolean isBindTrade() {
        return bindTrade;
    }

    public void setBindTrade(boolean bindTrade) {
        this.bindTrade = bindTrade;
    }

    public String getBindcount() {
        return bindcount;
    }

    public void setBindcount(String bindcount) {
        this.bindcount = bindcount;
    }

    public FuserBean getFuser() {
        return fuser;
    }

    public void setFuser(FuserBean fuser) {
        this.fuser = fuser;
    }

    public IdentityBean getIdentity() {
        return identity;
    }

    public void setIdentity(IdentityBean identity) {
        this.identity = identity;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhoneString() {
        return phoneString;
    }

    public void setPhoneString(String phoneString) {
        this.phoneString = phoneString;
    }

    public boolean isBindLogin() {
        return bindLogin;
    }

    public void setBindLogin(boolean bindLogin) {
        this.bindLogin = bindLogin;
    }

    public static class FuserBean {
        private String fid;
        private String fshowid;
        private String floginname;
        private String fnickname;
        private String ftelephone;
        private String femail;
        private String frealname;
        private String fidentityno;
        private String fidentitytype;
        private String fgoogleurl;
        private String fstatus;
        private String fstatus_s;
        private boolean fhasrealvalidate;
        private String fhasrealvalidatetime;
        private boolean fistelephonebind;
        private boolean fismailbind;
        private boolean fgooglebind;
        private String fupdatetime;
        private String fareacode;
        private String fintrouid;
        private String finvalidateintrocount;
        private String fiscny;
        private String fiscny_s;
        private String fiscoin;
        private String fiscoin_s;
        private String fbirth;
        private String flastlogintime;
        private String fregistertime;
        private String fqqopenid;
        private String funionid;
        private String fagentid;
        private String fleverlock;
        private String ip;
        private String score;
        private String level;

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getFshowid() {
            return fshowid;
        }

        public void setFshowid(String fshowid) {
            this.fshowid = fshowid;
        }

        public String getFloginname() {
            return floginname;
        }

        public void setFloginname(String floginname) {
            this.floginname = floginname;
        }

        public String getFnickname() {
            return fnickname;
        }

        public void setFnickname(String fnickname) {
            this.fnickname = fnickname;
        }

        public String getFtelephone() {
            return ftelephone;
        }

        public void setFtelephone(String ftelephone) {
            this.ftelephone = ftelephone;
        }

        public String getFemail() {
            return femail;
        }

        public void setFemail(String femail) {
            this.femail = femail;
        }

        public String getFrealname() {
            return frealname;
        }

        public void setFrealname(String frealname) {
            this.frealname = frealname;
        }

        public String getFidentityno() {
            return fidentityno;
        }

        public void setFidentityno(String fidentityno) {
            this.fidentityno = fidentityno;
        }

        public String getFidentitytype() {
            return fidentitytype;
        }

        public void setFidentitytype(String fidentitytype) {
            this.fidentitytype = fidentitytype;
        }

        public String getFgoogleurl() {
            return fgoogleurl;
        }

        public void setFgoogleurl(String fgoogleurl) {
            this.fgoogleurl = fgoogleurl;
        }

        public String getFstatus() {
            return fstatus;
        }

        public void setFstatus(String fstatus) {
            this.fstatus = fstatus;
        }

        public String getFstatus_s() {
            return fstatus_s;
        }

        public void setFstatus_s(String fstatus_s) {
            this.fstatus_s = fstatus_s;
        }

        public boolean isFhasrealvalidate() {
            return fhasrealvalidate;
        }

        public void setFhasrealvalidate(boolean fhasrealvalidate) {
            this.fhasrealvalidate = fhasrealvalidate;
        }

        public String getFhasrealvalidatetime() {
            return fhasrealvalidatetime;
        }

        public void setFhasrealvalidatetime(String fhasrealvalidatetime) {
            this.fhasrealvalidatetime = fhasrealvalidatetime;
        }

        public boolean isFistelephonebind() {
            return fistelephonebind;
        }

        public void setFistelephonebind(boolean fistelephonebind) {
            this.fistelephonebind = fistelephonebind;
        }

        public boolean isFismailbind() {
            return fismailbind;
        }

        public void setFismailbind(boolean fismailbind) {
            this.fismailbind = fismailbind;
        }

        public boolean isFgooglebind() {
            return fgooglebind;
        }

        public void setFgooglebind(boolean fgooglebind) {
            this.fgooglebind = fgooglebind;
        }

        public String getFupdatetime() {
            return fupdatetime;
        }

        public void setFupdatetime(String fupdatetime) {
            this.fupdatetime = fupdatetime;
        }

        public String getFareacode() {
            return fareacode;
        }

        public void setFareacode(String fareacode) {
            this.fareacode = fareacode;
        }

        public String getFintrouid() {
            return fintrouid;
        }

        public void setFintrouid(String fintrouid) {
            this.fintrouid = fintrouid;
        }

        public String getFinvalidateintrocount() {
            return finvalidateintrocount;
        }

        public void setFinvalidateintrocount(String finvalidateintrocount) {
            this.finvalidateintrocount = finvalidateintrocount;
        }

        public String getFiscny() {
            return fiscny;
        }

        public void setFiscny(String fiscny) {
            this.fiscny = fiscny;
        }

        public String getFiscny_s() {
            return fiscny_s;
        }

        public void setFiscny_s(String fiscny_s) {
            this.fiscny_s = fiscny_s;
        }

        public String getFiscoin() {
            return fiscoin;
        }

        public void setFiscoin(String fiscoin) {
            this.fiscoin = fiscoin;
        }

        public String getFiscoin_s() {
            return fiscoin_s;
        }

        public void setFiscoin_s(String fiscoin_s) {
            this.fiscoin_s = fiscoin_s;
        }

        public String getFbirth() {
            return fbirth;
        }

        public void setFbirth(String fbirth) {
            this.fbirth = fbirth;
        }

        public String getFlastlogintime() {
            return flastlogintime;
        }

        public void setFlastlogintime(String flastlogintime) {
            this.flastlogintime = flastlogintime;
        }

        public String getFregistertime() {
            return fregistertime;
        }

        public void setFregistertime(String fregistertime) {
            this.fregistertime = fregistertime;
        }

        public String getFqqopenid() {
            return fqqopenid;
        }

        public void setFqqopenid(String fqqopenid) {
            this.fqqopenid = fqqopenid;
        }

        public String getFunionid() {
            return funionid;
        }

        public void setFunionid(String funionid) {
            this.funionid = funionid;
        }

        public String getFagentid() {
            return fagentid;
        }

        public void setFagentid(String fagentid) {
            this.fagentid = fagentid;
        }

        public String getFleverlock() {
            return fleverlock;
        }

        public void setFleverlock(String fleverlock) {
            this.fleverlock = fleverlock;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    public static class IdentityBean{
        private String fid;
        private String fuid;
        private String fcountry;
        private String fname;
        private String fcode;
        private String ftype;
        private String fstatus;
        private String fcreatetime;
        private String fupdatetime;
        private String fstatus_s;
        private String ftype_s;
        private String ip;

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getFuid() {
            return fuid;
        }

        public void setFuid(String fuid) {
            this.fuid = fuid;
        }

        public String getFcountry() {
            return fcountry;
        }

        public void setFcountry(String fcountry) {
            this.fcountry = fcountry;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getFcode() {
            return fcode;
        }

        public void setFcode(String fcode) {
            this.fcode = fcode;
        }

        public String getFtype() {
            return ftype;
        }

        public void setFtype(String ftype) {
            this.ftype = ftype;
        }

        public String getFstatus() {
            return fstatus;
        }

        public void setFstatus(String fstatus) {
            this.fstatus = fstatus;
        }

        public String getFcreatetime() {
            return fcreatetime;
        }

        public void setFcreatetime(String fcreatetime) {
            this.fcreatetime = fcreatetime;
        }

        public String getFupdatetime() {
            return fupdatetime;
        }

        public void setFupdatetime(String fupdatetime) {
            this.fupdatetime = fupdatetime;
        }

        public String getFstatus_s() {
            return fstatus_s;
        }

        public void setFstatus_s(String fstatus_s) {
            this.fstatus_s = fstatus_s;
        }

        public String getFtype_s() {
            return ftype_s;
        }

        public void setFtype_s(String ftype_s) {
            this.ftype_s = ftype_s;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }
}
