package com.muye.rocket.widget.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.muye.rocket.R;

/**
 * Created by ll on 2018/3/8.
 */

public class ShowVoucherDialog extends Dialog implements View.OnClickListener {
    ImageView mImageView;
    TextView mTextView;
    ImageButton mButton;
    String mTitle;
    String mPath;

    public ShowVoucherDialog(@NonNull Context context) {
        super(context, R.style.DialogNoFrame);
    }

    public void setTitle(String title) {
        this.mTitle = title;
        if (mTextView != null) {
            mTextView.setText(title);
        }
    }

    public void setVoucherImage(String path){
        mPath=path;
        if(mImageView!=null){
            Glide.with(getContext()).load(mPath).into(mImageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_qr_code);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(attributes);
        getWindow().setGravity(Gravity.CENTER);
        onWindowAttributesChanged(attributes);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        mImageView = findViewById(R.id.image_dialog_show_qr);
        mTextView = findViewById(R.id.text_dialog_show_qr);
        mButton = findViewById(R.id.button_dialog_show_qr);
        Glide.with(getContext()).load(mPath).into(mImageView);
        mTextView.setText(mTitle);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
