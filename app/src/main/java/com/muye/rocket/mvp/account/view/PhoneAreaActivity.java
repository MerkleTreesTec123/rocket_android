package com.muye.rocket.mvp.account.view;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.R;
import com.muye.rocket.entity.PhoneArea;

import butterknife.BindView;

/**
 * Created by ll on 2018/4/8.
 */

public class PhoneAreaActivity extends BaseActivity {
    public static final String BUNDLE_KEY_AREA = "bundle_key_area";
    @BindView(R.id.edit_phone_area_search)
    EditText mSearchEditText;

    PhoneAreaFragment mPhoneAreaFragment;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_phone_area;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.country_or_region));
        mPhoneAreaFragment = PhoneAreaFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, mPhoneAreaFragment, PhoneAreaFragment.TAG).commit();
        setupInputListener();
    }


    private void setupInputListener() {
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mPhoneAreaFragment != null) {
                    mPhoneAreaFragment.search(s.toString());
                }
            }
        });
    }

    public void getCheckedArea(PhoneArea phoneArea) {
        if (phoneArea != null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_KEY_AREA, phoneArea);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
