package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.mvp.me.view.ForgetSafePasswordStep1Activity;
import com.muye.rocket.mvp.me.view.SetSafePasswordActivity;


public class PayPasswordDialog extends Dialog implements View.OnClickListener {

    ImageView cancelButton;
    EditText passwordEditText;
    TextView forgetButton;
    Button submitButton;
    InputPayPasswordCallBack inputPayPasswordCallBack;
    Context context;

    public PayPasswordDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_password);
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
        forgetButton = findViewById(R.id.forget_button);
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
                submitButton.setEnabled(password.length() >= 6 && password.length() <= 16);
            }
        });
        cancelButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        forgetButton.setOnClickListener(this);
    }

    public void setInputPayPasswordCallBack(InputPayPasswordCallBack inputPayPasswordCallBack) {
        this.inputPayPasswordCallBack = inputPayPasswordCallBack;
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
                if (inputPayPasswordCallBack != null) {
                    inputPayPasswordCallBack.getPayPassword(passwordEditText.getText().toString());
                }
                passwordEditText.setText("");
                dismiss();
                break;
        }
    }

    public interface InputPayPasswordCallBack {
        void getPayPassword(String payPassword);
    }
}
