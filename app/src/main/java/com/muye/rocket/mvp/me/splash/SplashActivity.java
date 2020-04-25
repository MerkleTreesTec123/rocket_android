package com.muye.rocket.mvp.me.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.mvp.account.view.LoginActivity;
import com.muye.rocket.mvp.home.view.HomeActivity;

import java.lang.ref.WeakReference;

public class SplashActivity extends BaseActivity implements SplashContract.View {
    MyHandler mHandler;
    private SplashPresenter splashPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        mHandler = new MyHandler(this);

        getOpenWs();
        mHandler.sendEmptyMessageDelayed(1, 2000);
        // 设置是否开启交易功能
        MMKVTools.setOpenTrade(false);
    }

    /*
    * 请求是否开启长连接
    * */
    private void getOpenWs() {
        if (splashPresenter == null){
            splashPresenter = new SplashPresenter(this);
        }
        splashPresenter.getOpenWs();
    }

    public void openPage() {
        String phone = !TextUtils.isEmpty(MMKVTools.getPhone())?MMKVTools.getPhone():MMKVTools.getEmail();
        String password = MMKVTools.getLoginPassword();
        String token = MMKVTools.getToken();

        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(token)) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }

    @Override
    public void saveCode(int code) {
        //开启长连接
        MMKVTools.saveOpenWs(code);
    }

    static class MyHandler extends Handler {
        WeakReference<SplashActivity> weakReference;

        public MyHandler(SplashActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && weakReference.get() != null) {
                weakReference.get().openPage();
            }
        }
    }

}
