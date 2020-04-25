package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;
import com.muye.rocket.entity.BindWalletBean;

//选择币种dialog
public class CnfirmInfoDialog extends Dialog {

    TextView tvName,tvNum;
    Button submitBtn,updateBtn;
    OnSelectClickListener onSelectClickListener;
    private String idCardName,idCardNum;

    public CnfirmInfoDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sure_user_info_dialog);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);

        initView();
    }

    private void initView() {
        submitBtn = findViewById(R.id.submit_btn);
        updateBtn = findViewById(R.id.update_btn);
        tvName    = findViewById(R.id.id_card_name);
        tvNum     = findViewById(R.id.id_card_num);
        updateBtn.setOnClickListener(this::onClick);
        submitBtn.setOnClickListener(this::onClick);

        if (idCardName != null && idCardNum != null){
            tvName.setText(idCardName);
            tvNum.setText(idCardNum);
        }else {
            tvName.setText("--");
            tvNum.setText("--");
        }
    }

    private void onClick(View view) {
        switch (view.getId()){
            case R.id.submit_btn:
                //
                if (onSelectClickListener != null)
                    onSelectClickListener.onSubmit();
                break;
            case R.id.update_btn:
                if (onSelectClickListener != null)
                    onSelectClickListener.onUpdateInfo(this);
                break;
        }
    }


    public CnfirmInfoDialog setIdCardInfo(String name,String idNum){
        this.idCardName = name;
        this.idCardNum  = idNum;
        return this;
    }

    public interface OnSelectClickListener{
        void onSubmit();
        void onUpdateInfo(CnfirmInfoDialog dialog);
    }

    public void setOnClickSelect(OnSelectClickListener clickTransfer){
        this.onSelectClickListener = clickTransfer;
    }
}
