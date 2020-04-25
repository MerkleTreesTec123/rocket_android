package com.muye.rocket.api;

public class ExcWalletURLConfig {
    //资产
    public static final String URL_EXC_WALLET_ASSETS = "/v1/user/balance.html";
    //资产详情
    public static final String URL_EXC_WALLET_ASSETS_DETAIL = "/index.php?s=javaapi&c=api&m=coinInfo";
    //充值地址
    public static final String URL_EXC_WALLET_RECHARGE_ADDRESS = "/v1/deposit/coin.html";
    //资产账单
    public static final String URL_EXC_WALLET_RECORD = "/v1/user/operation.html";
    //提币地址
    public static final String URL_EXC_WALLET_WITHDRAW_ADDRESS_LIST = "/v1/coin/list_address.html";
    //删除提币地址
    public static final String URL_EXC_WALLET_DELETE_WITHDRAW_ADDRESS = "/v1/coin/address/del.html";
    //添加提币地址
    public static final String URL_EXC_WALLET_ADD_ADDRESS = "/v1/coin/address.html";
    //发送短信到绑定的手机
    public static final String URL_EXC_SEND_SMS_BIND = "/v1/validate/sign.html";
    //提币相关设置
    public static final String URL_EXC_WITHDRAW_SETTING = "/v1/system/fee.html";
    //提币
    public static final String URL_EXC_WITHDRAW = "/v1/coin/withdraw.html";
    //
    public static final String URL_EXC_ADDRESS_INFO = "index.php?s=javaapi&c=api&m=addressInfo";
}
