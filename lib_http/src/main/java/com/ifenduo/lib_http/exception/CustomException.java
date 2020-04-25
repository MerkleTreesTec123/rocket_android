package com.ifenduo.lib_http.exception;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

public class CustomException {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 请求超时
     */
    public static final int REQUEST_TIMEOUT = 1003;

    /**
     * 网络连接超时
     */
    public static final int CONNECT_TIMEOUT = 1004;

    /**
     * 安全证书异常
     */
    public static final int SSL_ERROR = 1005;

    /**
     * 网络异常
     */
    public static final int NETWORK_ANOMALY = 1006;

    /**
     * 域名解析失败
     */
    public static final int HOST_ERROR = 1007;

    /**
     * 协议错误
     */
    public static final int HTTP_ERROR = 1003;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException || e instanceof java.text.ParseException) {
            //解析错误
            ex = new ApiException(PARSE_ERROR, "数据解析错误");
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            //请求超时， 连接服务器成功，服务器响应时间过长
            ex = new ApiException(REQUEST_TIMEOUT, "系统升级中...");
            return ex;
        } else if (e instanceof ConnectException) {
            //网络连接超时，未连接服务器
            ex = new ApiException(CONNECT_TIMEOUT, "系统升级中...");
            return ex;
        } else if (e instanceof SSLHandshakeException) {
            //安全证书异常
            ex = new ApiException(SSL_ERROR, "安全证书异常");
            return ex;
        } else if (e instanceof HttpException) {
            int code = ((HttpException) e).code();
            if (code == 504) {
                //网络异常，请检查您的网络状态
                ex = new ApiException(SSL_ERROR, "网络异常，504");
                return ex;
            } else if (code == 404) {
                ex = new ApiException(NETWORK_ERROR, "网络连接错误");
                return ex;
            } else {
                ex = new ApiException(NETWORK_ERROR, "网络异常，请检查网络状态:" + code);
                return ex;
            }
        } else if (e instanceof UnknownHostException) {
            //域名解析失败
            ex = new ApiException(HOST_ERROR, "连接服务器失败");
            return ex;
        } else if (e instanceof ApiException) {
            return (ApiException) e;
        } else {
            //未知错误
            ex = new ApiException(UNKNOWN, "网络错误");
            return ex;
        }
    }
}
