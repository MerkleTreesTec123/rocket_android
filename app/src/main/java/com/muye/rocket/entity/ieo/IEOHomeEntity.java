package com.muye.rocket.entity.ieo;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IEOHomeEntity {
    private DefaultMostBean defaultMost;
    private IeoBean ieo;
    private List<DefaultMostBean> moneyList;

    public DefaultMostBean getDefaultMost() {
        return defaultMost;
    }

    public IeoBean getIeo() {
        return ieo;
    }

    public List<DefaultMostBean> getMoneyList() {
        if (defaultMost != null && !TextUtils.isEmpty(defaultMost.getId())) {
            DefaultMostBean defaultMostBean = new DefaultMostBean();
            defaultMostBean.setId(defaultMost.getId());
            defaultMostBean.setAmount(defaultMost.getAmount());
            defaultMostBean.setCoin_id(defaultMost.getCoin_id());
            defaultMostBean.setCoin_name(defaultMost.getCoin_name());
            defaultMostBean.setProid(defaultMost.getProid());
            defaultMostBean.setIs_default(defaultMost.is_default);
            if (moneyList != null) {
                if (moneyList.size() > 0) {
                    DefaultMostBean bean_ = null;
                    for (int i = 0; i < moneyList.size(); i++) {
                        DefaultMostBean bean = moneyList.get(i);
                        if (bean != null && defaultMost.getId().equals(bean.getId())) {
                            bean_ = bean;
                        }
                    }
                    if (bean_ == null) {
                        moneyList.add(0, defaultMostBean);
                    }
                } else {
                    moneyList.add(defaultMostBean);
                }
            } else {
                moneyList = new ArrayList<>();
                moneyList.add(defaultMostBean);
            }
        }
        return moneyList;
    }

    public static class DefaultMostBean {
        private String id;
        private String coin_id;
        private String coin_name;
        private String amount;
        private String proid;
        private String is_default;

        public String getId() {
            return id;
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

        public double getAmountDouble() {
            if (TextUtils.isEmpty(amount)) return 0;
            return new BigDecimal(amount).doubleValue();
        }

        public String getProid() {
            return proid;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setCoin_id(String coin_id) {
            this.coin_id = coin_id;
        }

        public void setCoin_name(String coin_name) {
            this.coin_name = coin_name;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setProid(String proid) {
            this.proid = proid;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }
    }

    public static class IeoBean {

        private String id;
        private String title;
        private String status;
        private String par_coin_id;
        private String par_coin_name;
        private String platform_coin_id;
        private String platform_coin_name;
        private String rate;
        private String deal_price;
        private String current_round;
        private String inputtime;
        private String start_time;
        private String par_num;
        private String endtime;
        private String last_round_price;
        private String round_endtime;
        private String round_start_time;
        private String next_round_start_time;
        private String current_time;
        private String count_down;
        private String max_deal_num;
        private String bought;
        private String ruleinfo;

        public String getMax_deal_num() {
            if (TextUtils.isEmpty(max_deal_num)) {
                return "0";
            }
            return max_deal_num;
        }

        public String getBought() {
            if (TextUtils.isEmpty(bought)) {
                return "0";
            }
            return bought;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getStatus() {
            return status;
        }

        public String getPar_coin_id() {
            return par_coin_id;
        }

        public String getPar_coin_name() {
            return par_coin_name;
        }

        public String getPlatform_coin_id() {
            return platform_coin_id;
        }

        public String getPlatform_coin_name() {
            return platform_coin_name;
        }

        public String getRate() {
            return rate;
        }

        public double getDeal_price() {
            if (TextUtils.isEmpty(deal_price)) return 0;
            return new BigDecimal(deal_price).doubleValue();
        }

        public String getCurrent_round() {
            if (TextUtils.isEmpty(current_round)) {
                return "--";
            } else {
                return current_round;
            }
        }

        public String getLastRound() {
            if (TextUtils.isEmpty(current_round)) {
                return "--";
            } else {
                return String.valueOf(Integer.parseInt(current_round) - 1);
            }
        }

        public String getInputtime() {
            return inputtime;
        }

        public long getStart_time() {
            if (TextUtils.isEmpty(start_time)) return 0;
            return Long.parseLong(start_time);
        }

        public String getPar_num() {
            return par_num;
        }

        public long getEndtime() {
            if (TextUtils.isEmpty(endtime)) {
                return 0;
            }
            return Long.parseLong(endtime);
        }

        public String getLast_round_price() {
            return last_round_price;
        }

        public long getRound_endtime() {
            if (TextUtils.isEmpty(round_endtime)) return 0;
            return Long.parseLong(round_endtime);
        }

        public long getRound_start_time() {
            if (TextUtils.isEmpty(round_start_time)) return 0;
            return Long.parseLong(round_start_time);
        }

        public long getNext_round_start_time() {
            if (TextUtils.isEmpty(next_round_start_time)) {
                return 0;
            }
            return Long.parseLong(next_round_start_time);
        }

        public long getCurrent_time() {
            if (TextUtils.isEmpty(current_time)) return 0;
            return Long.parseLong(current_time);
        }

        public String getCount_down() {
            return count_down;
        }

        public String getRuleinfo() {
            return ruleinfo;
        }

        public void setRuleinfo(String ruleinfo) {
            this.ruleinfo = ruleinfo;
        }
    }
}
