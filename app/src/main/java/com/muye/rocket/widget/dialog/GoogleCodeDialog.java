package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.muye.rocket.R;


public class GoogleCodeDialog extends Dialog implements View.OnClickListener {

    ImageView cancelButton;
    EditText passwordEditText;
    Button submitButton;
    InputGoogleCodeCallBack inputGoogleCodeCallBack;

    public GoogleCodeDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_google_code);
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
        submitButton = findViewById(R.id.submit_button);

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordEditText.getText().toString();
                submitButton.setEnabled(password.length() >= 4 && password.length() <= 16);
            }
        });
        cancelButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    public void setInputGoogleCodeCallBack(InputGoogleCodeCallBack inputGoogleCodeCallBack) {
        this.inputGoogleCodeCallBack = inputGoogleCodeCallBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                passwordEditText.setText("");
                dismiss();
                break;
            case R.id.submit_button:
                if (inputGoogleCodeCallBack != null) {
                    inputGoogleCodeCallBack.getGoogleCode(passwordEditText.getText().toString());
                }
                passwordEditText.setText("");
                dismiss();
                break;
        }
    }

    public interface InputGoogleCodeCallBack {
        void getGoogleCode(String googleCode);
    }
}
