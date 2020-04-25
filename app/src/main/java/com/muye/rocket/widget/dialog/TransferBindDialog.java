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
import com.muye.rocket.R;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.CoinBean;
import java.text.DecimalFormat;
import java.util.List;

//绑定dialog
public class TransferBindDialog extends Dialog implements View.OnClickListener {

    TextView userName, userPhone;
    TextView usdtY, catY;
    Button transferBtn;
    ImageView closeImg;
    OnTransferClickListener onTransferClickListener;
    private BindWalletBean mWalletBean;
    private List<CoinBean> mCoinBeans;
    private int usdtP = -1;
    private int catP  = -1;

    public TransferBindDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_dialog);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);

        initView();
    }

    private void initView() {
        userName = findViewById(R.id.uase_name);
        userPhone = findViewById(R.id.user_phone);
        usdtY    = findViewById(R.id.usdt_num);
        catY     = findViewById(R.id.cat_num);
        transferBtn = findViewById(R.id.transfer_btn);
        closeImg  = findViewById(R.id.img_close);
        transferBtn.setOnClickListener(this);
        closeImg.setOnClickListener(this);
        transferBtn.setVisibility(View.VISIBLE);
        if (mWalletBean != null && mCoinBeans != null && usdtP != -1 && catP != -1)
        setContent(mWalletBean,mCoinBeans,usdtP,catP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.transfer_btn:
                // 体现到火箭
                onTransferClickListener.onTransfer(mWalletBean);
                break;
            case R.id.img_close:
                onTransferClickListener.onClose(this);
                break;
        }
    }
    // 设置dialog内容
    public void setContent(BindWalletBean walletBean, List<CoinBean> coinBeans,int usdtIndex,int catIndex){
        mWalletBean = walletBean;
        mCoinBeans  = coinBeans;
        usdtP = usdtIndex ;
        catP = catIndex;
        if (walletBean != null){
            DecimalFormat   fnum  =   new DecimalFormat("##0.0000");
            if (usdtY == null) return;
            if (null == coinBeans|| coinBeans.size() <= 0){
                usdtY.setText("--");
            }else {
                if (usdtIndex!=-1) {
                    try {
                        float usdtNum = Float.parseFloat(coinBeans.get(usdtIndex).getTotal());
                        usdtY.setText(fnum.format(usdtNum) + "个");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else usdtY.setText("--");
            }
            if (null == coinBeans|| coinBeans.size() <= 0){
                catY.setText("--");
            }else {
                if (catIndex != -1) {
                    try {
                        float catNum = Float.parseFloat(coinBeans.get(catIndex).getTotal());
                        catY.setText(fnum.format(catNum) + "个");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else catY.setText("--");
            }
            if (TextUtils.isEmpty(walletBean.getCat_username())){
                userName.setText("--");
            }else {
                userName.setText(walletBean.getCat_username());
            }
            if (TextUtils.isEmpty(walletBean.getCat_phone())){
                userPhone.setText("--");
            }else {
                userPhone.setText(walletBean.getCat_phone());
            }
        }
    }

    public interface OnTransferClickListener{
        void onTransfer(BindWalletBean walletBean);
        void onClose(TransferBindDialog dialog);
    }

    public void setOnClickTransfer(OnTransferClickListener clickTransfer){
        this.onTransferClickListener = clickTransfer;
    }
}
