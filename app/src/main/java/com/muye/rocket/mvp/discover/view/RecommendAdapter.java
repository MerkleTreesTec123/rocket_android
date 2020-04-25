package com.muye.rocket.mvp.discover.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DimensionTool;
import com.makeramen.roundedimageview.RoundedImageView;
import com.muye.rocket.R;
import com.muye.rocket.entity.DiscoverEntity;
import com.muye.rocket.mvp.WebBrowser;
import com.muye.rocket.tools.GlideLoadUtils;
import com.muye.rocket.widget.dialog.DappDialog;

import java.util.ArrayList;
import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<DiscoverEntity> mData;
    private Context mContext;
    private int mDp2px16;

    public RecommendAdapter(Context context) {
        this.mContext = context;
        mDp2px16 = DimensionTool.dp2px(context, 16);
    }

    public void setData(List<DiscoverEntity> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                DiscoverEntity entity = data.get(i);
                if (entity != null) {
                    mData.add(entity);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_discover_recommend_child, viewGroup, false);
        return new ViewHolder(viewGroup.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        RoundedImageView imageView = holder.getView(R.id.cover_image_view);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        int itemCount = getItemCount();
        if (i == itemCount - 1) {
            layoutParams.leftMargin = mDp2px16;
            layoutParams.rightMargin = mDp2px16;
        } else {
            layoutParams.leftMargin = mDp2px16;
            layoutParams.rightMargin = 0;
        }
        DiscoverEntity entity = null;
        if (mData != null && mData.size() > i) {
            entity = mData.get(i);
        }
        if (entity != null) {
            GlideLoadUtils.getInstance().glideLoad(imageView.getContext(), entity.getThumb(), imageView);
            holder.setText(R.id.title_text_view, entity.getTitle());
        } else {
            GlideLoadUtils.getInstance().glideLoad(imageView.getContext(), "", imageView);
            holder.setText(R.id.title_text_view, "");
        }

        DiscoverEntity finalEntity = entity;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalEntity == null) return;
                showMessageDialog(finalEntity.getTitle(), finalEntity.getLianjie());
            }
        });
    }

    private void showMessageDialog(String name, String url) {
        DappDialog dappDialog = new DappDialog(mContext);
        dappDialog.setDappName(name);
        dappDialog.setOnOkButtonClickListener(new DappDialog.OnOkButtonClickListener() {
            @Override
            public void onOkButtonClick(DappDialog dialog) {
                dialog.dismiss();
                WebBrowser.openWebBrowser(mContext, url, name);
            }
        }).show();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
