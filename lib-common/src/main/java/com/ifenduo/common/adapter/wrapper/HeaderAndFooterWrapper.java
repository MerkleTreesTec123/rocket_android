package com.ifenduo.common.adapter.wrapper;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.adapter.utils.WrapperUtils;
import com.ifenduo.lib_common.R;

public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    private static final int BASE_ITEM_TYPE_EMPTY = 300000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;
    private View mEmptyView;
    private @DrawableRes
    int mEmptyImage;
    private @StringRes
    int mEmptyString;
    private @ColorInt
    int mEmptyStringColor=Color.BLACK;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;

        } else if (mFootViews.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            return holder;
        } else if (viewType == BASE_ITEM_TYPE_EMPTY) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mEmptyView);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return getFooterTypeByPosition(position);
        } else if (isEmptyViewPos(position)) {
            return BASE_ITEM_TYPE_EMPTY;
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        if (isFooterViewPos(position)) {
            return;
        }
        if (isEmptyViewPos(position)) {
            if (holder instanceof ViewHolder) {
                ImageView imageView = ((ViewHolder) holder).getView(R.id.empty_image_view);
                TextView emptyTextView = ((ViewHolder) holder).getView(R.id.empty_text_view);

                if (mEmptyImage != 0) {
                    imageView.setImageResource(mEmptyImage);
                }

                if (mEmptyString != 0) {
                    emptyTextView.setText(mEmptyString);
                    emptyTextView.setTextColor(mEmptyStringColor);
                }
            }

            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        if (mEmptyView == null) {
            return getHeadersCount() + getFootersCount() + getRealItemCount();
        } else {
            if (getRealItemCount() == 0) {
                return getHeadersCount() + 1;
            } else {
                return getHeadersCount() + getFootersCount() + getRealItemCount();
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (isEmptyViewPos(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position) || isEmptyViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        if (mEmptyView == null) {
            return position >= getHeadersCount() + getRealItemCount();
        } else {
            if (getRealItemCount() == 0) {
                return position >= getHeadersCount() + getRealItemCount() + 1;
            } else {
                return position >= getHeadersCount() + getRealItemCount();
            }
        }
    }

    private boolean isEmptyViewPos(int position) {
        if (mEmptyView == null) return false;
        if (getRealItemCount() == 0) {
            return position == getHeadersCount();
        } else {
            return false;
        }
    }

    public void addHeaderView(View view) {
        if (view == null) return;
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view) {
        if (view == null) return;
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    private int getFooterTypeByPosition(int position) {
        if (mEmptyView == null) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        } else if (getRealItemCount() == 0) {
            return mFootViews.keyAt(position - 1 - getHeadersCount());
        } else {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setEmptyImage(@DrawableRes int emptyImage) {
        this.mEmptyImage = emptyImage;
    }

    public void setEmptyString(@StringRes int emptyString) {
        this.mEmptyString = emptyString;
    }

    public void setEmptyStringColor(@ColorInt int emptyStringColor) {
        this.mEmptyStringColor = emptyStringColor;
    }


    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }


}
