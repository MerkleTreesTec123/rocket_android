package com.luck.picture.lib.tools;

import android.Manifest;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.widget
 * email：893855882@qq.com
 * data：2017/3/21
 */

public class Constant {
    public final static String ACTION_AC_FINISH = "app.activity.finish";

    public final static String ACTION_AC_REFRESH_DATA = "app.action.refresh.data";

    public final static String ACTION_CROP_DATA = "app.action.crop_data";

    public final static String ACTION_AC_SINGE_UCROP = "app.activity.singe.ucrop.finish";

    // SD卡写入权限 Flag
    public static final int WRITE_EXTERNAL_STORAGE = 0x01;

    //权限
    public static final int REQUEST_CAMERA_PERMISSION = 1027;
    public static final String[] CAMERA_AND_CONTACTS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final String[] CAMERA_AND_STORAGE = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //C2C 购买 / 出售 类型
    public static final String BUY_OR_SELL="buy_or_sell";
}
