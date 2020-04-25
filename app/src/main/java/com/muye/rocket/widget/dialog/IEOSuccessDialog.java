package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;
import com.muye.rocket.mvp.ieo.view.MyIeoActivity;

public class IEOSuccessDialog extends Dialog implements View.OnClickListener {

    private TextView titleTextView;
    private TextView numTextView;

    private String mPhase;
    private String mNum;

    public IEOSuccessDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ieo_success);
        titleTextView = findViewById(R.id.title_text_view);
        numTextView = findViewById(R.id.num_text_view);
        findViewById(R.id.submit_button).setOnClickListener(this);
        findViewById(R.id.cancel_button).setOnClickListener(this);

        titleTextView.setText(String.format(getContext().getString(R.string.space_plan_phase_), TextUtils.isEmpty(mPhase) ? "--" : mPhase));
        numTextView.setText(String.format(getContext().getString(R.string.exchange_gains_ufo_), TextUtils.isEmpty(mNum) ? "--" : mNum));

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        getWindow().setAttributes(layoutParams);
    }

    public void setPhase(String phase) {
        mPhase = phase;
        if (titleTextView != null) {
            titleTextView.setText(String.format(getContext().getString(R.string.space_plan_phase_), TextUtils.isEmpty(mPhase) ? "--" : mPhase));
        }
    }

    public void setNum(String num) {
        mNum = num;
        if (numTextView != null) {
            numTextView.setText(String.format(getContext().getString(R.string.exchange_gains_ufo_), TextUtils.isEmpty(mNum) ? "--" : mNum));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            dismiss();
        } else if (v.getId() == R.id.submit_button) {
            Intent intent = new Intent(getContext(), MyIeoActivity.class);
            getContext().startActivity(intent);
            dismiss();
        }
    }
}
