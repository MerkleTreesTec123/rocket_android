package com.ifenduo.lib_base.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import java.util.Map;

public class AppTool {
    /**
     * 检查包是否存在
     *
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 调用系统浏览器打开网页或下载链接
     *
     * @param url 地址
     */
    public static void openLinkBySystem(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 根据包名打开三方APP
     *
     * @param context
     * @param packageName
     */
    public static void openAppByPackageName(Context context, String packageName, Map<String, String> param) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (param != null && param.size() > 0) {
                Bundle bundle = new Bundle();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    bundle.putString(entry.getKey(), entry.getValue());
                }
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
