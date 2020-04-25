package com.muye.rocket.mvp.me.view;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.ifenduo.common.view.CircleImageView;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.tools.StringTools;

import butterknife.BindView;

public class PersonalActivity extends BaseActivity {
    @BindView(R.id.avatar_image_view)
    CircleImageView avatarImageView;
    @BindView(R.id.nick_text_view)
    TextView nickTextView;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.personal_data));
        nickTextView.setText(StringTools.phoneNumberFormat(MMKVTools.getPhone()));
    }

}
