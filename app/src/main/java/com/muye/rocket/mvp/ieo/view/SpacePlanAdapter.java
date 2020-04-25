package com.muye.rocket.mvp.ieo.view;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ifenduo.common.adapter.base.ViewHolder;
import com.muye.rocket.R;
import com.muye.rocket.entity.ieo.IEOHomeEntity;
import java.util.List;
import io.reactivex.annotations.NonNull;

public class SpacePlanAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<IEOHomeEntity.DefaultMostBean> mData;
    String mCheckedID;
    OnItemCheckedCallBack mOnItemCheckedCallBack;

    public void setData(List<IEOHomeEntity.DefaultMostBean> data, String defCheckedID) {
        this.mData = data;
        IEOHomeEntity.DefaultMostBean defBean = null;
        if (!TextUtils.isEmpty(mCheckedID)) {
            if (mData != null && mData.size() > 0) {
                for (int i = 0; i < mData.size(); i++) {
                    IEOHomeEntity.DefaultMostBean bean = mData.get(i);
                    if (bean != null && mCheckedID.equals(bean.getId())) {
                        defBean = bean;
                    }
                }
            }
        }
        if(defBean==null){
            mCheckedID = defCheckedID;
        }

        notifyDataSetChanged();
    }

    public void setOnItemCheckedCallBack(OnItemCheckedCallBack onItemCheckedCallBack) {
        mOnItemCheckedCallBack = onItemCheckedCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_space_plan, viewGroup, false);
        return new ViewHolder(viewGroup.getContext(), itemView);
    }

    public IEOHomeEntity.DefaultMostBean getCheckedItem() {
        if (!TextUtils.isEmpty(mCheckedID) && mData != null && mData.size() > 0) {
            for (int i = 0; i < mData.size(); i++) {
                IEOHomeEntity.DefaultMostBean checkedBean = mData.get(i);
                if (checkedBean != null && mCheckedID.equals(checkedBean.getId())) {
                    return checkedBean;
                }
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        IEOHomeEntity.DefaultMostBean mostBean = null;
        if (mData != null && mData.size() > i) {
            mostBean = mData.get(i);
        }
        holder.setText(R.id.title_text_view, mostBean == null ? "--" : mostBean.getCoin_name());
        holder.setText(R.id.num_text_view, mostBean == null ? "--" : mostBean.getAmount());
        holder.itemView.setSelected(mostBean != null && !TextUtils.isEmpty(mCheckedID) && mCheckedID.equals(mostBean.getId()));
        IEOHomeEntity.DefaultMostBean finalMostBean = mostBean;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalMostBean != null) {
                    mCheckedID = finalMostBean.getId();
                    if (mOnItemCheckedCallBack != null) {
                        mOnItemCheckedCallBack.onItemChecked(finalMostBean);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnItemCheckedCallBack {
        void onItemChecked(IEOHomeEntity.DefaultMostBean mostBean);
    }

}
