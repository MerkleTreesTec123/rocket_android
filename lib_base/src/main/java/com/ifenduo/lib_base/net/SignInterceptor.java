package com.ifenduo.lib_base.net;

import android.content.Context;

import com.ifenduo.lib_base.api.BaseURLConfig;
import com.ifenduo.lib_http.interceptor.BaseSignInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 签名拦截器
 */
public class SignInterceptor extends BaseSignInterceptor {
    public SignInterceptor(Context context) {
        super(context);
    }

    @Override
    protected Map<String, String> getSignParam() {
        return createSignParam();
    }

    public static Map<String, String> createSignParam() {
        Map<String, String> map = new HashMap<>();

//        long time = Calendar.getInstance().getTimeInMillis();
//        String secret = "vsVshxadEbr3YSZT";
//        String timeStr = String.valueOf(time);
//        String randomStr = StringTools.getRandomString(16);
//        String temp = randomStr + secret + timeStr;
//
//        map.put("timeStamp", timeStr);
//        map.put("randomStr", randomStr);
//        map.put("signature", StringTools.md5(StringTools.sha1Encode(temp)));
//
//        map.put("auth", BaseURLConfig.AUTH);
//        map.put("clienttype", "app");
        return map;
    }


}
