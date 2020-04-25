package com.muye.rocket.reveive;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.joooonho.SelectableRoundedImageView;
import com.muye.rocket.R;
import com.muye.rocket.mvp.WebBrowser;
import com.muye.rocket.tools.GlideLoadUtils;
import com.muye.rocket.tools.SPUtils;
import com.tencent.mmkv.MMKV;

public class PushInfoDataActivity extends Activity {

    SelectableRoundedImageView pushImg;
    ImageView closeTx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_info_dialog_activity);
        pushImg = findViewById(R.id.push_img);
        closeTx = findViewById(R.id.closeImg);

        String pushImgUrl = MMKV.defaultMMKV().decodeString("pushImgUrl", "");
        String pushAddress = MMKV.defaultMMKV().decodeString("push_address_url","");
        String pushTitle   = MMKV.defaultMMKV().decodeString("push_title","");
        GlideLoadUtils.getInstance().glideLoad(this,pushImgUrl,pushImg);
        pushImg.setOnClickListener(v -> {
            WebBrowser.openWebBrowser(PushInfoDataActivity.this, pushAddress, pushTitle);
            SPUtils.savePushNotificClick(true);
            MMKV.defaultMMKV().encode("push_address_url","");
            finish();
        });
        closeTx.setOnClickListener(v -> {
            SPUtils.savePushNotificClick(true);
            MMKV.defaultMMKV().encode("push_address_url","");
            finish();
        });
    }

}
