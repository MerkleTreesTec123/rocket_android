package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hjq.toast.ToastUtils;
import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;
import com.muye.rocket.entity.BindWalletBean;

//选择币种dialog
public class SelectCoinDialog extends Dialog implements View.OnClickListener {

    LinearLayout llUsdt,llCat;
    Button selectBtn;
    ImageView closeImg,imgUsdt,imgCat;
    EditText rocketNum;
    EditText columbusPassword;
    OnSelectClickListener onTransferClickListener;
    private int COINTYPE = -1;
    BindWalletBean walletBean;
    Context context;
    private int type ;

    public SelectCoinDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_coin_dialog);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);

        initView();
    }

    private void initView() {
        llUsdt           = findViewById(R.id.ll_usdt);
        llCat            = findViewById(R.id.ll_cat);
        selectBtn        = findViewById(R.id.select_btn);
        closeImg         = findViewById(R.id.img_close);
        imgUsdt          = findViewById(R.id.img_usdt);
        imgCat           = findViewById(R.id.img_cat);
        rocketNum        = findViewById(R.id.rocket_num);
        columbusPassword = findViewById(R.id.columbus_password);
        selectBtn.setOnClickListener(this);
        closeImg.setOnClickListener(this);

        if (type == 1){
            rocketNum.setVisibility(View.VISIBLE);
            columbusPassword.setHint(context.getString(R.string.cloumbu_password));
            selectBtn.setBackground(context.getResources().getDrawable(R.drawable.bg_72fff9));
        }else {
            rocketNum.setVisibility(View.VISIBLE);
            columbusPassword.setHint(context.getString(R.string.rocket_password));
            selectBtn.setBackground(context.getResources().getDrawable(R.drawable.bg_e08e2d));
        }

        llUsdt.setOnClickListener(this::onClick);
        llCat .setOnClickListener(this::onClick);
    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_usdt:
                // 体现到火箭
                COINTYPE = 1;
                llUsdt.setBackground(context.getResources().getDrawable(R.drawable.bg_dedede_fbfbfb));
                llCat.setBackground(context.getResources().getDrawable(R.drawable.bg_dedede_ffffff));
                if (type == 1){
                    imgUsdt.setBackground(context.getResources().getDrawable(R.drawable.radio_checked));
                }else {
                    imgUsdt.setBackground(context.getResources().getDrawable(R.drawable.radio_press));
                }
                imgCat.setBackground(context.getResources().getDrawable(R.drawable.radio_normal));
                break;
            case R.id.ll_cat:
                // 体现到火箭
                COINTYPE = 2;
                llUsdt.setBackground(context.getResources().getDrawable(R.drawable.bg_dedede_ffffff));
                llCat.setBackground(context.getResources().getDrawable(R.drawable.bg_dedede_fbfbfb));
                imgUsdt.setBackground(context.getResources().getDrawable(R.drawable.radio_normal));
                if (type == 1){
                    imgCat.setBackground(context.getResources().getDrawable(R.drawable.radio_checked));
                }else {
                    imgCat.setBackground(context.getResources().getDrawable(R.drawable.radio_press));
                }

                break;
            case R.id.select_btn:
                if (type == 1){
                    if (onTransferClickListener != null)
                        onTransferClickListener.onSelected(COINTYPE,walletBean,columbusPassword,type,rocketNum.getText().toString().trim());
                }else {
                    if (COINTYPE == -1){
                        ToastUtils.show(context.getString(R.string.select_coin_type));
                        return;
                    }
                    if (TextUtils.isEmpty(rocketNum.getText().toString().trim())){
                        ToastUtils.show(context.getString(R.string.coin_num));
                        return;
                    }else {
                        if (onTransferClickListener != null)
                            onTransferClickListener.onSelected(COINTYPE,walletBean,columbusPassword,type,rocketNum.getText().toString().trim());
                    }
                }

                break;
            case R.id.img_close:
                onTransferClickListener.onClose(this);
                break;
        }
    }

    public SelectCoinDialog setWalletCoin(BindWalletBean walletCoin){
        this.walletBean = walletCoin;
        return this;
    }

    public SelectCoinDialog setType(int type){
        this.type = type;
        return this;
    }

    public interface OnSelectClickListener{
        void onSelected(int coin,BindWalletBean walletBean,EditText pasword,int type,String num);
        void onClose(SelectCoinDialog dialog);
    }

    public void setOnClickTransfer(OnSelectClickListener clickTransfer){
        this.onTransferClickListener = clickTransfer;
    }
}
