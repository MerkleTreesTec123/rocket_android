package com.ifenduo.lib_bottomnavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;



/***
 * @author sirLL
 * @email 1725790539@qq.com
 * create at 2018/5/15 上午11:14
 * github https://github.com/sirLL
 * description:
 */
public class NormalTab extends BaseTab implements DragPointView.OnDragCallback {
    private OnMessageDragListener mOnDragListener;

    public NormalTab(@NonNull Context context) {
        this(context, null);
    }

    public NormalTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_tab_normal_bottom_navigation, this, true);
        mIconImageView = findViewById(R.id.icon);
        mTitleTextView = findViewById(R.id.title);
        mMessagesView = findViewById(R.id.messages);
        mMessagesView.setDragListener(this);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mIconImageView.setImageResource(mCheckedDrawable);
            mTitleTextView.setTextColor(mCheckedTextColor);
        } else {
            mIconImageView.setImageResource(mDefaultDrawable);
            mTitleTextView.setTextColor(mDefaultTextColor);
        }
    }

    @Override
    public void setMessageNumber(int number) {
        mMessagesView.setMessageNumber(number);
    }

    @Override
    public void setHasMessage(boolean hasMessage) {
        mMessagesView.setHasMessage(hasMessage);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }


    @Override
    public void onDragOut() {
        if (mOnDragListener != null) {
            mOnDragListener.onDragOut(this);
            mMessagesView.setMessageNumber(0);
            mMessagesView.setHasMessage(false);
        }
    }

    public interface OnMessageDragListener {
        void onDragOut(BaseTab tab);
    }


    public void setOnDragListener(OnMessageDragListener onDragListener) {
        mOnDragListener = onDragListener;
    }
}
