package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;

public class SelectVerifiedDialog extends Dialog implements View.OnClickListener {

    Button chinaBtn;
    TextView otherBtn;
    ImageView closeImg;

    public SelectVerifiedDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_realname_sdialog);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
        initView();
    }

    private void initView() {
        chinaBtn = findViewById(R.id.china_real_btn);
        otherBtn = findViewById(R.id.other_real_btn);
        chinaBtn.setOnClickListener(this);
        otherBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.china_real_btn){
            clickListener.omSelectedIDCardType(1);
        }else if (v.getId() == R.id.other_real_btn){
            clickListener.omSelectedIDCardType(2);
        }

    }
    OnSelectClickListener clickListener;
    public interface OnSelectClickListener{
        void omSelectedIDCardType(int type);
    }
    public void setOnSelectedListener(OnSelectClickListener clickListener){
        this.clickListener = clickListener;
    }

}
