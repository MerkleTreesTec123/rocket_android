package com.ifenduo.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_common.R;

/**
 * Created by yangxuefeng on 16/10/22.
 */

public class NormalLoadFootView extends FrameLayout implements ILoadFootView {
    /**
     * 正在加载
     */
    public final static int STATE_LOADING=1;
    /**
     * 闲置状态
     */
    public final static int STATE_IDLE=2;
    /**
     * 没有更多了
     */
    public final static int STATE_NO_MORE=3;

    private int   mState;

    private ProgressBar mProgressBar;

    private TextView mStatusTextView;

    public NormalLoadFootView(Context context) {
        super(context);
        initializer();
    }

    public NormalLoadFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializer();
    }

    public NormalLoadFootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializer();
    }

    private void initializer(){
        mProgressBar=new ProgressBar(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.CENTER;
        addView(mProgressBar,layoutParams);

        mStatusTextView=new TextView(getContext());
        addView(mStatusTextView,layoutParams);

        setViewState(STATE_IDLE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), DimensionTool.dp2px(getContext(),80));
    }

    public void setViewState(int state){
        mState=state;
        switch (mState){
            case STATE_IDLE:
                setViewIdle();
                break;
            case STATE_LOADING:
                setViewLoading();
                break;
            case STATE_NO_MORE:
                setViewNoMore();
                break;
            default:
                mState=STATE_IDLE;
                setViewIdle();
                break;
        }
    }

    public int getViewState(){
        return mState;
    }

    @Override
    public void setViewLoading() {
        mProgressBar.setVisibility(VISIBLE);
        mStatusTextView.setVisibility(GONE);
    }

    @Override
    public void setViewIdle() {
        mProgressBar.setVisibility(GONE);
        mStatusTextView.setVisibility(GONE);
    }

    @Override
    public void setViewNoMore() {
        mProgressBar.setVisibility(GONE);
        mStatusTextView.setVisibility(VISIBLE);
        mStatusTextView.setText(getResources().getText(R.string.load_footer_no_more));
    }
}
