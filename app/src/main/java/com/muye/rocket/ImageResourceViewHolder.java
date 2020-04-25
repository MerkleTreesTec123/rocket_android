package com.muye.rocket;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhpan.bannerview.holder.ViewHolder;

public class ImageResourceViewHolder implements ViewHolder<Object> {

    private final Context context;

    public ImageResourceViewHolder(Context context) {
        this.context = context;
    }

    @Override
    public int getLayoutId() {
//        return R.layout.item_slide_mode;
        return R.layout.banner_item;
    }

    @Override
    public void onBind(View itemView, Object data, int position, int size) {
//        ImageView imageView = itemView.findViewById(R.id.banner_image);
        ImageView imageView = itemView.findViewById(R.id.iv_img);
        Glide.with(context).load(data).into(imageView);
    }

}