package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

public class MyReleaseDetailActivity extends BaseActivity {

    String mReleaseID;
    String mOrderType;

    public static void openMyReleaseDetailActivity(Context context, String releaseID, String orderType) {
        Intent intent = new Intent(context, MyReleaseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("releaseID", releaseID);
        bundle.putString("orderType", orderType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_my_release;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_release_details));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, MyReleaseDetailFragment.newInstance(mReleaseID, mOrderType)).commit();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mReleaseID = bundle.getString("releaseID", "");
            mOrderType = bundle.getString("orderType", "");
        }
    }
}
