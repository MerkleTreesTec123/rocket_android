package com.muye.rocket.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import com.ifenduo.common.tool.DimensionTool;

public class DragFloatActionButton extends AppCompatImageView {

    private static final String TAG = "DragButton";
    private int parentHeight;
    private int parentWidth;

    private int dp2px16;
    private int statusBarHeight;
    private int lastX;
    private int lastY;

    private boolean isDrag;
    private ViewGroup parent;

    public DragFloatActionButton(Context context) {
        super(context);
        dp2px16 = DimensionTool.dp2px(context, 10);
        statusBarHeight = DimensionTool.getStatusBarHeight(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        dp2px16 = DimensionTool.dp2px(context, 10);
        statusBarHeight = DimensionTool.getStatusBarHeight(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dp2px16 = DimensionTool.dp2px(context, 10);
        statusBarHeight = DimensionTool.getStatusBarHeight(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                this.setAlpha(0.9f);
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                this.setAlpha(0.9f);
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance > 2 && !isDrag) {
                    isDrag = true;
                }

                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < dp2px16 ? dp2px16 : x > parentWidth - getWidth() - dp2px16 ? parentWidth - getWidth() - dp2px16 : x;
                y = y < (dp2px16 + statusBarHeight) ? (dp2px16 + statusBarHeight) : y + getHeight() + dp2px16 > parentHeight ? parentHeight - getHeight() - dp2px16 : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    //恢复按压效果
                    moveHide(rawX);
                }
                break;
        }
        return isDrag||super.onTouchEvent(event);
    }


    private void moveHide(int rawX) {
        if (rawX >= parentWidth / 2) {
            //靠右吸附
            animate().setInterpolator(new DecelerateInterpolator())
                    .setDuration(300)
                    .xBy(parentWidth - getWidth() - getX() - dp2px16)
                    .start();
        } else {
            //靠左吸附
            animate().setInterpolator(new DecelerateInterpolator())
                    .setDuration(300)
                    .xBy(dp2px16 - getX())
                    .start();

        }
    }

}
