package com.muye.rocket.mvp.me.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.muye.rocket.R;
import com.muye.rocket.api.RURLConfig;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.mvp.WebBrowser;

import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.about_us));
    }

    @OnClick({R.id.privacy_policy_container, R.id.ahout_rocket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.privacy_policy_container:
                WebBrowser.openWebBrowser(this, RURLConfig.URL_PAGE_PRIVACY_POLICY, getString(R.string.privacy_policy));
                break;
            case R.id.ahout_rocket:
//                Intent intent = new Intent(this,RocketProfileActivity.class);
//                startActivity(intent);
                WebBrowser.openWebBrowser(this, RURLConfig.URL_PAGE_ADVERT_ROCKET, getString(R.string.rocket_));
                break;
        }
    }
}
