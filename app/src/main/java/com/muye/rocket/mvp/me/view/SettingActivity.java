package com.muye.rocket.mvp.me.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.LanguageUtil;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.APKVersionCodeUtils;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.tools.DataCleanManager;
import com.muye.rocket.widget.dialog.MessageDialog;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import butterknife.BindView;
import butterknife.OnClick;

/*
* 更多设置
* */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.language_text_view)
    TextView languageTextView;
    @BindView(R.id.cache_text_view)
    TextView cacheTextView;
    @BindView(R.id.version_text_view)
    TextView versionTextView;
    private MessageDialog mLoginOutDialog;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);

        setNavigationCenter(getString(R.string.setting));

        String language = MMKVTools.getLanguage();
        languageTextView.setText(LanguageUtil.ENGLISH.equals(language) ? R.string.english:R.string.chinese);
        try {
            cacheTextView.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        versionTextView.setText("v" + APKVersionCodeUtils.getVerName(this));
    }

    @OnClick({R.id.language_container, R.id.feedback_container, R.id.submit_button,R.id.clear_cache,R.id.version_container, R.id.about_container})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.language_container://语言切换
                intent = new Intent(this, LanguageActivity.class);
                startActivity(intent);
                break;
            case R.id.feedback_container://反馈
                break;
            case R.id.submit_button://退出账户
                showMessageDialog();
                break;
            case R.id.clear_cache://清除缓存
                showLoginOutDialog();
                break;
            case R.id.about_container://关于我们
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.version_container://检查更新
                UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
                if (upgradeInfo != null && upgradeInfo.versionCode > APKVersionCodeUtils.getVersionCode(this)) {
                    Beta.checkUpgrade(true, true);
                } else {
                    showToast(getString(R.string.already_the_latest_version));
                }
                break;
        }
    }

    private void showMessageDialog() {
        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.setMessage(getString(R.string.loginout_hint))
                .setCancelColor(getResources().getColor(R.color.color8488F5))
                .setOkColor(getResources().getColor(R.color.color8488F5))
                .setOkText(getString(R.string.ok))
                .setCancelText(getString(R.string.cancel))
                .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                    @Override
                    public void onOkButtonClick(MessageDialog dialog) {
                        dialog.dismiss();
                        loginOut();
                    }
                })
                .show();
    }

    private void showLoginOutDialog() {
        if (mLoginOutDialog == null) {
            mLoginOutDialog = new MessageDialog(this)
                    .setCancelText(getString(R.string.cancel))
                    .setOkText(getString(R.string.ok))
                    .setMessage(getString(R.string.clear_cache))
                    .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                        @Override
                        public void onOkButtonClick(MessageDialog dialog) {
                            dialog.dismiss();
                            DataCleanManager.clearAllCache(SettingActivity.this);
                            try {
                                cacheTextView.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        if (!mLoginOutDialog.isShowing()) {
            mLoginOutDialog.show();
        }
    }

}
