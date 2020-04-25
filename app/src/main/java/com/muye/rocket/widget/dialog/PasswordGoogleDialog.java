package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.mvp.me.view.ForgetSafePasswordStep1Activity;
import com.muye.rocket.mvp.me.view.SetSafePasswordActivity;


public class PasswordGoogleDialog extends Dialog implements View.OnClickListener {

    ImageView cancelButton;
    TextView passwordTitleTextView;
    EditText passwordEditText;
    View passwordDivider;
    TextView forgetButton;
    TextView googleTitleTextView;
    EditText googleEditText;
    View googleDivider;
    Button submitButton;

    boolean isShowPassword = true;
    boolean isShowGoogleCode = true;
    InputPasswordCallBack inputPasswordCallBack;
    Context context;

    public PasswordGoogleDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exc_wal_dialog_password_google);
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
        initView();
    }

    private void initView() {
        cancelButton = findViewById(R.id.cancel_button);
        passwordEditText = findViewById(R.id.password_edit_text);
        passwordDivider = findViewById(R.id.password_divider);
        passwordTitleTextView = findViewById(R.id.password_title_text_view);
        googleEditText = findViewById(R.id.google_edit_text);
        googleDivider = findViewById(R.id.google_divider);
        googleTitleTextView = findViewById(R.id.google_title_text_view);
        forgetButton = findViewById(R.id.forget_button);
        submitButton = findViewById(R.id.submit_button);

        passwordTitleTextView.setVisibility(isShowPassword ? View.VISIBLE : View.GONE);
        passwordEditText.setVisibility(isShowPassword ? View.VISIBLE : View.GONE);
        passwordDivider.setVisibility(isShowPassword ? View.VISIBLE : View.GONE);
        forgetButton.setVisibility(isShowPassword ? View.VISIBLE : View.GONE);

        googleTitleTextView.setVisibility(isShowGoogleCode ? View.VISIBLE : View.GONE);
        googleEditText.setVisibility(isShowGoogleCode ? View.VISIBLE : View.GONE);
        googleDivider.setVisibility(isShowGoogleCode ? View.VISIBLE : View.GONE);

        cancelButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        forgetButton.setOnClickListener(this);
        setupInputListener();
    }

    private void setupInputListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSubmitButtonEnable();
            }
        };

        passwordEditText.addTextChangedListener(textWatcher);
        googleEditText.addTextChangedListener(textWatcher);
    }

    private void setSubmitButtonEnable() {
        if (passwordEditText == null || googleEditText == null || submitButton == null) {
            return;
        }
        String password = passwordEditText.getText().toString();
        String googleCode = googleEditText.getText().toString();

        boolean passwordEnable;
        boolean googleEnable;
        if (isShowPassword) {
            passwordEnable = password.length() >= 6 && password.length() <= 16;
        } else {
            passwordEnable = true;
        }
        if (isShowGoogleCode) {
            googleEnable = !TextUtils.isEmpty(googleCode);
        } else {
            googleEnable = true;
        }
        submitButton.setEnabled(passwordEnable && googleEnable);
    }

    public PasswordGoogleDialog setIsShowPassword(boolean isShowPassword) {
        this.isShowPassword = isShowPassword;
        if (passwordTitleTextView != null) {
            passwordTitleTextView.setVisibility(isShowPassword ? View.VISIBLE : View.GONE);
        }
        if (passwordEditText != null) {
            passwordEditText.setVisibility(isShowPassword ? View.VISIBLE : View.GONE);
        }
        if (passwordDivider != null) {
            passwordDivider.setVisibility(isShowPassword ? View.VISIBLE : View.GONE);
        }
        if (forgetButton != null) {
            forgetButton.setVisibility(isShowPassword ? View.VISIBLE : View.GONE);
        }
        setSubmitButtonEnable();
        return this;
    }

    public PasswordGoogleDialog setIsShowGoogleCode(boolean isShowGoogleCode) {
        this.isShowGoogleCode = isShowGoogleCode;
        if (googleTitleTextView != null) {
            googleTitleTextView.setVisibility(isShowGoogleCode ? View.VISIBLE : View.GONE);
        }
        if (googleEditText != null) {
            googleEditText.setVisibility(isShowGoogleCode ? View.VISIBLE : View.GONE);
        }
        if (googleDivider != null) {
            googleDivider.setVisibility(isShowGoogleCode ? View.VISIBLE : View.GONE);
        }
        setSubmitButtonEnable();
        return this;
    }

    public PasswordGoogleDialog setInputPasswordCallBack(InputPasswordCallBack inputPasswordCallBack) {
        this.inputPasswordCallBack = inputPasswordCallBack;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                passwordEditText.setText("");
                dismiss();
                break;
            case R.id.forget_button:
                Intent intent = null;
                if (MMKVTools.hasSetPayPassword()) {
                    if (MMKVTools.hasBindPhone()) {
                        intent = new Intent(getContext(), ForgetSafePasswordStep1Activity.class);
                    }else {
                        if (context != null)
                            Toast.makeText(context,context.getString(R.string.bind_phone_),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    intent = new Intent(getContext(), SetSafePasswordActivity.class);
                }
                getContext().startActivity(intent);
                break;
            case R.id.submit_button:
                if (inputPasswordCallBack != null) {
                    inputPasswordCallBack.getPayPassword(passwordEditText.getText().toString(), googleEditText.getText().toString());
                }
                passwordEditText.setText("");
                googleEditText.setText("");
                dismiss();
                break;
        }
    }

    public interface InputPasswordCallBack {
        void getPayPassword(String password, String googleCode);
    }
}
