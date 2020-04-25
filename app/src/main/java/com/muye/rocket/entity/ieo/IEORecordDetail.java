package com.muye.rocket.entity.ieo;

import android.text.TextUtils;

import java.util.List;

public class IEORecordDetail {
    private InfoBean info;
    private List<LogBean> log;

    public InfoBean getInfo() {
        return info;
    }

    public List<LogBean> getLog() {
        return log;
    }

    public static class InfoBean {
        private String id;
        private String uid;
        private String round;
        private String proid;
        private String buy_coin_id;
        private String buy_coin_name;
        private String get_coin_id;
        private String get_coin_name;
        private String amount;
        private String get_amount;
        private String lock_amount;
        private String release_amount;
        private String inputtime;
        private String par_coin_name;
        private String platform_coin_name;
        private String release_day;
        private String release_today;

        public String getId() {
            return id;
        }

        public String getUid() {
            return uid;
        }

        public String getRound() {
            return round;
        }

        public String getProid() {
            return proid;
        }

        public String getBuy_coin_id() {
            return buy_coin_id;
        }

        public String getBuy_coin_name() {
            return buy_coin_name;
        }

        public String getGet_coin_id() {
            return get_coin_id;
        }

        public String getGet_coin_name() {
            return get_coin_name;
        }

        public String getAmount() {
            return amount;
        }

        public String getGet_amount() {
            return get_amount;
        }

        public String getLock_amount() {
            return lock_amount;
        }

        public String getRelease_amount() {
            return release_amount;
        }

        public long getInputtime() {
            if (TextUtils.isEmpty(inputtime)) return 0;
            return Long.parseLong(inputtime);
        }

        public String getPar_coin_name() {
            return par_coin_name;
        }

        public String getPlatform_coin_name() {
            return platform_coin_name;
        }

        public String getRelease_day() {
            return release_day;
        }

        public String getRelease_today() {
            return release_today;
        }
    }

    public static class LogBean {
        private String id;
        private String uid;
        private String buy_id;
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

        public String getBuy_id() {
            return buy_id;
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
