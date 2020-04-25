package com.muye.rocket.mvp.me.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ColumnFreeActivity extends BaseActivity {

    @BindView(R.id.in_img)
    ImageView inImg;
    @BindView(R.id.out_img)
    ImageView outImg;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.column_free_lauout;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.free_column_title));

    }

    @OnClick({R.id.in_img, R.id.out_img})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.in_img:
                Intent intentIn = new Intent(this, MyBindWalletListsActivity.class);
                intentIn.putExtra("intent_type","1");
                startActivity(intentIn);
                break;
            case R.id.out_img:
                Intent intentOut = new Intent(this, MyBindWalletListsActivity.class);
                intentOut.putExtra("intent_type","2"); // 提币跳转
                startActivity(intentOut);
                break;
        }
    }
}
