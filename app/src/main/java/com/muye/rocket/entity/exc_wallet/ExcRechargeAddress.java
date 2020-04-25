package com.muye.rocket.entity.exc_wallet;

import java.util.List;

public class ExcRechargeAddress {
    private String memo;
    private CoinTypeBean coinType;
    private RechargeAddressBean rechargeAddress;
    private PageBean page;

    public String getMemo() {
        return memo;
    }

    public CoinTypeBean getCoinType() {
        return coinType;
    }

    public void setCoinType(CoinTypeBean coinType) {
        this.coinType = coinType;
    }

    public RechargeAddressBean getRechargeAddress() {
        return rechargeAddress;
    }

    public void setRechargeAddress(RechargeAddressBean rechargeAddress) {
        this.rechargeAddress = rechargeAddress;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public static class CoinTypeBean {
        private String id;
        private String name;
        private String type;
        private String shortname;
        private String weblogo;
        private String applogo;
        private String symbol;
        private String status;
        private boolean withdraw;
        private boolean recharge;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getShortname() {
            return shortname;
        }

        public void setShortname(String shortname) {
            this.shortname = shortname;
        }

        public String getWeblogo() {
            return weblogo;
        }

        public void setWeblogo(String weblogo) {
            this.weblogo = weblogo;
        }

        public String getApplogo() {
            return applogo;
        }

        public void setApplogo(String applogo) {
            this.applogo = applogo;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isWithdraw() {
            return withdraw;
        }

        public void setWithdraw(boolean withdraw) {
            this.withdraw = withdraw;
        }

        public boolean isRecharge() {
            return recharge;
        }

        public void setRecharge(boolean recharge) {
            this.recharge = recharge;
        }
    }

    public static class RechargeAddressBean {
        private String fid;
        private String fcoinid;
        private String fadderess;
        private String fuid;
        private long fcreatetime;
        private Object fshortname;

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getFcoinid() {
            return fcoinid;
        }

        public void setFcoinid(String fcoinid) {
            this.fcoinid = fcoinid;
        }

        public String getFadderess() {
            return fadderess;
        }

        public void setFadderess(String fadderess) {
            this.fadderess = fadderess;
        }

        public String getFuid() {
            return fuid;
        }

        public void setFuid(String fuid) {
            this.fuid = fuid;
        }

        public long getFcreatetime() {
            return fcreatetime;
        }

        public void setFcreatetime(long fcreatetime) {
            this.fcreatetime = fcreatetime;
        }

        public Object getFshortname() {
            return fshortname;
        }

        public void setFshortname(Object fshortname) {
            this.fshortname = fshortname;
        }
    }

    public static class PageBean {
        private String totalRows;
        private String pageSize;
        private String currentPage;
        private String totalPages;
        private String pagin;
        private List<ExcRechargeRecord> data;

        public String getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(String totalRows) {
            this.totalRows = totalRows;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public String getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(String totalPages) {
            this.totalPages = totalPages;
        }

        public String getPagin() {
            return pagin;
        }

        public void setPagin(String pagin) {
            this.pagin = pagin;
        }

        public List<ExcRechargeRecord> getData() {
            return data;
        }

        public void setData(List<ExcRechargeRecord> data) {
            this.data = data;
        }
    }
}
