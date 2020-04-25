package com.muye.rocket.mvp.home.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.toast.ToastUtils;
import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.mvp.exc_wallet.view.ExcRechargeActivity;
import com.muye.rocket.mvp.exc_wallet.view.ExcWalletActivity;

public class AssetsAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final int ITEM_TYPE_ASSETS_WALLET = 1;
    private static final int ITEM_TYPE_C2C_WALLET = 2;
    private Context mContext;
    private int mItemSize;
    private ExcAssetsEntity mAssetsEntity;

    public AssetsAdapter(Context context) {
        this.mContext = context;
        int screenWidth = DimensionTool.getScreenWidth(context);
        mItemSize = (int) (screenWidth * (239f / 375));
    }

    public void setAssetsWallet(ExcAssetsEntity assetsEntity) {
        mAssetsEntity = assetsEntity;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = null;
        if (ITEM_TYPE_ASSETS_WALLET == i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_item_assets_wallet, viewGroup, false);
            viewHolder = new AssetsWalletHolder(viewGroup.getContext(), itemView);
        } else if (ITEM_TYPE_C2C_WALLET == i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_item_c2c_wallet, viewGroup, false);
            viewHolder = new C2CWalletHolder(viewGroup.getContext(), itemView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (viewHolder instanceof AssetsWalletHolder) {

            TextView titleTextView = viewHolder.getView(R.id.title_text_view);
            TextView numTextView = viewHolder.getView(R.id.num_text_view);
            if (MMKVTools.isShowAssetsWalletNum()) {
                titleTextView.setSelected(false);
                numTextView.setText(mAssetsEntity != null ? NumberUtil.formatRMBDown(mAssetsEntity.getNetassets()) : "--");
            } else {
                numTextView.setText("******");
                titleTextView.setSelected(true);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ExcWalletActivity.class);
                    mContext.startActivity(intent);
                }
            });

            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MMKVTools.changeIsShowAssetsWalletNum();
                    notifyDataSetChanged();
                }
            });

            viewHolder.setOnClickListener(R.id.qr_button, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExcRechargeActivity.openExcRechargeActivity(mContext, "");
                }
            });

            viewHolder.setOnClickListener(R.id.transfer_button, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(R.string.not_open);
                }
            });

        } else if (viewHolder instanceof C2CWalletHolder) {
            TextView titleTextView = viewHolder.getView(R.id.title_text_view);
            TextView numTextView = viewHolder.getView(R.id.num_text_view);

            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleTextView.setSelected(!titleTextView.isSelected());
                    if (titleTextView.isSelected()) {
                        numTextView.setText("******");
                    } else {
                        numTextView.setText("--");
                    }

                }
            });

            viewHolder.setOnClickListener(R.id.transfer_button, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(R.string.not_open);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_ASSETS_WALLET;
        } else {
            return ITEM_TYPE_C2C_WALLET;
        }
    }

    class AssetsWalletHolder extends ViewHolder {

        public AssetsWalletHolder(Context context, View itemView) {
            super(context, itemView);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = mItemSize;
            itemView.setLayoutParams(layoutParams);
        }
    }

    class C2CWalletHolder extends ViewHolder {

        public C2CWalletHolder(Context context, View itemView) {
            super(context, itemView);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = mItemSize;
            itemView.setLayoutParams(layoutParams);
        }
    }
}
