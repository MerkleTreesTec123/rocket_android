package com.muye.rocket.widget.popup;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.muye.rocket.R;
import com.zyyoona7.popup.EasyPopup;

public class C2CPayTypePicker extends EasyPopup implements View.OnClickListener {

    ImageView mBankCardHook;
    ImageView mAlipayHook;
    ImageView mWechatHook;
    Context mContext;


    public static C2CPayTypePicker create(Context context, int width, int height) {
        return new C2CPayTypePicker(context, width, height);
    }

    private C2CPayTypePicker(Context context, int width, int height) {
        super(context);
        mContext = context;
        setContentView(R.layout.c2c_home_pay_type_picker, width, height);
        setBackgroundDimEnable(false);
        setFocusAndOutsideEnable(true);
        apply();
    }

    @Override
    protected void onPopupWindowViewCreated(View contentView) {
        super.onPopupWindowViewCreated(contentView);
        contentView.findViewById(R.id.background_view);
        mBankCardHook = contentView.findViewById(R.id.bank_card_hook);
        mAlipayHook = contentView.findViewById(R.id.alipay_hook);
        mWechatHook = contentView.findViewById(R.id.wechat_hook);
        contentView.findViewById(R.id.background_view).setOnClickListener(this);
        contentView.findViewById(R.id.bank_card_container).setOnClickListener(this);
        contentView.findViewById(R.id.alipay_container).setOnClickListener(this);
        contentView.findViewById(R.id.wechat_container).setOnClickListener(this);
        contentView.findViewById(R.id.reset_button).setOnClickListener(this);
        contentView.findViewById(R.id.ok_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.background_view:
                dismiss();
                break;
            case R.id.bank_card_hook:
                mBankCardHook.setVisibility(mBankCardHook.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.alipay_hook:
                mAlipayHook.setVisibility(mAlipayHook.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.wechat_hook:
                mWechatHook.setVisibility(mWechatHook.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.reset_button:
                mBankCardHook.setVisibility(View.GONE);
                mAlipayHook.setVisibility(View.GONE);
                mWechatHook.setVisibility(View.GONE);
                break;
            case R.id.ok_button:
                dismiss();
                break;
        }
    }
}
