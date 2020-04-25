package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.muye.rocket.R;
import com.muye.rocket.mvp.c2c.view.AlipayAddressActivity;
import com.muye.rocket.mvp.c2c.view.BankAddressActivity;
import com.muye.rocket.mvp.c2c.view.WeChatAddressActivity;

public class C2CAddAccountDialog extends Dialog implements View.OnClickListener {
    TextView bankCardTextView;
    TextView alipayTextView;
    TextView wechatTextView;
    ImageView cancelButton;

    boolean isShowBankCard;
    boolean isShowAlipay;
    boolean isShowWechat;

    public C2CAddAccountDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        setContentView(R.layout.c2c_dialog_add_pay_account);

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

    public C2CAddAccountDialog setIsShowBankCard(boolean isShow) {
        isShowBankCard = isShow;
        if (bankCardTextView != null) {
            bankCardTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        return this;
    }


    public C2CAddAccountDialog setIsShowAlipay(boolean isShow) {
        isShowAlipay = isShow;
        if (alipayTextView != null) {
            alipayTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public C2CAddAccountDialog setIsShowWechat(boolean isShow) {
        isShowWechat = isShow;
        if (wechatTextView != null) {
            wechatTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bank_card_text_view:
                dismiss();
                intent = new Intent(getContext(), BankAddressActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.alipay_text_view:
                dismiss();
                intent = new Intent(getContext(), AlipayAddressActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.wechat_text_view:
                dismiss();
                intent = new Intent(getContext(), WeChatAddressActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.cancel_button:
                dismiss();
                break;
        }
    }
}
