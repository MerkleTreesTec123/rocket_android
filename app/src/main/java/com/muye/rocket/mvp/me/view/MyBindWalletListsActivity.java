package com.muye.rocket.mvp.me.view;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import butterknife.BindView;

public class MyBindWalletListsActivity extends BaseActivity {

    private String type;
    @BindView(R.id.rl_contain)
    RelativeLayout riContain;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.bind_cat_list_layout;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);

        type = getIntent().getStringExtra("intent_type");

        setNavigationCenter(type.equals("1")?getString(R.string.my_bind_list):getString(R.string.select_tb_account));
        if (type != null){
            if (type.equals("1")){
                riContain.setBackgroundColor(getResources().getColor(R.color.colorF7F8FC));
            }else riContain.setBackgroundColor(getResources().getColor(R.color.colorFEFEFB));
            getSupportFragmentManager().beginTransaction().add(R.id.fl_container, MyBindWalletListFragment.newInstance(type)).commit();
        }
    }
}
