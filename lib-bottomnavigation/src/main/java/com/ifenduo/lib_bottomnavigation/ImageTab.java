package com.ifenduo.lib_bottomnavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

public class ImageTab extends BaseTab {
    public ImageTab(@NonNull Context context) {
        this(context,null);
    }

    public ImageTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImageTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_tab_image_bottom_navigation, this, true);
        mIconImageView = findViewById(R.id.icon);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mIconImageView.setImageResource(mCheckedDrawable);
        } else {
            mIconImageView.setImageResource(mDefaultDrawable);
        }
    }

    @Override
    public void setMessageNumber(int number) {

    }

    @Override
    public void setHasMessage(boolean hasMessage) {

    }

    @Override
    public String getTitle() {
        return "";
    }
}
