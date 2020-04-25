package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;

public class IEOFailDialog extends Dialog implements View.OnClickListener {

    private TextView titleTextView;

    private String mMessage;
    private OnReExchangeCallBack mOnReExchangeCallBack;

    public IEOFailDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ieo_fail);
        titleTextView = findViewById(R.id.title_text_view);
        findViewById(R.id.submit_button).setOnClickListener(this);
        findViewById(R.id.cancel_button).setOnClickListener(this);

        titleTextView.setText(TextUtils.isEmpty(mMessage) ? "--" : mMessage);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        getWindow().setAttributes(layoutParams);

    }

    public void setOnReExchangeCallBack(OnReExchangeCallBack onReExchangeCallBack) {
        mOnReExchangeCallBack = onReExchangeCallBack;
    }

    public void setMessage(String message) {
        mMessage = message;
        if (titleTextView != null) {
            titleTextView.setText(TextUtils.isEmpty(mMessage) ? "--" : mMessage);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            dismiss();
        } else if (v.getId() == R.id.submit_button) {
            dismiss();
            if (mOnReExchangeCallBack != null) {
                mOnReExchangeCallBack.onReExchange();
            }
        }
    }

    public interface OnReExchangeCallBack {
        void onReExchange();
    }
}
