package com.muye.rocket.mvp.me.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ifenduo.lib_base.tools.LanguageUtil;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.mvp.home.view.HomeActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageActivity extends BaseActivity {

    @BindView(R.id.chinese_image_view)
    ImageView chineseImageView;
    @BindView(R.id.english_image_view)
    ImageView englishImageView;


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.language));
        bindCheckedLanguage();
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @OnClick({R.id.chinese_container, R.id.english_container})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.chinese_container:
                if (LanguageUtil.SIMPLIFIED_CHINESE.equals(MMKVTools.getLanguage())) return;
                MMKVTools.saveLanguage(LanguageUtil.SIMPLIFIED_CHINESE);
                bindCheckedLanguage();
                intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.english_container:
                if (LanguageUtil.ENGLISH.equals(MMKVTools.getLanguage())) return;
                MMKVTools.saveLanguage(LanguageUtil.ENGLISH);
                bindCheckedLanguage();
                intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void bindCheckedLanguage() {
        String language = MMKVTools.getLanguage();
        chineseImageView.setVisibility(LanguageUtil.SIMPLIFIED_CHINESE.equals(language) ? View.VISIBLE : View.GONE);
        englishImageView.setVisibility(LanguageUtil.ENGLISH.equals(language) ? View.VISIBLE : View.GONE);
//        japaneseImageView.setVisibility(LanguageUtil.JAPANESE.equals(language) ? View.VISIBLE : View.GONE);

    }

}
