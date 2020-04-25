package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.muye.rocket.R;
import com.muye.rocket.entity.c2c.C2CCoin;

import java.util.ArrayList;
import java.util.List;

public class C2CCoinPicker extends Dialog implements View.OnClickListener {
    RecyclerView mRecyclerView;
    List<C2CCoin> mData;
    String mSelectedID;
    CoinAdapter mAdapter;
    OnCoinSelectedListener mOnCoinSelectedListener;

    public C2CCoinPicker(@NonNull Context context) {
        super(context, R.style.BottomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_coin_picker);
        mRecyclerView = findViewById(R.id.recycler_view);
        findViewById(R.id.cancel_button).setOnClickListener(this);
        mAdapter = new CoinAdapter();
        mRecyclerView.setAdapter(mAdapter);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public void setSelectedCoin(String coinID) {
        mSelectedID = coinID;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setData(List<C2CCoin> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                C2CCoin coin = data.get(i);
                if (coin != null) {
                    mData.add(coin);
                }
            }
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setOnCoinSelectedListener(OnCoinSelectedListener onCoinSelectedListener) {
        mOnCoinSelectedListener = onCoinSelectedListener;
    }

    class CoinAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_coin_picker, viewGroup, false);
            return new ViewHolder(getContext(), itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
            C2CCoin coin = null;
            TextView titleTextView = holder.getView(R.id.title_text_view);
            if (mData != null && mData.size() > i) {
                coin = mData.get(i);
            }
            if (coin != null) {
                titleTextView.setText(coin.getCoinname());
                if (!TextUtils.isEmpty(mSelectedID) && mSelectedID.equals(coin.getId())) {
                    titleTextView.setTextColor(getContext().getResources().getColor(R.color.c2cColor8488F5));
                } else {
                    titleTextView.setTextColor(getContext().getResources().getColor(R.color.colorB7B7C4));
                }
            } else {
                holder.setText(R.id.title_text_view, "--");
                titleTextView.setTextColor(getContext().getResources().getColor(R.color.colorB7B7C4));
            }

            C2CCoin finalCoin = coin;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalCoin == null || TextUtils.isEmpty(finalCoin.getId()) || mOnCoinSelectedListener == null)
                        return;
                    mOnCoinSelectedListener.onCoinSelected(finalCoin);
                    mSelectedID = finalCoin.getId();
                    dismiss();
                }
            });

        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }

    public interface OnCoinSelectedListener {
        void onCoinSelected(C2CCoin coin);
    }
}
