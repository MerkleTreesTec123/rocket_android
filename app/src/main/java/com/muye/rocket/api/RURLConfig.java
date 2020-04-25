package com.muye.rocket.api;

import com.ifenduo.lib_base.api.BaseURLConfig;

public class RURLConfig {
/*
*  setting.setConnectUrl("ws://161.117.226.171:23460");//必填
        setting.setConnectUrl("ws://www.cbmedia.info:23460");//必填
//        setting.setConnectUrl("ws://161.117.194.251:23460");//必填正式服
//        setting.setConnectUrl("ws://161.117.186.169:23460");//必填
* */
    public static final String WS_BASE_URL = "ws://161.117.194.251:23460";

    //帮助中心
    public static final String URL_PAGE_HELP_CENTER = BaseURLConfig.BASE_URL + "/index.php?s=javaapi&c=api&m=problemDetail&id=1";
    //服务协议
    public static final String URL_PAGE_SERVICE_AGREEMENT = BaseURLConfig.BASE_URL + "/index.php?s=javaapi&c=api&m=problemDetail&id=7";
    //隐私条款
    public static final String URL_PAGE_PRIVACY_POLICY = BaseURLConfig.BASE_URL + "/index.php?s=javaapi&c=api&m=problemDetail&id=13";
    //Rocket简介
    public static final String URL_PAGE_ADVERT_ROCKET = BaseURLConfig.BASE_URL + "/index.php?s=javaapi&c=api&m=problemDetail&id=5";

    //绑定哥伦布账户须知
    public static final String BIND_CAT_KNOW = "https://rocket-static.oss-ap-southeast-1.aliyuncs.com/topic/irocket-tips.html";
    //
    public static final String LOTTERY_RULE = "http://static.irocket.io/topic/act11/tips.html";

    /**
     * 发送不需要签名的短信 POST (非签名)
     */
    public static final String URL_SEND_PHONE_MESSAGE_NOT_SIGNTRUE = "/v1/validate/send.html";
    /**
     * 发送邮箱验证码
     */
    public static final String URL_SEND_EMAIL = "/index.php?s=javaapi&c=Api&m=send_email";
    /**
     * 校验验证码
     */
    public static final String CODE_IS_OK = "/index.php?s=javaapi&c=Api&m=check_email";
    /**
     * 手机注册
     */
    public static final String URL_PHONE_REGISTER = "/index.php?s=javaapi&c=api&m=register";
    /**
     * 邮箱注册
     */
    public static final String URL_EMAIL_REGIST = "/index.php?s=javaapi&c=Api&m=email_register";
    /**
     * 登录
     * /index.php?s=javaapi&c=userApi&m=logins
     * /v1/user/login.html
     */
    public static final String URL_LOGIN = "/v1/user/login.html";
    /**
     * Banner
     */
    public static final String URL_BANNER = "/index.php?s=javaapi&c=api&m=get_new_banner";
    /**
     * adBanner
     */
    public static final String URL_AD_BANNER = "/index.php?s=javaapi&c=api&m=get_img";

    /**
     * Banner详情
     */
    public static final String URL_BANNER_DETAIL = BaseURLConfig.TEST_BASE_URL + "/index.php?s=javaapi&c=api&m=bannerDetail&id=";
    /**
     * 公告列表
     */
    public static final String URL_MARQUEE = "/index.php?s=javaapi&c=api&m=notice";
    /**
     * 获取安全设置详情
     */
    public static final String URL_SAFE_CENTER_INFO = "/v1/user/security.html";
    /**
     * google验证码关联设备
     */
    public static final String URL_GOOGLE_DEVICE = "/v1/user/google_device.html";
    /**
     * 绑定google验证码
     */
    public static final String URL_BIND_GOOGLE_CODE = "/v1/user/google_auth.html";
    /**
     * 实名认证
     */
    public static final String URL_SUBMIT_REL_NAME = "/v1/user/real_auth.html";

