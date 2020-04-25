package com.muye.rocket.entity;

import android.text.TextUtils;

import com.muye.rocket.tools.StringTools;

import java.util.List;

public class InviteRankEntity {
    private String yaoqingma;
    private String myInviter;
    private String myWeekRank;
    private List<WeeklyBean> weekly;
    private List<HistoryBean> history;
    private List<CostMostBean> costMost;
    private List<RewardBean> reward;
    private String content;
    private String qr_code;

    public String getQr_code() {
        return qr_code;
    }

    public String getContent() {
        return content;
    }

    public String getYaoqingma() {
        if (TextUtils.isEmpty(yaoqingma)) return "";
        return yaoqingma;
    }

    public String getMyInviter() {
        if (TextUtils.isEmpty(myInviter)) return "";
        return myInviter;
    }

    public String getMyWeekRank() {
        if (TextUtils.isEmpty(myWeekRank)) return "";
        return myWeekRank;
    }

    public List<WeeklyBean> getWeekly() {
        return weekly;
    }

    public List<HistoryBean> getHistory() {
        return history;
    }

    public List<CostMostBean> getCostMost() {
        return costMost;
    }

    public List<RewardBean> getReward() {
        return reward;
    }

    public static class WeeklyBean {
        private String num;
        private String uid;
        private String username;
        private String rank;

        public String getNum() {
            return num;
        }

        public String getUid() {
            return uid;
        }

        public String getUsername() {
            return StringTools.phoneNumberFormat(username);
        }

        public String getRank() {
            return rank;
        }
    }

    public static class HistoryBean {
        private String num;
        private String uid;
        private String username;
        private String rank;

        public String getNum() {
            return num;
        }

        public String getUid() {
            return uid;
        }

        public String getUsername() {
            return StringTools.phoneNumberFormat(username);
        }

        public String getRank() {
            return rank;
        }
    }

    public static class CostMostBean {
        private String amount;
        private String uid;
        private String username;
        private String rank;

        public String getAmount() {
            return amount;
        }

        public String getUid() {
            return uid;
        }

        public String getUsername() {
            return StringTools.phoneNumberFormat(username);
        }

        public String getRank() {
            return rank;
        }
    }

    public static class RewardBean {
        private String uid;
        private String coin_id;
        private String coin_name;
        private String total;

        public String getUid() {
            return uid;
        }

        public String getCoin_id() {
            return coin_id;
        }

        public String getCoin_name() {
            return coin_name;
        }

        public String getTotal() {
            return total;
        }
    }
}
