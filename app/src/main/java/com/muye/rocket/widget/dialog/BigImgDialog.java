package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;

public class BigImgDialog extends Dialog implements View.OnClickListener {

    ImageView lottery_img;
    Context context;
    int level = -1;
    private boolean isOnTouch;

    public BigImgDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_img_dialog);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
        if (level != 7){
            setCancelable(false);
        }
        lottery_img = findViewById(R.id.lottery_img);
        if (level != -1) {
            if (level == 1){
                lottery_img.setImageResource(R.mipmap.fail_real_img);
            }else if (level == 2){
                lottery_img.setImageResource(R.mipmap.read_wirte_img);
            }else if (level == 3){
                lottery_img.setImageResource(R.mipmap.take_photo_img);
            }else if (level == 4){
                lottery_img.setImageResource(R.mipmap.close_with_draw);
            }else if (level == 5){
                lottery_img.setImageResource(R.mipmap.with_draw_coin_dialog);
            }
        }
        lottery_img.setOnClickListener(this);
    }

    public BigImgDialog setImg(int level){
        this.level = level;
        return this;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