    /**
     * 自动识别
     */
    public static final String URL_SUBMIT_REL_ZG = "index.php?s=auth&c=Api&m=rocket_auth";
    /**
     * 修改/绑定，登录和交易密码
     */
    public static final String URL_SUBMIT_PASSWORD = "/v1/user/password.html";
    /**
     * 验证短息验证码
     */
    public static final String URL_CHECK_SMS_CODE = "/index.php?s=javaapi&c=api&m=checkSmsCode";
    /**
     * 找回交易密码接口
     */
    public static final String URL_FIND_TRANS_PASSWORD = "/v1/user/findtradepwd.html";
    /**
     * 找回登录密码
     */
    public static final String URL_FIND_LOGIN_PASSWORD = "/v1/user/resetpwd.html";
    /**
     * 邀请榜
     */
    public static final String URL_INVITE_RANKING = "/index.php?s=ieo&c=api&m=inviteBulletin";
    /**
     * 个人直推记录
     */
    public static final String URL_INVITE_RECORD = "/index.php?s=ieo&c=api&m=myInviteList";
    /**
     * 累计收益详情
     */
    public static final String URL_INVITE_REWARD_DETAIL = "/index.php?s=ieo&c=api&m=rewardDetail";
    /**
     * 发现Banner
     */
    public static final String URL_DISCOVER_BANNER = "/index.php?s=faxian&c=api&m=findBanner";
    /**
     * 发现推荐
     */
    public static final String URL_DISCOVER_RECOMMEND = "/index.php?s=faxian&c=api&m=findflag";
    /**
     * 发现列表
     */
    public static final String URL_DISCOVER_LIST = "/index.php?s=faxian&c=api&m=findList";
    /**
     * 发现内容
     */
    public static final String URL_DISCOVER_CONTENT = "/index.php?s=faxian&c=api&m=findContent";
    /**
     * 发现分类
     */
    public static final String URL_DISCOVER_CAT = "/index.php?s=faxian&c=api&m=findCat";
    /**
     * IEO首页数据
     */
    public static final String URL_IEO_HOME = "/index.php?s=ieo&c=api&m=par_amount";
    /**
     * 购买IEO
     */
    public static final String URL_IEO_BUY = "/index.php?s=ieo&c=api&m=buy";
    /**
     * IEO兑换进度
     */
    public static final String URL_IEO_PROGRESS = "/index.php?s=ieo&c=api&m=currentRoundBought";
    /**
     * 释放量
     */
    public static final String URL_IEO_RELEASE = "/index.php?s=ieo&c=api&m=alreadyRelease";
    /**
     * 我的IEO
     */
    public static final String URL_IEO_RECORD = "/index.php?s=ieo&c=api&m=myLogs";
    /**
     * 我的IEO详情
     */
    public static final String URL_IEO_RECORD_DETAIL = "/index.php?s=ieo&c=api&m=logInfo";
    /**
     * 服务器时间
     */
    public static final String URL_SYSTEM_TIME="index.php?s=appapi&c=api&m=getServerTime";
    /**
     * 图片上传
     */
    public static final String URL_UPLOAD_IMAGE = "index.php?s=appapi&c=api&m=upload";

    /**
     * 发送绑定手机号
     */
    public static final String SEND_BIND_PHONE = "/index.php?s=javaapi&c=Api&m=send_sms";

    /**
     * 绑定手机号接口
     */
    public static final String BIND_PHONE = "/index.php?s=javaapi&c=Api&m=bind_phone";

    /**
     *发送邮箱验证码java
     */
    public static final String URL_SEND_EMAIL_JAVA = "/v1/email/send.html";
    /**
     * 绑定邮箱接口
     */
    public static final String BIND_EMAIL = "/v1/email/add.html";

    /**
     * 获取钱包地址
     */
    public static final String QR_CODE_BINDE_WALLET = "api/get_addr";

    /**
     * 查询绑定cat钱包的列表
     */
    public static final String BIND_CAT_LIST = "api/cat_list";

    /**
     * 查询cat余额
     */
    public static final String REMAIN_INQUIRE = "api/get_cat";
    /**
     * 我的抽奖等级 api/getuserinfo
     */
    public static final String MY_LOTTERY_RESULT = "api/getuserinfo";

    /**
     * 中奖人展示
     */
    public static final String LOTTERY_RECORD = "api/getusers";

    /**
     * 是否可以抽奖
     */
    public static final String IS_LOOTERY = "api/raffle";

    /**
     * 我的中奖纪录
     */
    public static final String MY_WIN_RECORD = "api/meresult";

    /**
     * 我的邀请记录
     */
    public static final String MY_INVITER_RECORD = "api/getinvite";

    /**
     * 是否可以抽宝箱
     */
    public static final String CAN_LOTTERY = "/api/getcstatus";

    /**
     * 底部导航中间的界面url地址
     */
    public static final String SPACE_PLAN_URL = "index.php?s=javaapi&c=Api&m=rocket_html";

    /**
     * 免费提币到交易所
     */
    public static final String  REFLECT_URL = "/api/cat_pay";

    /**
     * 免费体现输入密码
     */
    public static final String REFECT_FREE_PASSWORD = "/api/check_password";

    /**
     * 驳回原因
     */
    public static final String REASON = "/index.php?s=javaapi&c=api&m=auth_reason";

    /**
     * 控制用户是否可以显示交易测试数据
     */
    public static final String MY_TEST_SALE = "index.php?s=javaapi&c=Api&m=rocket_index";

    /**
     * 修改支付密码验证手机号
     */
    public static final String UPDATE_PAY_PASSWORD = "index.php?s=javaapi&c=Api&m=paysms";


    /**
     * 提币到哥伦布钱包
     */
    public static final String WITH_DRAW_CAT = "/api/rocket_pay";

    /**
     * 查询哥伦布钱包余额
     */
    public static final String SEAECH_ROCKET_BALANCE = "/api/get_total";

    /**
     * 保存用户额推送id
     */
    public static final String SAVA_USER_PUSHID = "/index.php?s=javaapi&c=Api&m=get_jg";

    /**
     * 是否开启长连接
     */
    public static final String GET_OPEN_WS = "/api/ps";
}
