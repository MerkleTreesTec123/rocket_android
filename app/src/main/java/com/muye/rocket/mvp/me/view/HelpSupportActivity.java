package com.muye.rocket.mvp.me.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.muye.rocket.R;
import com.muye.rocket.api.RURLConfig;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.mvp.WebBrowser;

import butterknife.OnClick;

/*
* 帮助与支持
* */
public class HelpSupportActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_help_support;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.help_and_support));
    }

    @OnClick({R.id.help_center_container, R.id.service_agreement_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.help_center_container:
                WebBrowser.openWebBrowser(this, RURLConfig.URL_PAGE_HELP_CENTER, getString(R.string.help_center));
                break;
            case R.id.service_agreement_container:
                WebBrowser.openWebBrowser(this, RURLConfig.URL_PAGE_SERVICE_AGREEMENT, getString(R.string.service_agreement));
                break;
        }
    }
}
