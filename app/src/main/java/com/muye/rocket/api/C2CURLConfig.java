package com.muye.rocket.api;

public class C2CURLConfig {
    //币种列表
    public static final String URL_C2C_COIN_LIST = "index.php?s=wallet&c=api&m=getCoinList";

    //发布列表
    public static final String URL_C2C_RELEASE_LIST = "index.php?s=trans&c=api&m=getTrans";

    //订单列表
    public static final String URL_C2C_ORDER_LIST = "index.php?s=trans&c=api&m=getOrder";

    //C2C交易
    public static final String URL_C2C_SUBMIT_TRANSACTION = "index.php?s=trans&c=api&m=transaction";

    //C2C订单
    public static final String URL_C2C_ORDER = "index.php?s=trans&c=api&m=getOrder";

    //修改订单状态
    public static final String URL_C2C_UPDATE_ORDER_STATUS = "index.php?s=trans&c=api&m=updateOrder";

    //获取银行等信息
    public static final String URL_C2C_PAY_ACCOUTN_INFO = "index.php?s=trans&c=api&m=getUserinfo";

    //修改及添加银行等信息
    public static final String URL_C2C_PAY_ACCOUNT = "index.php?s=trans&c=api&m=updateUserinfo";

    //发布交易
    public static final String URL_C2C_RELEASE = "index.php?s=trans&c=api&m=creatTrans";

    //C2C钱包币种余额
    public static final String URL_C2C_COIN_BALANCE = "index.php?s=wallet&c=api&m=getWalletBlance";

    //获取发布信息
    public static final String URL_C2C_MY_RELEASE_LIST = "index.php?s=trans&c=api&m=getTrans";

    //更新发布状态
    public static final String URL_C2C_UPDATE_RELEASE_STATUS = "index.php?s=trans&c=api&m=updateTrans";

    //钱包
    public static final String URL_C2C_WALLET = "index.php?s=wallet&c=api&m=walletindex";

    //资产明细
    public static final String URL_C2C_WALLET_DETAIL_RECORD = "index.php?s=wallet&c=api&m=getTransactionDetails";

    //根据c2cCoinID获取资产钱包币种信息
    public static final String URL_C2C_ASSETS_BALANCE = "index.php?s=javaapi&c=transfer&m=getAssetsWallet";

    //资金划转
    public static final String URL_C2C_TRANSFER = "index.php?s=javaapi&c=transfer&m=index";

    //根据资产钱包币种id获取c2c钱包币种信息
    public static final String URL_C2C_BALANCE_BY_ASSETS_COIN_ID = "index.php?s=javaapi&c=transfer&m=getc2cWallet";
}
