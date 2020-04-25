package com.muye.rocket.mvp.ieo.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

/**
 * ieo详情
 */
public class MyIeoDetailActivity extends BaseActivity {

    private String mRecordID;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_ieo_detail;
    }

    public static void openMyIeoDetailActivity(Context context, String recordID) {
        Intent intent = new Intent(context, MyIeoDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("recordID", recordID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.details));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout, MyIeoDetailListFragment.newInstance(mRecordID))
                .commit();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mRecordID = bundle.getString("recordID", "");
        }
    }
}
