package com.muye.rocket.mvp.discover.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.muye.rocket.R;
import com.muye.rocket.entity.DiscoverCat;
import com.muye.rocket.entity.DiscoverEntity;
import com.muye.rocket.entity.DiscoverHomeEntity;
import com.muye.rocket.mvp.WebBrowser;
import com.muye.rocket.tools.GlideLoadUtils;
import com.muye.rocket.widget.dialog.DappDialog;

import java.util.ArrayList;
import java.util.List;

public abstract class DiscoverAdapter extends RecyclerView.Adapter<ViewHolder> {
    public static final int ITEM_TYPE_BANNER = 1;
    public static final int ITEM_TYPE_RECOMMEND = 2;
    public static final int ITEM_TYPE_TITLE = 3;
    public static final int ITEM_TYPE_CONTENT = 4;

    Context mContext;
    List<String> mBannerData;
    List<DiscoverEntity> mRecommendData;
    List<ListEntity> mListData;

    public DiscoverAdapter(Context context) {
        this.mContext = context;
    }

    public void setBannerData(List<String> bannerData) {
        mBannerData = bannerData;
        notifyDataSetChanged();
    }

    public void setDiscoverHomeData(List<DiscoverHomeEntity> discoverHomeEntityList) {
        if (mListData == null) {
            mListData = new ArrayList<>();
        } else {
            mListData.clear();
        }

        if (discoverHomeEntityList != null && discoverHomeEntityList.size() > 0) {
            for (int i = 0; i < discoverHomeEntityList.size(); i++) {
                DiscoverHomeEntity homeEntity = discoverHomeEntityList.get(i);
                if (homeEntity != null) {
                    DiscoverCat discoverCat = new DiscoverCat();
                    discoverCat.setId(homeEntity.getId());
                    discoverCat.setName(homeEntity.getName());
                    ListEntity titleEntity = new ListEntity(ITEM_TYPE_TITLE, discoverCat, null);
                    List<ListEntity> entityList = new ArrayList<>();
                    if (homeEntity.getList() != null && homeEntity.getList().size() > 0) {
                        for (int j = 0; j < homeEntity.getList().size(); j++) {
                            DiscoverEntity discoverEntity = homeEntity.getList().get(j);
                            if (discoverEntity != null) {
                                entityList.add(new ListEntity(ITEM_TYPE_CONTENT, null, discoverEntity));
                            }
                        }
                    }
                    if (entityList.size() > 0) {
                        mListData.add(titleEntity);
                        mListData.addAll(entityList);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setRecommendData(List<DiscoverEntity> recommendData) {
        if (mRecommendData == null) {
            mRecommendData = new ArrayList<>();
        } else {
            mRecommendData.clear();
        }
        if (recommendData != null && recommendData.size() > 0) {
            for (int i = 0; i < recommendData.size(); i++) {
                DiscoverEntity entity = recommendData.get(i);
                if (entity != null) {
                    mRecommendData.add(entity);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        ViewHolder holder = null;
        if (itemType == ITEM_TYPE_BANNER) {
            View view = getBannerView();
            holder = new BannerHolder(viewGroup.getContext(), view);
        } else if (itemType == ITEM_TYPE_RECOMMEND) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_discover_recommend, viewGroup, false);
            holder = new RecommendHolder(viewGroup.getContext(), itemView);
        } else if (itemType == ITEM_TYPE_TITLE) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_discover_title, viewGroup, false);
            holder = new TitleHolder(viewGroup.getContext(), itemView);
        } else if (itemType == ITEM_TYPE_CONTENT) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_discover_content, viewGroup, false);
            holder = new ContentHolder(viewGroup.getContext(), itemView);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof RecommendHolder) {
            ((RecommendHolder) holder).adapter.setData(mRecommendData);
            holder.setVisible(R.id.divider_view, position != 0);
        } else if (holder instanceof TitleHolder) {
            ListEntity entity = null;
            int position_ = position - getHeaderCount();
            if (position_ >= 0 && mListData != null && mListData.size() > position_) {
                entity = mListData.get(position_);
            }
            holder.setText(R.id.title_text_view, (entity == null && entity.getCat() != null) ? "" : entity.getCat().getName());
            holder.setVisible(R.id.divider_view, position != 0);

            ListEntity finalEntity = entity;
            holder.setOnClickListener(R.id.all_button, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String catID = "";
                    if (finalEntity != null && finalEntity.getCat() != null) {
                        catID = finalEntity.getCat().getId();
                    }
                    DiscoverMoreActivity.openDiscoverMoreActivity(mContext, catID);
                }

            });
        } else if (holder instanceof ContentHolder) {
            RoundedImageView imageView = holder.getView(R.id.cover_image_view);
            DiscoverEntity entity = null;
            int position_ = position - getHeaderCount();
            if (position_ >= 0 && mListData != null && mListData.size() > position_) {
                if (mListData.get(position_) != null) {
                    entity = mListData.get(position_).getDiscoverEntity();
                }
            }
            GlideLoadUtils.getInstance().glideLoad(imageView.getContext(), entity == null ? "" : entity.getThumb(), imageView);
            holder.setText(R.id.title_text_view, entity == null ? "" : entity.getTitle());
            holder.setText(R.id.content_text_view, entity == null ? "" : entity.getDescription());

            DiscoverEntity finalEntity = entity;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalEntity == null) return;
                    showMessageDialog(finalEntity.getTitle(), finalEntity.getLianjie());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (mBannerData != null && mBannerData.size() > 0) {
                return ITEM_TYPE_BANNER;
            } else if (mRecommendData != null && mRecommendData.size() > 0) {
                return ITEM_TYPE_RECOMMEND;
            }
        }
        if (mBannerData != null && mBannerData.size() > 0 && mRecommendData != null && mRecommendData.size() > 0 && position == 1) {
            return ITEM_TYPE_RECOMMEND;
        }

