package com.muye.rocket.mvp.me.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.entity.InviteRankEntity;

import java.util.List;

public class InviteRankedAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private List<InviteRankEntity.RewardBean> mData;
    private int mItemWidth;

    public InviteRankedAdapter(Context context) {
        this.mContext = context;
        int screenWidth = DimensionTool.getScreenWidth(context);
        int dp2px16 = DimensionTool.dp2px(context, 16);
        mItemWidth = (int) ((screenWidth - dp2px16 * 2) / 2f);
    }

    public void setData(List<InviteRankEntity.RewardBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invite_ranking, viewGroup, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = mItemWidth;
        itemView.setLayoutParams(layoutParams);
        return new ViewHolder(viewGroup.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        InviteRankEntity.RewardBean rewardBean = null;
        if (mData != null && mData.size() > i) {
            rewardBean = mData.get(i);
        }
        if (rewardBean != null) {
            viewHolder.setText(R.id.title_text_view, mContext.getString(R.string.i_accumulated_awards) + rewardBean.getCoin_name());
            viewHolder.setText(R.id.num_text_view, NumberUtil.formatMoney(rewardBean.getTotal()));
        } else {
            viewHolder.setText(R.id.title_text_view, mContext.getString(R.string.i_accumulated_awards));
            viewHolder.setText(R.id.num_text_view, "--");
        }

        InviteRankEntity.RewardBean finalRewardBean = rewardBean;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalRewardBean == null) return;
                CumulativeRewardActivity.openCumulativeRewardActivity(mContext, finalRewardBean.getCoin_id(), finalRewardBean.getCoin_name());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mData == null || mData.size() < 2) ? 2 : mData.size();
    }
}
