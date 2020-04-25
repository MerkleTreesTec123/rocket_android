package com.ifenduo.lib_base.tools;


import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;

public class NumberUtil {
    public static final int MONEY_DECIMAL_LENGTH = 4;//金额小数点后保留长度

    public static String setNumberW(int number) {
        if (number > 10000) {
            double d = number / 10000f;
            return String.format("%.1f", d) + "w";
        }
        return number + "";
    }

    /**
     * 格式化人民币
     *
     * @param money
     * @return
     */
    public static String formatRMB(String money) {
        return formatMoney(money, 2);
    }

    /**
     * 格式化人民币
     *
     * @param money
     * @return
     */
    public static String formatRMB(double money) {
        return formatMoney(money, 2);
    }

    /**
     * 格式化人民币
     *
     * @param money
     * @return
     */
    public static String formatRMBDown(String money) {
        return formatMoneyDown(money, 2);
    }

    /**
     * 格式化人民币
     *
     * @param money
     * @return
     */
    public static String formatRMBDown(double money) {
        return formatMoneyDown(money, 2);
    }

    /**
     * 格式化金额
     *
     * @param money
     * @return
     */
    public static String formatMoney(double money) {
        return formatMoney(money, MONEY_DECIMAL_LENGTH);
    }

    /**
     * 格式化金额
     *
     * @param money
     * @return
     */
    public static String formatMoney(String money) {
        return formatMoney(money, MONEY_DECIMAL_LENGTH);
    }

    /**
     * 格式化金额
     *
     * @param money
     * @return
     */
    public static String formatMoney(double money, @IntRange(from = 0) int scale) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(money));
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 格式化金额
     *
     * @param money
     * @return
     */
    public static String formatMoney(String money, @IntRange(from = 0) int scale) {
        if (TextUtils.isEmpty(money)) {
            money = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(money);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 格式化金额向下取整
     *
     * @param money
     * @return
     */
    public static String formatMoneyDown(double money) {
        return formatMoneyDown(money, MONEY_DECIMAL_LENGTH);
    }

    /**
     * 格式化金额向下取整
     *
     * @param money
     * @return
     */
    public static String formatMoneyDown(String money) {
        return formatMoneyDown(money, MONEY_DECIMAL_LENGTH);
    }

    /**
     * 格式化金额向下取整
     *
     * @return
     */
    public static String formatMoneyDown(double money, @IntRange(from = 0) int scale) {
        return formatMoneyDown(String.valueOf(money), scale);
    }

    /**
     * 格式化金额向下取整
     *
     * @param money
     * @return
     */
    public static String formatMoneyDown(String money, @IntRange(from = 0) int scale) {
        Log.d("TAG", "formatMoneyDown: " + money);
        if (TextUtils.isEmpty(money)) {
            money = "0.000000";
        }
        money = new BigDecimal(money).stripTrailingZeros().toPlainString();
        String start = "";
        String end = "";
        if (money.contains(".")) {
            String[] strArr = money.split("\\.");
            if (strArr.length == 2) {
                start = strArr[0];
                end = strArr[1];
            } else if (strArr.length == 1) {
                start = strArr[0];
            } else {
                throw new NumberFormatException("格式化金额向下取整,传输的值格式不正确:" + money);
            }
        } else {
            start = money;
        }

        StringBuilder builder = new StringBuilder();
        if (TextUtils.isEmpty(start)) {
            builder.append("0");
        } else {
            builder.append(start);
        }

        if (scale > 0) {
            builder.append(".");
            for (int i = 0; i < scale; i++) {
                if (TextUtils.isEmpty(end)) {
                    builder.append("0");
                } else if (i < end.length()) {
                    builder.append(end.charAt(i));
                } else {
                    builder.append("0");
                }
            }
        }

        return builder.toString();
    }

    public static double string2Double(String number) {
        return new BigDecimal(TextUtils.isEmpty(number) ? "0" : number).doubleValue();
    }
}
