package com.muye.rocket.mvp.exc_wallet.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.R;

/*
* 提币记录
* */
public class ExcWithdrawRecordActivity extends BaseActivity {

    String mCoinID;

    public static void openExcWithdrawRecordActivity(Context context, String coinID) {
        Intent intent = new Intent(context, ExcWithdrawRecordActivity.class);
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
        setNavigationCenter(getString(R.string.exc_wal_withdraw_record));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, ExcWithdrawRecordFragment.newInstance(mCoinID)).commit();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
        }
    }
}
