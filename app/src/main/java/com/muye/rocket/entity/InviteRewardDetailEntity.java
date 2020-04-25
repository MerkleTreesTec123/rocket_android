package com.muye.rocket.entity;

import android.text.TextUtils;

import java.util.List;

public class InviteRewardDetailEntity {
    private RewardBean reward;
    private List<LogBean> log;
    private String content;

    public String getContent() {
        return content;
    }

    public RewardBean getReward() {
        return reward;
    }

    public List<LogBean> getLog() {
        return log;
    }

    public static class RewardBean {
        private String id;
        private String uid;
        private String coin_id;
        private String coin_name;
        private String total;
        private String frozen;
        private String release_total;
        private String status;

        public String getId() {
            return id;
        }

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

        public String getFrozen() {
            return frozen;
        }

        public String getRelease_total() {
            return release_total;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class LogBean {
        private String id;
        private String uid;
        private String coin_id;
        private String coin_name;
        private String amount;
        private String type;
        private String note;
        private String inputtime;

        public String getId() {
            return id;
        }

        public String getUid() {
            return uid;
        }

        public String getCoin_id() {
            return coin_id;
        }

        public String getCoin_name() {
            return coin_name;
        }

        public String getAmount() {
            return amount;
        }

        public String getType() {
            return type;
        }

        public String getNote() {
            return note;
        }

        public long getInputtime() {
            if (TextUtils.isEmpty(inputtime)) return 0;
            return Long.parseLong(inputtime);
        }
    }
}
