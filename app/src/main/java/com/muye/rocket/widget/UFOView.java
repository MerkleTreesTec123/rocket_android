package com.muye.rocket.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;

public class UFOView extends RelativeLayout {
    private static final String TAG = "UFOView";
    ImageView ufoImageView;
    ImageView coinImageView;
    ImageView lightImageView;

    ObjectAnimator lightAnimator;
    ObjectAnimator coinAnimator;

    public UFOView(Context context) {
        this(context, null);
    }

    public UFOView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_ufo, this, true);
        ufoImageView = view.findViewById(R.id.ufo_image_view);
        coinImageView = view.findViewById(R.id.coin_image_view);
        lightImageView = view.findViewById(R.id.light_image_view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        startCoinAnimator();
        startLightAnimator();
    }

    private void startCoinAnimator() {

        float end = DimensionTool.dp2px(getContext(), 22);
        if (coinAnimator == null) {
            coinAnimator = ObjectAnimator.ofFloat(coinImageView, "translationY", 0, end);
            coinAnimator.setInterpolator(new AccelerateInterpolator());
            coinAnimator.setDuration(1000);
            coinAnimator.setRepeatCount(ValueAnimator.INFINITE);
            coinAnimator.setRepeatMode(ValueAnimator.REVERSE);
        }
        if (coinAnimator.isRunning()) {
            coinAnimator.cancel();
        }
        coinAnimator.start();
    }

    private void  startLightAnimator(){
        if (lightAnimator == null) {
            lightAnimator = ObjectAnimator.ofFloat(lightImageView, "alpha", 1f, 0.2f);
            lightAnimator.setInterpolator(new LinearInterpolator());
            lightAnimator.setDuration(1000);
            lightAnimator.setRepeatCount(ValueAnimator.INFINITE);
            lightAnimator.setRepeatMode(ValueAnimator.REVERSE);
        }
        if (lightAnimator.isRunning()) {
            lightAnimator.cancel();
        }
        lightAnimator.start();
    }
}
