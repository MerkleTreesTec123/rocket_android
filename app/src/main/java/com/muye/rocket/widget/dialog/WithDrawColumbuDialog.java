package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.CoinBean;

import java.text.DecimalFormat;
import java.util.List;

//绑定dialog
public class WithDrawColumbuDialog extends Dialog implements View.OnClickListener {

    TextView iconRocket, iconColumbu;
    TextView usdtY, catY;
    Button transferBtn;
    ImageView closeImg;
    OnTransferClickListener onTransferClickListener;
    private BindWalletBean mWalletBean;
    private List<CoinBean> mCoinBeans;
    private double usdtP = -1;
    private double catP  = -1;

    public WithDrawColumbuDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.free_with_draw_dialog);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);

        initView();
    }

    private void initView() {
        iconRocket = findViewById(R.id.icon_rocket);
        iconColumbu = findViewById(R.id.icon_columbu);
        usdtY    = findViewById(R.id.usdt_num);
        catY     = findViewById(R.id.cat_num);
        transferBtn = findViewById(R.id.transfer_btn);
        closeImg  = findViewById(R.id.img_close);
        transferBtn.setOnClickListener(this);
        closeImg.setOnClickListener(this);
        transferBtn.setVisibility(View.VISIBLE);
        if (mWalletBean != null && usdtP != -1 && catP != -1)
        setContent(mWalletBean,usdtP,catP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.transfer_btn:
                // 体现到哥伦布钱包
                onTransferClickListener.onTransfer(mWalletBean);
                break;
            case R.id.img_close:
                onTransferClickListener.onClose(this);
                break;
        }
    }
    // 设置dialog内容
    public void setContent(BindWalletBean walletBean,double usdtIndex,double catIndex){
        mWalletBean = walletBean;

        usdtP = usdtIndex ;
        catP = catIndex;


        if (walletBean != null){
            DecimalFormat   fnum  =   new DecimalFormat("##0.0000");
            if (usdtY == null) return;
            usdtY.setText(fnum.format(usdtIndex) + "个");
            catY.setText(fnum.format(catIndex) + "个");
            if (TextUtils.isEmpty(MMKVTools.getPhone())){
                iconRocket.setText("--");
            }else {
                iconRocket.setText(MMKVTools.getPhone());
            }
            if (TextUtils.isEmpty(walletBean.getCat_username())){
                iconColumbu.setText("--");
            }else {
                iconColumbu.setText(walletBean.getCat_username());
            }
        }
    }

    public interface OnTransferClickListener{
        void onTransfer(BindWalletBean walletBean);
        void onClose(WithDrawColumbuDialog dialog);
    }

    public void setOnClickTransfer(OnTransferClickListener clickTransfer){
        this.onTransferClickListener = clickTransfer;
    }
}
