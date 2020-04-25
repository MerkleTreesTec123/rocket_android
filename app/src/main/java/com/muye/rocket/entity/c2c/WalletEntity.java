package com.muye.rocket.entity.c2c;

import android.os.Parcel;
import android.os.Parcelable;

import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.cache.ExcCache;

import java.util.List;

public class WalletEntity implements Parcelable {
    private String total;
    private List<CoinWalletBean> coin_wallet;

    public double getTotal() {
        return NumberUtil.string2Double(total) * ExcCache.getUsdt2RmbCache();
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<CoinWalletBean> getCoin_wallet() {
        return coin_wallet;
    }

    public void setCoin_wallet(List<CoinWalletBean> wallet) {
        this.coin_wallet = wallet;
    }

    public static class CoinWalletBean implements Parcelable {


        private String id;
        private String uid;
        private String coin_id;
        private String total;
        private String frozen;
        private String flag;
        private String type;
        private String inputtime;
        private String updatetime;
        private String version;
        private String status;
        private String coinname;
        private String icon;
        private String createtime;
        private String cash_fee;
        private String cashfee_status;
        private String cashfee_min;
        private String convert_fee;
        private String convertfee_status;
        private String convertfee_min;
        private String usdt_rate;
        private String cash;
        private String orderbys;
        private String recharge_address;
        private String chaintype;
        private String re_id;
        private String cointype;
        private String cny;
        private String dollariteam;
        private String zhangfu;
        private String dollar;
        private String rmb;

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

        public String getCoin_id() {
            return coin_id;
        }

        public void setCoin_id(String coin_id) {
            this.coin_id = coin_id;
        }

        public double getTotal() {
            return NumberUtil.string2Double(total);
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getFrozen() {
            return frozen;
        }

        public void setFrozen(String frozen) {
            this.frozen = frozen;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInputtime() {
            return inputtime;
        }

        public void setInputtime(String inputtime) {
            this.inputtime = inputtime;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCoinname() {
            return coinname;
        }

        public void setCoinname(String coinname) {
            this.coinname = coinname;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getCash_fee() {
            return cash_fee;
        }

        public void setCash_fee(String cash_fee) {
            this.cash_fee = cash_fee;
        }

        public String getCashfee_status() {
            return cashfee_status;
        }

        public void setCashfee_status(String cashfee_status) {
            this.cashfee_status = cashfee_status;
        }

        public String getCashfee_min() {
            return cashfee_min;
        }

        public void setCashfee_min(String cashfee_min) {
            this.cashfee_min = cashfee_min;
        }

        public String getConvert_fee() {
            return convert_fee;
        }

        public void setConvert_fee(String convert_fee) {
            this.convert_fee = convert_fee;
        }

        public String getConvertfee_status() {
            return convertfee_status;
        }

        public void setConvertfee_status(String convertfee_status) {
            this.convertfee_status = convertfee_status;
        }

        public String getConvertfee_min() {
            return convertfee_min;
        }

        public void setConvertfee_min(String convertfee_min) {
            this.convertfee_min = convertfee_min;
        }

        public double getUsdt_rate() {
            return NumberUtil.string2Double(usdt_rate);
        }

        public void setUsdt_rate(double usdt_rate) {
            this.usdt_rate = String.valueOf(usdt_rate);
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getOrderbys() {
            return orderbys;
        }

        public void setOrderbys(String orderbys) {
            this.orderbys = orderbys;
        }

        public String getRecharge_address() {
            return recharge_address;
        }

        public void setRecharge_address(String recharge_address) {
            this.recharge_address = recharge_address;
        }

        public String getChaintype() {
            return chaintype;
        }

        public void setChaintype(String chaintype) {
            this.chaintype = chaintype;
        }

        public String getRe_id() {
            return re_id;
        }

        public void setRe_id(String re_id) {
            this.re_id = re_id;
        }

        public String getCointype() {
            return cointype;
        }

        public void setCointype(String cointype) {
            this.cointype = cointype;
        }

        public String getCny() {
            return cny;
        }

        public void setCny(String cny) {
            this.cny = cny;
        }

        public String getDollariteam() {
            return dollariteam;
        }

        public void setDollariteam(String dollariteam) {
            this.dollariteam = dollariteam;
        }

        public double getZhangfu() {
            return NumberUtil.string2Double(zhangfu);
        }

        public void setZhangfu(String zhangfu) {
            this.zhangfu = zhangfu;
        }

        public double getDollar() {
            return NumberUtil.string2Double(dollar);
        }

        public void setDollar(String dollar) {
            this.dollar = dollar;
        }

        public String getRmb() {
            return rmb;
        }

        public void setRmb(String rmb) {
            this.rmb = rmb;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.uid);
            dest.writeString(this.coin_id);
            dest.writeString(this.total);
            dest.writeString(this.frozen);
            dest.writeString(this.flag);
            dest.writeString(this.type);
            dest.writeString(this.inputtime);
            dest.writeString(this.updatetime);
            dest.writeString(this.version);
            dest.writeString(this.status);
            dest.writeString(this.coinname);
            dest.writeString(this.icon);
            dest.writeString(this.createtime);
            dest.writeString(this.cash_fee);
            dest.writeString(this.cashfee_status);
            dest.writeString(this.cashfee_min);
            dest.writeString(this.convert_fee);
            dest.writeString(this.convertfee_status);
            dest.writeString(this.convertfee_min);
            dest.writeString(this.usdt_rate);
            dest.writeString(this.cash);
            dest.writeString(this.orderbys);
            dest.writeString(this.recharge_address);
            dest.writeString(this.chaintype);
            dest.writeString(this.re_id);
            dest.writeString(this.cointype);
            dest.writeString(this.cny);
            dest.writeString(this.dollariteam);
            dest.writeString(this.zhangfu);
            dest.writeString(this.dollar);
            dest.writeString(this.rmb);
        }

        public CoinWalletBean() {
        }

        protected CoinWalletBean(Parcel in) {
            this.id = in.readString();
            this.uid = in.readString();
            this.coin_id = in.readString();
            this.total = in.readString();
            this.frozen = in.readString();
            this.flag = in.readString();
            this.type = in.readString();
            this.inputtime = in.readString();
            this.updatetime = in.readString();
            this.version = in.readString();
            this.status = in.readString();
            this.coinname = in.readString();
            this.icon = in.readString();
            this.createtime = in.readString();
            this.cash_fee = in.readString();
            this.cashfee_status = in.readString();
            this.cashfee_min = in.readString();
            this.convert_fee = in.readString();
            this.convertfee_status = in.readString();
            this.convertfee_min = in.readString();
            this.usdt_rate = in.readString();
            this.cash = in.readString();
            this.orderbys = in.readString();
            this.recharge_address = in.readString();
            this.chaintype = in.readString();
            this.re_id = in.readString();
            this.cointype = in.readString();
            this.cny = in.readString();
            this.dollariteam = in.readString();
            this.zhangfu = in.readString();
            this.dollar = in.readString();
            this.rmb = in.readString();
        }

        public static final Creator<CoinWalletBean> CREATOR = new Creator<CoinWalletBean>() {
            @Override
            public CoinWalletBean createFromParcel(Parcel source) {
                return new CoinWalletBean(source);
            }

            @Override
            public CoinWalletBean[] newArray(int size) {
                return new CoinWalletBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.total);
        dest.writeTypedList(this.coin_wallet);
    }

    public WalletEntity() {
    }

    protected WalletEntity(Parcel in) {
        this.total = in.readString();
        this.coin_wallet = in.createTypedArrayList(CoinWalletBean.CREATOR);
    }

    public static final Creator<WalletEntity> CREATOR = new Creator<WalletEntity>() {
        @Override
        public WalletEntity createFromParcel(Parcel source) {
            return new WalletEntity(source);
        }

        @Override
        public WalletEntity[] newArray(int size) {
            return new WalletEntity[size];
        }
    };
}
