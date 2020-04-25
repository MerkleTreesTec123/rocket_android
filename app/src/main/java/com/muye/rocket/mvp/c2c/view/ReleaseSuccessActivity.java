package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import butterknife.OnClick;

public class ReleaseSuccessActivity extends BaseActivity {

    String mReleaseID;
    double mTotalNum;
    double mTransactionNum;
    String mTransactionType;
    String mCoinName;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_release_success;
    }

    public static void openReleaseSuccessActivity(Context context, String releaseID, String coinName,
                                                  String transactionType, double totalNum, double transactionNum) {
        Intent intent = new Intent(context, ReleaseSuccessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("releaseID", releaseID);
        bundle.putString("transactionType", transactionType);
        bundle.putDouble("totalNum", totalNum);
        bundle.putDouble("transactionNum", transactionNum);
        bundle.putString("coinName", coinName);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_release_result), getResources().getColor(R.color.c2cColor000000));
        setNavigationRight(getString(R.string.complete), getResources().getColor(R.color.c2cColorB7B7C4), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mReleaseID = bundle.getString("releaseID", "");
            mTotalNum = bundle.getDouble("totalNum", 0);
            mTransactionNum = bundle.getDouble("transactionNum", 0);
            mTransactionType = bundle.getString("transactionType", "");
            mCoinName = bundle.getString("coinName", "");
        }
    }


    @OnClick({R.id.left_button, R.id.right_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                MyReleaseDetailActivity.openMyReleaseDetailActivity(this, mReleaseID, mTransactionType);
                finish();
                break;
        }
    }
}
