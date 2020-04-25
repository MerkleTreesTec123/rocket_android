package com.muye.rocket.tools;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class StringTools {

    public static String phoneNumberFormat(String phone) {
        if (TextUtils.isEmpty(phone)) return "";
        if (phone.length() < 6) return phone;
        String endStr = "";
        String startStr = "";
        if (phone.length() % 2 == 0) {
            startStr = phone.substring(0, (phone.length() - 4) / 2);
            endStr = phone.substring(phone.length() - (phone.length() - 4) / 2, phone.length());
        } else {
            startStr = phone.substring(0, (phone.length() - 1 - 4) / 2);
            endStr = phone.substring(phone.length() - (phone.length() + 1 - 4) / 2, phone.length());
        }

        StringBuilder sb = new StringBuilder();
        sb.append(startStr).append("****").append(endStr);
        return sb.toString();
    }

    public static String getRandomString(int length) {
        //产生随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //循环length次
        for (int i = 0; i < length; i++) {
            //产生0-2个随机数，既与a-z，A-Z，0-9三种可能
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                //如果number产生的是数字0；
                case 0:
                    //产生A-Z的ASCII码
                    result = Math.round(Math.random() * 25 + 65);
                    //将ASCII码转换成字符
                    sb.append(String.valueOf((char) result));
                    break;
                case 1:
                    //产生a-z的ASCII码
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append(String.valueOf((char) result));
                    break;
                case 2:
                    //产生0-9的数字
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String sha1Encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatBankCardNo(String no) {
        if (TextUtils.isEmpty(no)) return "";
        if (no.length() >= 4) {
            return "**** **** **** " + no.substring(no.length() - 4, no.length());
        } else {
            return "**** **** **** " + no;
        }
    }

    public static String getBankCardEndNo(String no) {
        if (TextUtils.isEmpty(no)) return "";
        if (no.length() >= 4) {
            return no.substring(no.length() - 4, no.length());
        } else {
            return no;
        }
    }

    /**
     * num转等级名称
     * @param num
     * @return
     */
    public static String numToLevel(int num){
        if (num == 88){  //特等奖
            return "特等奖";
        }else if (num == 18){//一等奖
            return "一等奖";
        }else if (num == 8){   //二等奖
            return "二等奖";
        }else if (num == 2){ // 三等奖
            return "三等奖";
        }else if (num == 1) { // 四等奖
            return "四等奖";
        }else if (num == 0){// 五等奖
            return "谢谢参与";
        }
        return "--";
    }

    /**
     * num等级获取对应的cat数量
     * @param num
     * @return
     */
    public static String numToCatNumber(int num){
        if (num == 88){  //特等奖
            return "88";
        }else if (num == 1){//一等奖
            return "18";
        }else if (num == 2){   //二等奖
            return "8";
        }else if (num == 3){ // 三等奖
            return "2";
        }else if (num == 4) { // 四等奖
            return "1";
        }else if (num == 5){// 五等奖
            return "谢谢参与";
        }else if (num == 6) { // 六等级
            return "音响";
        }
        return "";
    }
}
