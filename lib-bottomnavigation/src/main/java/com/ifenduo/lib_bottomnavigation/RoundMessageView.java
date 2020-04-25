package com.ifenduo.lib_bottomnavigation;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Locale;

/***
 * @author sirLL
 * @email 1725790539@qq.com
 * create at 2018/5/15 下午2:49
 * github https://github.com/sirLL
 * description:
 */
public class RoundMessageView extends FrameLayout{
    private final View mOval;
    private final DragPointView mMessages;

    private int mMessageNumber;
    private boolean mHasMessage;

    public RoundMessageView(Context context) {
        this(context, null);
    }

    public RoundMessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_round_message, this, true);
        mOval = findViewById(R.id.oval);
        mMessages = findViewById(R.id.msg);
        mMessages.setTypeface(Typeface.DEFAULT_BOLD);
        mMessages.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
    }

    public void setMessageNumber(int number) {
        mMessageNumber = number;
        if (mMessageNumber > 0) {
            mOval.setVisibility(View.INVISIBLE);
            mMessages.setVisibility(View.VISIBLE);

            if (mMessageNumber < 10) {
                mMessages.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            } else {
                mMessages.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
            }

            if (mMessageNumber <= 99) {
                mMessages.setText(String.format(Locale.ENGLISH, "%d", mMessageNumber));
            } else {
                mMessages.setText(String.format(Locale.ENGLISH, "%d+", 99));
            }
        } else {
            mMessages.setVisibility(View.INVISIBLE);
            if (mHasMessage) {
                mOval.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setHasMessage(boolean hasMessage) {
        mHasMessage = hasMessage;

        if (hasMessage) {
            mOval.setVisibility(mMessageNumber > 0 ? View.INVISIBLE : View.VISIBLE);
        } else {
            mOval.setVisibility(View.INVISIBLE);
        }
    }

    public void tintMessageBackground(@ColorInt int color) {
        Drawable drawable = Utils.tint(ContextCompat.getDrawable(getContext(), R.drawable.shape_round_message_bg), color);
        ViewCompat.setBackground(mOval, drawable);
        ViewCompat.setBackground(mMessages, drawable);
    }

    public void setMessageNumberColor(@ColorInt int color) {
        mMessages.setTextColor(color);
    }

    public int getMessageNumber() {
        return mMessageNumber;
    }

    public boolean hasMessage() {
        return mHasMessage;
    }

    public void setDragListener(DragPointView.OnDragCallback onDragCallback){
        mMessages.setDragListencer(onDragCallback);
    }




}
