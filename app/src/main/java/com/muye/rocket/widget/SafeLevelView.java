package com.muye.rocket.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.muye.rocket.R;

public class SafeLevelView extends View {

    int mSafeLevel;
    Paint mPaint;
    ValueAnimator mAnimator;

    public SafeLevelView(Context context) {
        super(context);
        init();
    }

    public SafeLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SafeLevelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }
    }

    public void setSafeLevel(@IntRange(from = 0, to = 3) int level) {
        float end = 0;
        mSafeLevel = level;
        if (mSafeLevel == 1) {
            mPaint.setColor(getResources().getColor(R.color.colorEF2357));
            end = 0.33f;
        } else if (mSafeLevel == 2) {
            mPaint.setColor(getResources().getColor(R.color.colorEF5923));
            end = 0.67f;
        } else if (mSafeLevel == 3) {
            mPaint.setColor(getResources().getColor(R.color.color24D95F));
            end = 1f;
        } else {
            invalidate();
            return;
        }
        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ValueAnimator.ofFloat(0, end);
        mAnimator.setDuration((long) (800 * end));
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mSafeLevel == 1 || mSafeLevel == 2 || mSafeLevel == 3) {
            int height = getHeight();
            if (mPaint.getStrokeWidth() != height) {
                mPaint.setStrokeWidth(height);
            }
            float value = (float) mAnimator.getAnimatedValue();
            canvas.drawLine(0, height / 2f, value * getWidth(), height / 2f, mPaint);
        }
    }
}
