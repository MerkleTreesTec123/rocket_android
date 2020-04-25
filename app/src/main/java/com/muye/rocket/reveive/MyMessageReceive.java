package com.muye.rocket.reveive;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.muye.rocket.tools.SPUtils;
import com.tencent.mmkv.MMKV;
import org.json.JSONException;
import org.json.JSONObject;
import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;
//推送消息接收器
public class MyMessageReceive  extends JPushMessageReceiver {

    private static final String TAG = "MyRocketReceive";

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG,"[onMessage] "+customMessage);
        // 保存MMKV

        if (null != customMessage.extra){
            Log.e(TAG,"[onMessage] "+customMessage.extra);
            try {
                JSONObject object = new JSONObject(customMessage.extra);
                if (object.has("pushType")){
                    int type = object.getInt("pushType");
                    // type 等于 1 ：弹框，  2 ： 网页
                    if (type == 1){
                        if (object.has("imgUrl")){
                            String url = object.getString("imgUrl");
                            MMKV.defaultMMKV().encode("pushImgUrl", url);
                            String addressUrl = object.getString("pushAddressUrl");
                            String urlTitle   = object.getString("pushTitle");
                            MMKV.defaultMMKV().encode("push_address_url",addressUrl);
                            MMKV.defaultMMKV().encode("push_title",urlTitle);
                            openNotificMessage(context);// 跳转弹框界面
                        }
                    }else {
                        if (object.has("webLoadUrl")){
                            String webUrl = object.getString("webLoadUrl");
                            MMKV.defaultMMKV().encode("web_load_url",webUrl);
                            openPushWebInfo(context);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开弹框样式的activity
     * @param context
     */
    private void openNotificMessage(Context context) {
        Intent intent=new Intent(context, PushInfoDataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void openPushWebInfo(Context context) {
        Intent intent=new Intent(context, PushInfoAddressActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        Log.e(TAG,"[onNotifyMessageOpened] "+notificationMessage);
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
    }


    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.e(TAG,"[onNotifyMessageArrived] "+message);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG,"[onNotifyMessageDismiss] "+message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG,"[onRegister] ");
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG,"[onConnected] "+isConnected + " , registrationId: " + JPushInterface.getRegistrationID(context));
        // 连接成功之后，获取registrationId
        SPUtils.setRegistId(JPushInterface.getRegistrationID(context));
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG,"[onCommandResult] "+cmdMessage);
    }

}
