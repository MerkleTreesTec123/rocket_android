package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.muye.rocket.R;

public class DescriptionDialog extends Dialog {
    TextView contentTextView;
    String content;

    public DescriptionDialog(Context context) {
        super(context, R.style.BottomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invite_description);
        contentTextView = findViewById(R.id.content_text_view);
        WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setAttributes(layoutParams);
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        contentTextView.setText(!TextUtils.isEmpty(content)?content:"");
    }
    public void setContent(String content){
        this.content=content;
        if(contentTextView!=null){
            contentTextView.setText(!TextUtils.isEmpty(content)?content:"");
        }
    }
}
