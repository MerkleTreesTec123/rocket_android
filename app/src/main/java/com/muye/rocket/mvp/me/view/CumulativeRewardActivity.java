package com.muye.rocket.mvp.me.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

/**
 * 累计获得奖励
 */
public class CumulativeRewardActivity extends BaseActivity {
    private String mCoinID;
    private String mCoinName;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_invite_cumulative_reward;
    }


    public static void openCumulativeRewardActivity(Context context, String coinID, String coinName) {
        Intent intent = new Intent(context, CumulativeRewardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        bundle.putString("coinName", coinName);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_container, CumulativeRewardListFragment.newInstance(mCoinID, mCoinName))
                    .commit();
        }
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
            mCoinName = bundle.getString("coinName", "");
        }
    }


}
