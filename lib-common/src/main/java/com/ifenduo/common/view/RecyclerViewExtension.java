package com.ifenduo.common.view;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.ifenduo.common.adapter.wrapper.HeaderAndFooterWrapper;

/**
 * Created by yangxuefeng on 16/10/22.
 */

public class RecyclerViewExtension extends RecyclerView {

    private LoadingListener         mLoadingListener;
    private HeaderAndFooterWrapper  mHeaderAndFooterWrapper;
    private boolean                 isSupportLoadMore=true;
    private boolean                 isTriggerLoad;
    public RecyclerViewExtension(Context context) {
        super(context);
    }

    public RecyclerViewExtension(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewExtension(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface LoadingListener {
        void onLoadMore();
    }

    public void setLoadingListener(LoadingListener l) {
        mLoadingListener=l;
    }

    public void setSupportLoadMore(boolean isSupportLoadMore){
        this.isSupportLoadMore=isSupportLoadMore;
    }
    public void setTriggerLoad(boolean isTriggerLoad){
        this.isTriggerLoad=isTriggerLoad;
    }

    public boolean getSupportLoadMore(){
        return isSupportLoadMore;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
//            Log.i("TAG","itemCount="+layoutManager.getItemCount()+" childCount="+layoutManager.getChildCount()+" lastVisibleItemPosition="+lastVisibleItemPosition);

            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && layoutManager.getItemCount() > layoutManager.getChildCount() && isSupportLoadMore && isTriggerLoad) {
                    mLoadingListener.onLoadMore();
            }

        }
    }
}
