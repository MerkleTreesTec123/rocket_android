package com.muye.rocket.mvp.me.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.muye.rocket.base.BaseActivity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.entity.GoogleDevice;
import com.muye.rocket.event.BindGoogleSuccessEvent;
import com.muye.rocket.mvp.me.contract.BindGoogleContract;
import com.muye.rocket.mvp.me.presenter.BindGooglePresenter;
import com.muye.rocket.widget.PasswordEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class BindGoogleStep3Activity extends BaseActivity implements BindGoogleContract.View {
    @BindView(R.id.code_edit_text)
    PasswordEditText codeEditText;
    @BindView(R.id.submit_button)
    TextView submitButton;
    String mKey;
    BindGoogleContract.Presenter mPresenter;

    public static void openBindGoogleStep3Activity(Context context, String key) {
        Intent intent = new Intent(context, BindGoogleStep3Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_bind_google_step_3;
    }


    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.google_authenticator));
        setupInputListener();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mKey = bundle.getString("key");
        }
    }

    private void setupInputListener() {
        codeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                submitButton.setEnabled(s.toString().length() == 6);
            }
        });
    }

    @OnClick(R.id.submit_button)
    public void onViewClicked() {
        if (mPresenter == null) {
            mPresenter = new BindGooglePresenter(this);
        }
        mPresenter.bindGoogle(codeEditText.getText().toString(), mKey);
    }

    @Override
    public void bindGoogleDevice(GoogleDevice device) {

    }

    @Override
    public void onBindGoogleSuccess() {
        MMKVTools.saveHasBindGoogleCode(true);
        showToast(getString(R.string.bind_google_code_success));
        EventBus.getDefault().post(new BindGoogleSuccessEvent());
        finish();
    }
}
