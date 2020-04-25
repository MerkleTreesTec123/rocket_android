package com.muye.rocket.widget.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.muye.rocket.R;
import com.muye.rocket.entity.c2c.C2CCoin;
import com.zyyoona7.popup.EasyPopup;

import java.util.ArrayList;
import java.util.List;

public class C2CCoinPicker extends EasyPopup {

    RecyclerView mRecyclerView;
    View mBackgroundView;
    List<C2CCoin> mCoinList;
    Context mContext;

    String mCheckedID;
    CoinAdapter mAdapter;
    OnCoinCheckedListener mOnCoinCheckedListener;


    public static C2CCoinPicker create(Context context, int width, int height) {
        return new C2CCoinPicker(context, width, height);
    }

    private C2CCoinPicker(Context context, int width, int height) {
        super(context);
        mContext = context;
        setContentView(R.layout.c2c_home_coin_picker, width, height);
        setBackgroundDimEnable(false);
        setFocusAndOutsideEnable(true);
        apply();
    }

    @Override
    protected void onPopupWindowViewCreated(View contentView) {
        super.onPopupWindowViewCreated(contentView);
        mRecyclerView = contentView.findViewById(R.id.recycler_view);
        mBackgroundView = contentView.findViewById(R.id.background_view);
        mAdapter = new CoinAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public C2CCoinPicker setOnCoinCheckedListener(OnCoinCheckedListener onCoinCheckedListener) {
        mOnCoinCheckedListener = onCoinCheckedListener;
        return this;
    }

    public void setData(List<C2CCoin> coinList) {
        if (mCoinList == null) {
            mCoinList = new ArrayList<>();
        } else {
            mCoinList.clear();
        }

        if (coinList != null && coinList.size() > 0) {
            for (int i = 0; i < coinList.size(); i++) {
                C2CCoin coin = coinList.get(i);
                if (coin != null) {
                    mCoinList.add(coin);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setCheckedID(String checkedID) {
        mCheckedID = checkedID;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CoinAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.c2c_item_home_coin_picker, viewGroup, false);
            return new ViewHolder(mContext, itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            C2CCoin coin = (mCoinList == null || i >= mCoinList.size()) ? null : mCoinList.get(i);
            viewHolder.setText(R.id.title_text_view, coin == null ? "" : coin.getCoinname());
            viewHolder.setVisible(R.id.hook_image_view, (coin != null && !TextUtils.isEmpty(mCheckedID) && mCheckedID.equals(coin.getId())));
            viewHolder.setVisible(R.id.divider25, (mCoinList != null && i < mCoinList.size() - 1));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (coin == null) return;
                    mCheckedID = coin.getId();
                    notifyDataSetChanged();
                    if (mOnCoinCheckedListener != null) {
                        mOnCoinCheckedListener.onCoinChecked(coin, C2CCoinPicker.this);
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return mCoinList == null ? 0 : mCoinList.size();
        }
    }

    public interface OnCoinCheckedListener {
        void onCoinChecked(C2CCoin coin, C2CCoinPicker coinPicker);
    }
}
