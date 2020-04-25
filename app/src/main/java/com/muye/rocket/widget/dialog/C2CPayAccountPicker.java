package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.muye.rocket.Constant;
import com.muye.rocket.R;

public class C2CPayAccountPicker extends Dialog implements View.OnClickListener {
    TextView bankCardTextView;
    TextView alipayTextView;
    TextView wechatTextView;
    ImageView cancelButton;

    boolean isShowBankCard;
    boolean isShowAlipay;
    boolean isShowWechat;
    OnPayTypeCheckedListener mOnPayTypeCheckedListener;

    public C2CPayAccountPicker(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        setContentView(R.layout.c2c_dialog_pay_account_picker);

        bankCardTextView = findViewById(R.id.bank_card_text_view);
        alipayTextView = findViewById(R.id.alipay_text_view);
        wechatTextView = findViewById(R.id.wechat_text_view);
        cancelButton = findViewById(R.id.cancel_button);

        bankCardTextView.setOnClickListener(this);
        alipayTextView.setOnClickListener(this);
        wechatTextView.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        bankCardTextView.setVisibility(isShowBankCard ? View.VISIBLE : View.GONE);
        alipayTextView.setVisibility(isShowAlipay ? View.VISIBLE : View.GONE);
        wechatTextView.setVisibility(isShowWechat ? View.VISIBLE : View.GONE);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
    }

    public C2CPayAccountPicker setOnPayTypeCheckedListener(OnPayTypeCheckedListener onPayTypeCheckedListener) {
        mOnPayTypeCheckedListener = onPayTypeCheckedListener;
        return this;
    }

    public C2CPayAccountPicker setIsShowBankCard(boolean isShow) {
        isShowBankCard = isShow;
        if (bankCardTextView != null) {
            bankCardTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        return this;
    }


    public C2CPayAccountPicker setIsShowAlipay(boolean isShow) {
        isShowAlipay = isShow;
        if (alipayTextView != null) {
            alipayTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public C2CPayAccountPicker setIsShowWechat(boolean isShow) {
        isShowWechat = isShow;
        if (wechatTextView != null) {
            wechatTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        Drawable drawable=getContext().getResources().getDrawable(R.drawable.c2c_ic_picker_hook);
        switch (v.getId()) {
            case R.id.bank_card_text_view:
                bankCardTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                alipayTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                wechatTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                dismiss();
                if (mOnPayTypeCheckedListener != null) {
                    mOnPayTypeCheckedListener.onPayTypeChecked(Constant.C2C_PAY_TYPE_BANK_CARD);
                }
                break;
            case R.id.alipay_text_view:
                bankCardTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                alipayTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                wechatTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                dismiss();
                if (mOnPayTypeCheckedListener != null) {
                    mOnPayTypeCheckedListener.onPayTypeChecked(Constant.C2C_PAY_TYPE_ALIPAY);
                }
                break;
            case R.id.wechat_text_view:
                bankCardTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                alipayTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                wechatTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                dismiss();
                if (mOnPayTypeCheckedListener != null) {
                    mOnPayTypeCheckedListener.onPayTypeChecked(Constant.C2C_PAY_TYPE_WECHAT);
                }
                break;
            case R.id.cancel_button:
                dismiss();
                break;
        }
    }

    public interface OnPayTypeCheckedListener {
        void onPayTypeChecked(String payType);
    }
}
