package com.muye.rocket.entity.exc_wallet;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.List;

public class ExcAssetsEntity {
    private String netassets;
    private List<WalletBean> wallet;

    public double getNetassets() {
        if (TextUtils.isEmpty(netassets)) return 0;
        return new BigDecimal(netassets).doubleValue();
    }

    public void setNetassets(String netassets) {
        this.netassets = netassets;
    }

    public List<WalletBean> getWallet() {
        return wallet;
    }

    public void setWallet(List<WalletBean> wallet) {
        this.wallet = wallet;
    }

    public static class WalletBean {

        private String id;
        private String uid;
        private String coinId;
        private String total;
        private String frozen;
        private String borrow;
        private String ico;
        private String gmtCreate;
        private String gmtModified;
        private String lockTotal;
        private String loginName;
        private String nickName;
        private String realName;
        private String coinName;
        private String shortName;
        private String logo;
        private boolean canWithdraw;
        private boolean canDeposit;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCoinId() {
            return coinId;
        }

        public void setCoinId(String coinId) {
            this.coinId = coinId;
        }

        public double getTotal() {
            if (TextUtils.isEmpty(total)) return 0;
            return new BigDecimal(total).doubleValue();
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public double getFrozen() {
            if (TextUtils.isEmpty(frozen)) return 0;
            return new BigDecimal(frozen).doubleValue();
        }

        public void setFrozen(String frozen) {
            this.frozen = frozen;
        }

        public String getBorrow() {
            return borrow;
        }

        public void setBorrow(String borrow) {
            this.borrow = borrow;
        }

        public String getIco() {
            return ico;
        }

        public void setIco(String ico) {
            this.ico = ico;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public String getLockTotal() {
            return lockTotal;
        }

        public void setLockTotal(String lockTotal) {
            this.lockTotal = lockTotal;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getCoinName() {
            return coinName;
        }

        public void setCoinName(String coinName) {
            this.coinName = coinName;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public boolean isCanWithdraw() {
            return canWithdraw;
        }

        public void setCanWithdraw(boolean canWithdraw) {
            this.canWithdraw = canWithdraw;
        }

        public boolean isCanDeposit() {
            return canDeposit;
        }

        public void setCanDeposit(boolean canDeposit) {
            this.canDeposit = canDeposit;
        }
    }
}
