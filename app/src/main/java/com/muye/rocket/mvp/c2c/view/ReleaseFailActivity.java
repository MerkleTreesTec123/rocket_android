package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ReleaseFailActivity extends BaseActivity {

    @BindView(R.id.hint_text_view)
    TextView hintTextView;

    String mMessage;
    String mTransactionType;
    int mCode;

    public static void openReleaseFailActivity(Context context, String transactionType, String message, int code) {
        Intent intent = new Intent(context, ReleaseFailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putString("transactionType", transactionType);
        bundle.putInt("code", code);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_release_fail;
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

        hintTextView.setText(TextUtils.isEmpty(mMessage) ? getString(R.string.c2c_release_fail_hint) : mMessage);
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mMessage = bundle.getString("message", "");
            mTransactionType = bundle.getString("transactionType", "");
            mCode = bundle.getInt("code");
        }
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                ReleaseActivity.openReleaseActivity(this, Constant.C2C_TRANSACTION_TYPE_SELL.equals(mTransactionType) ? 1 : 0,"");
                finish();
                break;
        }
    }
}
