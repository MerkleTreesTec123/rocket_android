package com.ifenduo.lib_bottomnavigation;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class BottomNavigation extends LinearLayout implements ViewPager.OnPageChangeListener, View.OnClickListener, NormalTab.OnMessageDragListener {
    private ViewPager mViewPager;
    private OnItemTabClickCallBack mOnItemTabClickCallBack;
    private OnItemDragListener mOnItemDragListener;
    private int mCheckedIndex;

    public BottomNavigation(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public BottomNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    public BottomNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
    }

    public BottomNavigation addTab(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, @ColorInt
            int color, @ColorInt int checkedColor, @StringRes int title) {
        BaseTab tab = new NormalTab(getContext());
        tab.initialize(drawableRes, checkedDrawableRes, color, checkedColor, title);
        tab.setChecked(false);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        tab.setLayoutParams(layoutParams);
        tab.setOnClickListener(this);
        addView(tab);
        return this;
    }

    public BottomNavigation addTab(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, @ColorInt
            int color, @ColorInt int checkedColor, String title) {
        BaseTab tab = new NormalTab(getContext());
        tab.initialize(drawableRes, checkedDrawableRes, color, checkedColor, title);
        tab.setChecked(false);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        tab.setLayoutParams(layoutParams);
        tab.setOnClickListener(this);
        ((NormalTab)tab).setOnDragListener(this);
        addView(tab);
        return this;
    }

    public void setupWithViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        if (mViewPager != null) mViewPager.addOnPageChangeListener(this);
    }

    public void setOnItemTabClickCallBack(OnItemTabClickCallBack onItemTabClickCallBack) {
        mOnItemTabClickCallBack = onItemTabClickCallBack;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCheckedTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v instanceof BaseTab) {
            int index = indexOfChild(v);
            if (mOnItemTabClickCallBack != null) {
                if (mOnItemTabClickCallBack.onItemTabClick(index, (BaseTab) v)) return;//返回true拦截
            }

            if (mViewPager != null) {
                //切换页面 Tab状态在onPageSelected方法中设置
                mViewPager.setCurrentItem(index);
            } else {
                //设置Tab状态
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childView = getChildAt(i);
                    if (childView instanceof BaseTab) {
                        ((BaseTab) childView).setChecked(childView == v);
                    }
                }
            }
        }
    }

    @Override
    public void onDragOut(BaseTab tab) {
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onItemDrag(indexOfChild(tab));
        }
    }

    public interface OnItemTabClickCallBack {
        boolean onItemTabClick(int index, BaseTab tab);
    }

    public void setMessageNumber(int index, int number) {
        if (index >= getChildCount() || !(getChildAt(index) instanceof BaseTab)) return;
        BaseTab tab = (BaseTab) getChildAt(index);
        tab.setMessageNumber(number);
    }

    public void setHasMessage(int index, boolean hasMessage) {
        if (index >= getChildCount() || !(getChildAt(index) instanceof BaseTab)) return;
        BaseTab tab = (BaseTab) getChildAt(index);
        tab.setHasMessage(hasMessage);
    }

    public void setCheckedTab(int index) {
        int childCount = getChildCount();
        if (index >= childCount) return;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof BaseTab) {
                ((BaseTab) childView).setChecked(i == index);
            }
        }
    }

    public interface OnItemDragListener {
        void onItemDrag(int index);
    }


    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        mOnItemDragListener = onItemDragListener;
    }
}
