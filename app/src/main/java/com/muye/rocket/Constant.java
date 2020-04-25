package com.muye.rocket;

public class Constant {
    /**********************发送不需要签名的短信********************/
    public static final String SEND_SMS_NOT_SIGNATURE_TYPE_REGISTER = "112";//手机端注册
    public static final String SEND_SMS_NOT_SIGNATURE_TYPE_BIND_PHONE = "102";//绑定手机

    public static String PAY_PASSWORD = "";

    /**********************C2C支付类型************************/
    //1、银行卡 2、支付宝 3、微信 4、银行卡支付宝 5、银行卡微信 6、支付宝微信 7、银行卡支付宝微信
    public static final String C2C_PAY_TYPE_BANK_CARD = "1";
    public static final String C2C_PAY_TYPE_ALIPAY = "2";
    public static final String C2C_PAY_TYPE_WECHAT = "3";
    public static final String C2C_PAY_TYPE_BANK_CARD_ALIPAY = "4";
    public static final String C2C_PAY_TYPE_BANK_CARD_WECHAT = "5";
    public static final String C2C_PAY_TYPE_ALIPAY_WECHAT = "6";
    public static final String C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT = "7";

    /**********************C2C交易类型************************/
    //1、买入 2卖出
    public static final String C2C_TRANSACTION_TYPE_BUY = "1";
    public static final String C2C_TRANSACTION_TYPE_SELL = "2";
    /**********************C2C划转类型************************/
    public static final String C2C_TRANSFER_ASSETS_TO_C2C = "1";//币币到c2c
    public static final String C2C_TRANSFER_C2C_TO_ASSETS = "2";//c2c到币币
    public static final String GESTURE_PASSWORD = "GesturePassword";//手势密码

    //黑客地址
    public static final String[] ARRAY_ADDRESS = {
            "0x63F98c1631404A4CF7F8a8d02C9DD2635cc18a09",
            "0x758EEec00ee39CD5455B57743bC1Fb7B9e0e7807",
            "0x34f50eD2b6e3bD12da05C039d1f0932f65De07b1"
    };
}
