package com.muye.rocket.mvp.me.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.widget.dialog.BigImgDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class RealFailReasonActivity extends BaseActivity {
    @BindView(R.id.photoImg)
    ImageView photoImg;
    @BindView(R.id.readImg)
    ImageView readImg;
    @BindView(R.id.errorImg)
    ImageView errorImg;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fail_realname_layout;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.real_name_guide));

    }

    @OnClick({R.id.errorImg, R.id.readImg, R.id.photoImg})
    public void onViewClicked(View view) {
        BigImgDialog dialog = new BigImgDialog(this);
        switch (view.getId()) {
            case R.id.errorImg:
                dialog.setImg(1);
                if (!dialog.isShowing())
                dialog.show();
                break;
            case R.id.readImg:
                dialog.setImg(2);
                if (!dialog.isShowing())
                dialog.show();
                break;
            case R.id.photoImg:
                dialog.setImg(3);
                if (!dialog.isShowing())
                dialog.show();
                break;

        }
    }
}
