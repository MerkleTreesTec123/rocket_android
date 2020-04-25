package com.muye.rocket.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;

public class MiningView extends ViewGroup {
    private static final String TAG = "MiningView";
    int mWidth;
    ImageView mCoinImageView;
    ImageView mMinerImageView;
    ImageView mLightImageView;

    ObjectAnimator lightAnimator;
    ObjectAnimator coinAnimator;

    public MiningView(Context context) {
        this(context, null);

    }

    public MiningView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCoinImageView = new ImageView(context);
        mCoinImageView.setImageResource(R.drawable.image_mining_coin);

        mMinerImageView = new ImageView(context);
        mMinerImageView.setImageResource(R.drawable.image_mining_miner);

        mLightImageView = new ImageView(context);
        mLightImageView.setImageResource(R.drawable.image_mining_light);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getWidth();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            addView();
        }
    }

    private void addView() {
        //矿机图片
        int minerWidth = mWidth;
        int minerHeight = (int) (1128 / 1125f * minerWidth);
        LayoutParams minerParams = new LayoutParams(minerWidth, minerHeight);
        addView(mMinerImageView, minerParams);
        mMinerImageView.layout(0, 0, minerWidth, minerHeight);

        //币图片
        int coinWidth = (int) (mWidth * (228f / 1125f));
        int coinHeight = (int) (267f / 228f * coinWidth);
        int coinLeft = (int) (mWidth * 1f / 375f * 153);
        int coinTop = (int) (mWidth * 1f / 375f * 72);
        LayoutParams coinParams = new LayoutParams(coinWidth, coinHeight);
        addView(mCoinImageView, coinParams);
        mCoinImageView.layout(coinLeft, coinTop, coinLeft + coinWidth, coinTop + coinHeight);

        //光束
        int lightWidth = (int) (mWidth * (453f / 1125f));
        int lightHeight = (int) (381f / 453f * lightWidth);
        int lightLeft = (int) ((mWidth - lightWidth) / 2f + (mWidth * 1f / 375f * 5.6));
        int lightTop = (int) (mWidth * 1f / 375f * 51);
        LayoutParams lightParams = new LayoutParams(lightWidth, lightHeight);
        addView(mLightImageView, lightParams);
        mLightImageView.layout(lightLeft, lightTop, lightLeft + lightWidth, lightTop + lightHeight);
    }

    private void startCoinAnimator() {
        if (coinAnimator == null) {
            float end =  0-DimensionTool.dp2px(getContext(), 16);
            coinAnimator = ObjectAnimator.ofFloat(mCoinImageView, "translationY", 0, end);
            coinAnimator.setInterpolator(new DecelerateInterpolator());
            coinAnimator.setDuration(1000);
            coinAnimator.setRepeatCount(ValueAnimator.INFINITE);
            coinAnimator.setRepeatMode(ValueAnimator.REVERSE);
        }
        if (coinAnimator.isRunning()) {
            coinAnimator.cancel();
        }
        coinAnimator.start();
    }

    private void startLightAnimator() {
        if (lightAnimator == null) {
            lightAnimator = ObjectAnimator.ofFloat(mLightImageView, "alpha", 1f, 0.4f);
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

    public void startAnim() {
        startCoinAnimator();
        startLightAnimator();
    }

    public void stopAnim() {
        if (coinAnimator != null && coinAnimator.isRunning()) {
            coinAnimator.cancel();
        }

        if (lightAnimator != null && lightAnimator.isRunning()) {
            lightAnimator.cancel();
        }
    }

    public boolean isRunning() {
        return (coinAnimator != null && lightAnimator != null && coinAnimator.isRunning() && lightAnimator.isRunning());
    }
}
