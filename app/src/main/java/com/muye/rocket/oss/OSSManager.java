package com.muye.rocket.oss;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;

public class OSSManager {

    private static OSS oss;
    public static String OSS_BUCKET_NAME = "rocket-static";
    public static final String OSS_END_POINT = "https://oss-ap-southeast-1.aliyuncs.com";
    public static final String OSS_FILE_PATH="rocketcoin/upload/args/";
    public static final String OSS_OUT_URL="https://rocket-static.oss-ap-southeast-1.aliyuncs.com";
    public static final String WITHDRAW_TOP = "http://static.irocket.io/topic/images/top_tx.png";

    public static void init(Context context) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAItw6nqcPILaPI", "rdGiTs4WssbDiUSjbwzX0BEIP3ptot");
        oss = new OSSClient(context.getApplicationContext(), OSS_END_POINT, credentialProvider);
    }

    public static OSS getOss() {
        return oss;
    }
}
