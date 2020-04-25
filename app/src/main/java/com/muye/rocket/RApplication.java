package com.muye.rocket;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.dhh.websocket.Config;
import com.dhh.websocket.RxWebSocket;
import com.hjq.toast.ToastUtils;
import com.ifenduo.lib_base.net.RetrofitServiceManager;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.oss.OSSManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

public class RApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Bugly.init(getApplicationContext(), "1f1777c248", false);

        new Thread(() -> {
            // 推送
            JPushInterface.setDebugMode(true);
            JPushInterface.init(this);
            UMConfigure.init(this, "5db2c68c0cafb207b0000b6e", "FIR_CHANNEL", UMConfigure.DEVICE_TYPE_PHONE, null);
            // 选用AUTO页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        }).start();

        RetrofitServiceManager.initialize(this);
        OSSManager.init(this);
        //保存数据工具类
        MMKVTools.init(this);
        ToastUtils.init(this);
        initRxWebSocket();

        handleSSLHandshake();
    }

    /*
    *
    * */
    private void initRxWebSocket() {
        //init config 在使用RxWebSocket之前设置即可，推荐在application里初始化
        Config config = new Config.Builder()
                .setShowLog(true)           //show  log
                .setClient(new OkHttpClient.Builder()
                        .pingInterval(3, TimeUnit.SECONDS) // 设置心跳间隔，这个是3秒检测一次
                        .build())   //if you want to set your okhttpClient
                .setReconnectInterval(2, TimeUnit.SECONDS)  //set reconnect interval
                .build();
        RxWebSocket.setConfig(config);
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();
    }

    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}
