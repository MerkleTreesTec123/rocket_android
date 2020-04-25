package com.muye.rocket.entity.exc_wallet;

import android.text.TextUtils;

public class ExcWalletRecord {
    private String fid;
    private String uid;
    private String relationId;
    private String relationCoinId;
    private String relationCoinName;
    private String amount;
    private String operation;
    private String operationDesc;
    private String status;
    private String statusDesc;
    private String createDate;
    private String updateDate;
    private String txId;
    private String rechargeAddress;
    private String withdrawAddress;
    private String memo;
    private String walletOperationDate;
    private String fee;

    public String getFid() {
        return fid;
    }

    public String getUid() {
        return uid;
    }

    public String getRelationId() {
        return relationId;
    }

    public String getRelationCoinId() {
        return relationCoinId;
    }

    public String getRelationCoinName() {
        return relationCoinName;
    }

    public String getAmount() {
        return amount;
    }

    public String getOperation() {
        return operation;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public long getCreateDate() {
        if (TextUtils.isEmpty(createDate)) return 0;
        return Long.parseLong(createDate) / 1000;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getTxId() {
        return txId;
    }

    public String getRechargeAddress() {
        return rechargeAddress;
    }

    public String getWithdrawAddress() {
        return withdrawAddress;
    }

    public String getMemo() {
        return memo;
    }

    public String getWalletOperationDate() {
        return walletOperationDate;
    }

    public String getFee() {
        return fee;
    }

    public String getAddress() {
        if (!TextUtils.isEmpty(rechargeAddress)) {
            return rechargeAddress;
        }
        if (!TextUtils.isEmpty(withdrawAddress)) {
            return withdrawAddress;
        }
        return "";
    }
}
