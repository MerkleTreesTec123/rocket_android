package com.ifenduo.common.tool;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by yangxuefeng on 16/10/30.
 */

public class IntentTool {

    public static Intent actionToCallPhone(String phoneNumber){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        return intent;
    }

    public static Intent actionToSMS(String phone,String message){
        Uri smsToUri = Uri.parse("smsto:"+phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", message);
        return intent;
    }

    public static Intent actionToCamera(String outputPath){
        return null;
    }

    public static Intent actionToCamera(){
        return null;
    }

    /**
     * 定位服务
     * @return
     */
    public static Intent actionToLocationSetting(){
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent actionToWireLessSetting(){
        return new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
    }
}
