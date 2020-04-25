package com.muye.rocket.mvp.ieo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.event.NavIndex;
import com.muye.rocket.mvp.home.view.HomeActivity;
import com.muye.rocket.tools.SPUtils;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;


/**
 * WebView 和App端接口类
 */
public class WebViewInterFace {

    private Context context;
    private Activity activity;

    public WebViewInterFace(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    /**
     * 直接在网页中点击关必当前界面
     */
    @JavascriptInterface
    public void close(){
        // 点击了关闭之后，把本地存放的推送网页地址清空，并设置存储的是否点击为true已点击
        MMKV.defaultMMKV().encode("web_load_url","");
        SPUtils.savePushWebUrlClick(true);
        if (activity != null)
            activity.finish();
    }

    /**
     * 关必当前推送网页，然后跳转到HomeActivity 中指定的Fragment中
     * @param index
     */
    @JavascriptInterface
    public void beforeCloseOpenIndex(int index){
        if (activity != null)
            activity.finish();
        startHomeNavigation(index);
    }

    /**
     * 直接在网页中点击跳转到HomeActivity 中指定的Fragment中
     * @param index
     */
    @JavascriptInterface
    public void openHomeIndex(int index,int isClose){
        isCloseCurrent(isClose);
        startHomeNavigation(index);
    }

    private void startHomeNavigation(int index) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        EventBus.getDefault().post(new NavIndex(index));
    }

    /**
     * 提供js获取用户登录的全部信息
     * @return
     */
    @JavascriptInterface
    public String getUser(){
        return MMKVTools.getAllUserInfo();
    }

    /**
     * 获取token
     * @return
     */
    @JavascriptInterface
    public String getToken(){
        return MMKVTools.getToken();
    }

    /**
     * 获取uid
     * @return
     */
    @JavascriptInterface
    public String getuid(){
        return MMKVTools.getUID();
    }

    /**
     * 获取email
     * @return
     */
    @JavascriptInterface
    public String getEmail(){
        return MMKVTools.getEmail();
    }

    /**
     * 获取手机号
     * @return
     */
    @JavascriptInterface
    public String getPhone(){
        return MMKVTools.getPhone();
    }

    /**
     * 获取用户实名状态
     * 0 ：待审核
     * 2 ：已驳回
     * 10：已认证
     * 其它代表未认证
     * @return
     */
    @JavascriptInterface
    public String getRealNameStatus(){
        return MMKVTools.getRelNameStatus();
    }

    /**
     * 根据状态处理是否关必当前网页Activity
     * @param status
     */
    private void isCloseCurrent(int status){
        if (status == 1){
            if (activity != null)
                activity.finish();
        }
    }
}
