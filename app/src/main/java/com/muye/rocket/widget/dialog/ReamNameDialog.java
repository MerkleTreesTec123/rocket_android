package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;

// 提示实名认证
public class ReamNameDialog extends Dialog implements View.OnClickListener {

    Context context;
    Button btnRealName;
    TextView tvLater;
    OnRealNameClickListener clickListener;

    public ReamNameDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_name_hint);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
        setCancelable(false);

        btnRealName = findViewById(R.id.btn_to_realm);
        tvLater = findViewById(R.id.later_txt);
        btnRealName.setOnClickListener(this);
        tvLater.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_to_realm:
                if (clickListener != null)
                clickListener.toRealNameClick(this);
                break;
            case R.id.later_txt:
                if (clickListener != null)
                clickListener.sayLater(this);
                break;
        }
    }

    public interface OnRealNameClickListener{
        void toRealNameClick(ReamNameDialog dialog);
        void sayLater(ReamNameDialog dialog);
    }
    public ReamNameDialog setOnRealNameListener(OnRealNameClickListener listener){
        this.clickListener = listener;
        return this;
    }
}
