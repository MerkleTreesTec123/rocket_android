package com.muye.rocket.tools;


import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ll on 2018/4/3.
 */

public class Validator {

    /**
     * 正则表达式:验证用户名(不包含中文和特殊字符)如果用户名使用手机号码或邮箱 则结合手机号验证和邮箱验证
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式:验证密码(不包含特殊字符)
     */
//    public static final String REGEX_PASSWORD = "^[a-zA-Z][a-zA-Z0-9\\W]{5,15}$";
//    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9\\W]{6,12}$";
    //   public static final String REGEX_PASSWORD = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\\\W]+$)(?![A-Za-z\\\\W]+$)(?![A-Z0-9\\\\W]+$)[a-zA-Z0-9\\\\W]{6,16}$";//必须是包含大写字母、小写字母、数字、特殊符号
//    public static final String REGEX_PASSWORD = "^(?![A-Z]+$)(?![a-z]+$)(?![0-9]+$)(?![a-zA-Z\\W]+$)[a-zA-Z0-9\\W]{6,20}$";//必须是数字+字符
    public static final String REGEX_PASSWORD = "^(?![A-Z]+$)(?![a-z]+$)(?![0-9]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[a-zA-Z0-9\\W]{6,50}$";//必须包含数字和字母

    /**
     * 正则表达式:验证手机号
     */
//    public static final String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
//    public static final String REGEX_MOBILE = "^(0|86|17951)?(1)[0-9]{10}$";
    public static final String REGEX_MOBILE = "^[0-9]{4,20}$";

    /**
     * 正则表达式:验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式:验证汉字(1-9个汉字)  {1,9} 自定义区间
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5]{1,9}$";

    /**
     * 正则表达式:验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|[Xx])$)";

    /**
     * 正则表达式:验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式:验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";

    /**
     * 正则表达式:币种地址(不包含特殊字符)
     */
    public static final String REGEX_COIN_ADDRESS = "^[a-zA-Z0-9]{6,60}$";

    //包含特殊符号
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
    // 以空字符串开头或结尾
    public static boolean isNullStartAndEnd(String str){
        if (str.startsWith(" ")){
            return false;
        }
        if (str.endsWith(" ")){
            return false;
        }
        return true;
    }
    // 全局存储一个值，判断是否弹出实名认证
    public static int IS_REALNAME ;

    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUserName(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIPAddress(String ipAddress) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddress);
    }

    /**
     * 校验币种地址
     *
     * @param address
     * @return
     */
    public static boolean isCoinAddress(String address) {
        return Pattern.matches(REGEX_COIN_ADDRESS, address);
    }

    public static final String REGEX_PASSWORD_LEVEL_3 = "^(?![A-Z]+$)(?![a-z]+$)(?![0-9]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)(?![0-9a-zA-Z]+$)[a-zA-Z0-9\\W]{6,10000}";
    public static final String REGEX_PASSWORD_LEVEL_2 = "^(?![A-Z]+$)(?![a-z]+$)(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,10000}";
    public static int getPasswordLevel(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            return 0;
        }
        if (Pattern.matches(REGEX_PASSWORD_LEVEL_3, password)) {
            return 3;
        }
        if (Pattern.matches(REGEX_PASSWORD_LEVEL_2, password)) {
            return 2;
        }
        return 1;
    }
}
