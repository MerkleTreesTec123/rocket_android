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
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;

import java.util.List;

public class CoinPicker extends Dialog implements View.OnClickListener {
    RecyclerView mRecyclerView;
    List<ExcAssetsEntity.WalletBean> mData;
    String mSelectedID;
    CoinAdapter mAdapter;
    OnCoinSelectedListener mOnCoinSelectedListener;
    BindWalletBean bindWalletBean;

    public CoinPicker(@NonNull Context context) {
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

    public void setData(List<ExcAssetsEntity.WalletBean> data) {
        mData = data;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public CoinPicker setWalletBean(BindWalletBean walletBean){
        this.bindWalletBean = walletBean;
        return this;
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
            ExcAssetsEntity.WalletBean walletBean = null;
            TextView titleTextView = holder.getView(R.id.title_text_view);
            if (mData != null && mData.size() > i) {
                walletBean = mData.get(i);
            }
            if (walletBean != null) {
                titleTextView.setText(walletBean.getShortName());
                if (!TextUtils.isEmpty(mSelectedID) && mSelectedID.equals(walletBean.getCoinId())) {
                    titleTextView.setTextColor(getContext().getResources().getColor(R.color.color8488F5));
                } else {
                    titleTextView.setTextColor(getContext().getResources().getColor(R.color.colorB7B7C4));
                }
            } else {
                holder.setText(R.id.title_text_view, "--");
                titleTextView.setTextColor(getContext().getResources().getColor(R.color.colorB7B7C4));
            }

            ExcAssetsEntity.WalletBean finalWalletBean = walletBean;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bindWalletBean != null){
                        mOnCoinSelectedListener.onCoinSelected(finalWalletBean,bindWalletBean);
                    }else {
                        if (finalWalletBean == null || TextUtils.isEmpty(finalWalletBean.getCoinId()) || mOnCoinSelectedListener == null)
                            return;
                        mOnCoinSelectedListener.onCoinSelected(finalWalletBean, bindWalletBean);
                        mSelectedID = finalWalletBean.getCoinId();
                    }
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
        void onCoinSelected(ExcAssetsEntity.WalletBean walletBean,BindWalletBean mWalletBean);
    }
}
