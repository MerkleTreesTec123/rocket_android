package com.muye.rocket.mvp.exc_wallet.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.R;

public class ExcRechargeRecordActivity extends BaseActivity {

    String mCoinID;

    public static void openExcRechargeRecordActivity(Context context, String coinID) {
        Intent intent = new Intent(context, ExcRechargeRecordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.exc_wal_activity_recharge_record;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.exc_wal_recharge_record));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, ExcRechargeRecordFragment.newInstance(mCoinID)).commit();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
        }
    }
}
