package com.ifenduo.common.tool;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by ll on 2017/8/6.
 */

public class UrlUtils {
    private final static String ENCODE = "UTF-8";

    /**
     * 解码
     * @param str
     * @return
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 转码
     * @param str
     * @return
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
