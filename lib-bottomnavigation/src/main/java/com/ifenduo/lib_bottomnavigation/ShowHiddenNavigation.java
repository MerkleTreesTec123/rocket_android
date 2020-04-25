package com.ifenduo.lib_bottomnavigation;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

public class ShowHiddenNavigation extends LinearLayout implements View.OnClickListener, NormalTab.OnMessageDragListener {

    FragmentChangeManager mFragmentChangeManager;
    OnItemTabClickCallBack mOnItemTabClickCallBack;
    OnItemDragListener mOnItemDragListener;

    public ShowHiddenNavigation(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public ShowHiddenNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    public ShowHiddenNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
    }

    public ShowHiddenNavigation addTab(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, @ColorInt
            int color, @ColorInt int checkedColor, @StringRes int title) {
        BaseTab tab ;
        if (title == 0) {
            tab = new ImageTab(getContext());
        } else {
            tab = new NormalTab(getContext());
        }
        tab.initialize(drawableRes, checkedDrawableRes, color, checkedColor, title);
        tab.setChecked(false);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        tab.setLayoutParams(layoutParams);
        tab.setOnClickListener(this);
        addView(tab);
        return this;
    }

    public ShowHiddenNavigation addTab(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, @ColorInt
            int color, @ColorInt int checkedColor, String title) {
        BaseTab tab ;
        if (TextUtils.isEmpty(title)) {
            tab = new ImageTab(getContext());
        } else {
            tab = new NormalTab(getContext());
        }
        tab.initialize(drawableRes, checkedDrawableRes, color, checkedColor, title);
        tab.setChecked(false);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        tab.setLayoutParams(layoutParams);
        tab.setOnClickListener(this);
        if (tab instanceof NormalTab) {
            ((NormalTab) tab).setOnDragListener(this);
        }
        addView(tab);
        return this;
    }

    public void setupFragments(FragmentManager fm, int containerViewId, List<Fragment> fragments) {
        mFragmentChangeManager = new FragmentChangeManager(fm, containerViewId, fragments);
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

    public void setOnItemTabClickCallBack(OnItemTabClickCallBack onItemTabClickCallBack) {
        this.mOnItemTabClickCallBack = onItemTabClickCallBack;
    }

    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        this.mOnItemDragListener = onItemDragListener;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof BaseTab) {
            int index = indexOfChild(v);
            if (mOnItemTabClickCallBack != null) {
                if (mOnItemTabClickCallBack.onItemTabClick(index, (BaseTab) v)) return;//返回true拦截
            }
            //设置Tab状态
            setCheckedTab(index);
        }
    }

    public void setCheckedTab(int index) {
        int childCount = getChildCount();
        if (index >= childCount) return;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof BaseTab) {
                ((BaseTab) childView).setChecked(i == index);
                if (mFragmentChangeManager != null) {
                    //切换页面 Tab状态在onPageSelected方法中设置
                    mFragmentChangeManager.setFragments(index);
                }
            }
        }
    }

    public int getCheckedTab() {
        if (mFragmentChangeManager != null) {
            return mFragmentChangeManager.getCurrentTab();
        } else {
            return -1;
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

    public interface OnItemDragListener {
        void onItemDrag(int index);
    }
}
