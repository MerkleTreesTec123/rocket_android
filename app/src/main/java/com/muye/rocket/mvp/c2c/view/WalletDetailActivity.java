package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;


public class WalletDetailActivity extends BaseActivity {

    String mCoinID;

    public static void openWalletDetailActivity(Context context, String coinID) {
        Intent intent = new Intent(context, WalletDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

//    @Override
//    protected int getNavigationIcon() {
//        return R.drawable.rc_back_icon_white;
//    }
//
//    @Override
//    protected int getStatusBarColor() {
//        return getResources().getColor(R.color.excColor252642);
//    }
//
//    @Override
//    protected int getToolbarColor() {
//        return getResources().getColor(R.color.excColor252642);
//    }

//    @Override
//    protected boolean isSetStatusBarLightMode() {
//        return false;
//    }
//
//    @Override
//    protected boolean isShouldCreateStatusBar() {
//        return true;
//    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_wallet_detail;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, WalletDetailFragment.newInstance(mCoinID)).commit();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID");
        }
    }


}
