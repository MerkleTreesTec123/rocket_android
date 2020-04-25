package com.ifenduo.lib_http.interceptor;


import android.content.Context;

import com.ifenduo.lib_http.APKVersionCodeUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 签名拦截器
 */
public abstract class BaseSignInterceptor implements Interceptor {
    private static final String TAG = "SignInterceptor";
    private Context mContext;

    public BaseSignInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        long time = Calendar.getInstance().getTimeInMillis();

        //获取到request
        Request request = chain.request();
        HttpUrl httpUrl = request.url();
        HttpUrl.Builder builder = httpUrl.newBuilder();
        //公告参数
//        builder.addQueryParameter("systemversion", APKVersionCodeUtils.getVerName(mContext));
//        builder.addQueryParameter("systemostype", "android");
        //签名参数
        Map<String, String> signParam = getSignParam();
        if (signParam != null && signParam.size() > 0) {
            for (Map.Entry<String, String> entry : signParam.entrySet()) {
                if (httpUrl.queryParameter(entry.getKey()) == null) {
                    builder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        return chain.proceed(request.newBuilder().url(builder.build()).build());
    }

    /**
     * 获取签名参数
     *
     * @return
     */
    protected abstract Map<String, String> getSignParam();
}
