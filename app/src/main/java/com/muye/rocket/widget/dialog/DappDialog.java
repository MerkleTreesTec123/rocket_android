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

public class DappDialog extends Dialog implements View.OnClickListener {
    TextView mNameTextView;

    String name;


    OnCancelButtonClickListener mOnCancelButtonClickListener;
    OnOkButtonClickListener mOnOkButtonClickListener;

    public DappDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_dapp);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
        initView();
    }

    private void initView() {
        mNameTextView = findViewById(R.id.name_text_view);
        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.submit_button).setOnClickListener(this);
        if (TextUtils.isEmpty(name)) {
            mNameTextView.setText(String.format(getContext().getString(R.string.dapp_name_), "--"));
        } else {
            mNameTextView.setText(String.format(getContext().getString(R.string.dapp_name_), name));
        }
    }

    public DappDialog setDappName(String name) {
        this.name = name;
        if(mNameTextView!=null){
            if (TextUtils.isEmpty(name)) {
                mNameTextView.setText(String.format(getContext().getString(R.string.dapp_name_), "--"));
            } else {
                mNameTextView.setText(String.format(getContext().getString(R.string.dapp_name_), name));
            }
        }

        return this;
    }

    public DappDialog setOnOkButtonClickListener(OnOkButtonClickListener onOkButtonClickListener) {
        mOnOkButtonClickListener = onOkButtonClickListener;
        return this;
    }

    public DappDialog setOnCancelButtonClickListener(OnCancelButtonClickListener onCancelButtonClickListener) {
        mOnCancelButtonClickListener = onCancelButtonClickListener;
        return this;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            dismiss();
            if (mOnCancelButtonClickListener != null) {
                mOnCancelButtonClickListener.onCancelButtonClick(this);
            }
        } else if (v.getId() == R.id.submit_button) {
            if (mOnOkButtonClickListener != null) {
                mOnOkButtonClickListener.onOkButtonClick(this);
            }
        }
    }

    public interface OnCancelButtonClickListener {
        void onCancelButtonClick(DappDialog dialog);
    }

    public interface OnOkButtonClickListener {
        void onOkButtonClick(DappDialog dialog);
    }

}
