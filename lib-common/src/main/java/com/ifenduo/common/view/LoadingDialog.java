package com.ifenduo.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ifenduo.lib_common.R;


public class LoadingDialog extends Dialog {

    private String mMessage;
    private TextView mMessageTextView;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(true);

        mMessageTextView=findViewById(R.id.message_text_view);

        mMessageTextView.setText(mMessage);
        mMessageTextView.setVisibility(!TextUtils.isEmpty(mMessage) ? View.VISIBLE : View.GONE);
    }

    public LoadingDialog setMessage(String message) {
        this.mMessage = message;
        if (mMessageTextView != null) {
            mMessageTextView.setText(message);
            mMessageTextView.setVisibility(!TextUtils.isEmpty(message) ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public void show(String message) {
        setMessage(message).show();
    }
}