        int position_ = position - getHeaderCount();
        if (position_ >= 0 && mListData != null && mListData.size() > position_) {
            ListEntity entity = mListData.get(position_);
            if (entity != null) {
                return entity.getItemType();
            } else {
                return ITEM_TYPE_CONTENT;
            }
        }
        return super.getItemViewType(position);
    }

    public int getHeaderCount() {
        int headerCount = 0;
        if (mBannerData != null && mBannerData.size() > 0) {
            ++headerCount;
        }
        if (mRecommendData != null && mRecommendData.size() > 0) {
            ++headerCount;
        }
        return headerCount;
    }

    @Override
    public int getItemCount() {
        int count = getHeaderCount();
        if (mListData != null && mListData.size() > 0) {
            count = count + mListData.size();
        }
        return count;
    }

    class BannerHolder extends ViewHolder {
        public BannerHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    class RecommendHolder extends ViewHolder {
        private RecyclerView recyclerView;
        private RecommendAdapter adapter;

        public RecommendHolder(Context context, View itemView) {
            super(context, itemView);
            recyclerView = getView(R.id.recycler_view);
            adapter = new RecommendAdapter(context);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    class TitleHolder extends ViewHolder {
        public TitleHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    class ContentHolder extends ViewHolder {
        public ContentHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    class ListEntity {
        private int itemType;
        private DiscoverCat cat;
        private DiscoverEntity discoverEntity;

        public ListEntity(int itemType, DiscoverCat cat, DiscoverEntity discoverEntity) {
            this.itemType = itemType;
            this.cat = cat;
            this.discoverEntity = discoverEntity;
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public DiscoverCat getCat() {
            return cat;
        }

        public void setCat(DiscoverCat cat) {
            this.cat = cat;
        }

        public DiscoverEntity getDiscoverEntity() {
            return discoverEntity;
        }

        public void setDiscoverEntity(DiscoverEntity discoverEntity) {
            this.discoverEntity = discoverEntity;
        }
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

    abstract View getBannerView();
}
