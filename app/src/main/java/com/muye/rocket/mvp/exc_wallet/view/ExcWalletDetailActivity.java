package com.muye.rocket.mvp.exc_wallet.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.R;

public class ExcWalletDetailActivity extends BaseActivity {

    String mCoinID;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.exc_wal_activity_wallet_detail;
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.rc_back_icon_white;
    }

    @Override
    protected int getStatusBarColor() {
        return getResources().getColor(R.color.excColor252642);
    }

    @Override
    protected int getToolbarColor() {
        return getResources().getColor(R.color.excColor252642);
    }

    public static void openExcWalletDetailActivity(Context context, String coinID) {
        Intent intent = new Intent(context, ExcWalletDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        mImmersionBar.statusBarDarkFont(false, 0.2f).init();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, ExcWalletDetailFragment.newInstance(mCoinID)).commit();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
        }
    }
}
