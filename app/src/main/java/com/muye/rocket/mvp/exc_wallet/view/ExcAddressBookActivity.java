package com.muye.rocket.mvp.exc_wallet.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.base.BaseFragment;
import com.muye.rocket.R;

import butterknife.OnClick;

/*
* 地址簿
* */
public class ExcAddressBookActivity extends BaseActivity {

    String mCoinID;
    String mSelectedAddressID;
    boolean isAddressPicker;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.exc_wal_activity_address_book;
    }

    public static void openExcAddressBookActivity(Context context, String coinID) {
        Intent intent = new Intent(context, ExcAddressBookActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void openExcAddressPicker(BaseActivity activity, String coinID, String selectedAddressID, int requestCode) {
        Intent intent = new Intent(activity, ExcAddressBookActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        bundle.putString("selectedAddressID", selectedAddressID);
        bundle.putBoolean("isAddressPicker", true);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openExcAddressPicker(BaseFragment fragment, String coinID, String selectedAddressID, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), ExcAddressBookActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        bundle.putString("selectedAddressID", selectedAddressID);
        bundle.putBoolean("isAddressPicker", true);
        intent.putExtras(bundle);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.exc_wal_address_book));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, ExcAddressBookFragment.newInstance(mCoinID, mSelectedAddressID, isAddressPicker)).commit();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
            mSelectedAddressID = bundle.getString("selectedAddressID", "");
            isAddressPicker = bundle.getBoolean("isAddressPicker", false);
        }
    }


    @OnClick(R.id.submit_button)
    public void onViewClicked() {
        ExcAddAddressActivity.openExcAddAddressActivity(this, mCoinID, isAddressPicker);
    }
}
