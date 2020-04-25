package com.muye.rocket.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class ExcProgressBar extends View {

    @ColorInt
    int mProgressColor;
    float mProgress;
    Paint mPaint;
    //动画
    ValueAnimator valueAnimator;
    float mAnimatedValue;

    public ExcProgressBar(Context context) {
        super(context);
        initPaint();
    }

    public ExcProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ExcProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        if (mProgressColor == 0) {
            mProgressColor = Color.BLACK;
        }
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(mProgressColor);
        }
    }

    public void setProgress(float progress) {
        if (mProgress != progress) {
            if (progress > 1) {
                mProgress = 1;
            } else if (progress < 0) {
                mProgress = 0;
            } else {
                mProgress = progress;
            }
            initPath();
        }
    }

    public void setProgressColor(int progressColor) {
        this.mProgressColor = progressColor;
        invalidate();
    }

    private void initPath() {
        mPaint.setStrokeWidth(getHeight());

        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        //实例化动画
        valueAnimator = ValueAnimator.ofFloat(0, mProgress);
        //平滑过渡
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) valueAnimator.getAnimatedValue();
                //重绘
                invalidate();
            }
        });

        valueAnimator.setDuration((long) (600 * mProgress));
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(getHeight());
        canvas.drawLine(getWidth(), getHeight() / 2f, (1f - mAnimatedValue) * getWidth(), getHeight() / 2f, mPaint);
    }
}
