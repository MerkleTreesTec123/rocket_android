package com.muye.rocket.mvp.exchange.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.entity.exchange.ExcOrder;

import java.util.ArrayList;
import java.util.List;

public abstract class ExchangeAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "ExchangeAdapter";
    private static final int ITEM_TYPE_HEADER = 1;
    private static final int ITEM_TYPE_ORDER = 2;
    private static final int ITEM_TYPE_EMPTY = 3;

    private List<ExcOrder> mData;
    private String mSellName;
    private String mBuyName;
    private OnCancelButtonClickListener mOnCancelButtonClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        ViewHolder holder = null;
        if (itemType == ITEM_TYPE_HEADER) {
            holder = new HeaderHolder(viewGroup.getContext(), getHeaderView());
        } else if (itemType == ITEM_TYPE_EMPTY) {
            holder = new EmptyHolder(viewGroup.getContext(), getEmptyView());
        } else if (itemType == ITEM_TYPE_ORDER) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exc_item_exchange, viewGroup, false);
            holder = new OrderHolder(viewGroup.getContext(), itemView);
        }
        return holder;
    }

    public void setData(List<ExcOrder> data, String sellName, String buyName) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        if (data != null && data.size() > 0) {
            mData.addAll(data);
        }
        mSellName = sellName;
        mBuyName = buyName;
        notifyDataSetChanged();
    }

    public void setOnCancelButtonClickListener(OnCancelButtonClickListener onCancelButtonClickListener) {
        mOnCancelButtonClickListener = onCancelButtonClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof OrderHolder) {
            TextView typeTextView = holder.getView(R.id.type_text_view);
            TextView cancelButton = holder.getView(R.id.cancel_button);
            ExcOrder order = null;
            if (mData != null && mData.size() > position - 1) {
                order = mData.get(position - 1);
            }
            if (order != null) {

                //手续费单位是交易获得的币
                if ("1".equals(order.getType())) {//卖
                    typeTextView.setText(R.string.exc_sell);
                    typeTextView.setBackgroundResource(R.drawable.exc_ic_order_type_sell);
                    holder.setText(R.id.fee_text_view, NumberUtil.formatMoney(order.getFees()) + mBuyName);
                } else {//买
                    typeTextView.setText(R.string.exc_buy);
                    typeTextView.setBackgroundResource(R.drawable.exc_ic_order_type_buy);
                    holder.setText(R.id.fee_text_view, NumberUtil.formatMoney(order.getFees()) + mSellName);
                }

                if (order.getCount() == order.getLeftcount()) {//没有交易
                    holder.setText(R.id.price_text_view_, "--");
                } else {
                    holder.setText(R.id.price_text_view_, NumberUtil.formatMoney(order.getSuccessamount() / (order.getCount() - order.getLeftcount())));
                }

//              Going(1, "未成交"),    // 未成交
//              PartDeal(2, "部分成交"),   // 部分成交
//              AllDeal(3, "完全成交"),   // 完全成交
//              WAITCancel(4, "撤单处理中"),  // 等待撤单
//              Cancel(5, "已撤销");    // 撤销
                if ("1".equals(order.getStatus()) || "2".equals(order.getStatus())) {
                    cancelButton.setVisibility(View.VISIBLE);
                } else {
                    cancelButton.setVisibility(View.GONE);
                }

                holder.setText(R.id.sell_text_view, mSellName);
                holder.setText(R.id.buy_text_view, "/" + mBuyName);
                holder.setText(R.id.time_text_view, order.getTime());
                holder.setText(R.id.price_text_view, NumberUtil.formatMoney(order.getPrice()));
                holder.setText(R.id.num_text_view, NumberUtil.formatMoney(order.getCount()));
                holder.setText(R.id.num_text_view_, NumberUtil.formatMoney(order.getCount() - order.getLeftcount()));

                ExcOrder finalOrder = order;
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCancelButtonClickListener != null) {
                            mOnCancelButtonClickListener.oCancelButtonClick(finalOrder.getId());
                        }
                    }
                });
            } else {
                holder.setText(R.id.sell_text_view, "--");
                holder.setText(R.id.buy_text_view, "/--");
                holder.setText(R.id.time_text_view, "--");
                holder.setText(R.id.price_text_view, "--");
                holder.setText(R.id.num_text_view, "--");
                holder.setText(R.id.num_text_view_, "--");
                holder.setText(R.id.price_text_view_, "--");
                holder.setText(R.id.fee_text_view, "--");
                typeTextView.setBackgroundResource(R.drawable.exc_ic_order_type_sell);
                holder.setText(R.id.fee_text_view, "--");
                cancelButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mData == null || mData.size() == 0) ? 2 : mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else {
            return (mData == null || mData.size() == 0) ? ITEM_TYPE_EMPTY : ITEM_TYPE_ORDER;
        }
    }

    class HeaderHolder extends ViewHolder {

        public HeaderHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    class OrderHolder extends ViewHolder {
        public OrderHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    class EmptyHolder extends ViewHolder {

        public EmptyHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    abstract View getHeaderView();

    abstract View getEmptyView();

    public interface OnCancelButtonClickListener {
        void oCancelButtonClick(String orderID);
    }
}
