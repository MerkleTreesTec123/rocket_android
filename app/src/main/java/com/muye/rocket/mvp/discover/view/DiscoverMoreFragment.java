package com.muye.rocket.mvp.discover.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.DiscoverEntity;
import com.muye.rocket.mvp.WebBrowser;
import com.muye.rocket.mvp.discover.contract.DiscoverListContract;
import com.muye.rocket.mvp.discover.presenter.DiscoverListPresenter;
import com.muye.rocket.tools.GlideLoadUtils;
import com.muye.rocket.widget.dialog.DappDialog;

import java.util.List;

public class DiscoverMoreFragment extends BaseListFragment<DiscoverEntity> implements DiscoverListContract.View {

    String mCatID;
    DiscoverListContract.Presenter mPresenter;

    public static DiscoverMoreFragment newInstance(String catID) {
        Bundle args = new Bundle();
        args.putString("catID", catID);
        DiscoverMoreFragment fragment = new DiscoverMoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_discover;
    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCatID = bundle.getString("catID", "");
        }
        super.initData();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, DiscoverEntity entity) {
        if (entity == null) return;
        showMessageDialog(entity.getTitle(),entity.getLianjie());
    }
    private void showMessageDialog(String name, String url) {
        DappDialog dappDialog = new DappDialog(getContext());
        dappDialog.setDappName(name);
        dappDialog.setOnOkButtonClickListener(new DappDialog.OnOkButtonClickListener() {
            @Override
            public void onOkButtonClick(DappDialog dialog) {
                dialog.dismiss();
                WebBrowser.openWebBrowser(getContext(), url, name);
            }
        }).show();
    }
    @Override
    public void onRequest(int page) {
        if (mPresenter == null) {
            mPresenter = new DiscoverListPresenter(this);
        }
        mPresenter.fetchDiscoverList(mCatID, page);
    }

    @Override
    public void onBindView(ViewHolder holder, DiscoverEntity entity, int position) {
        RoundedImageView imageView = holder.getView(R.id.cover_image_view);
        GlideLoadUtils.getInstance().glideLoad(imageView.getContext(), entity == null ? "" : entity.getThumb(), imageView);
        holder.setText(R.id.title_text_view, entity == null ? "" : entity.getTitle());
        holder.setText(R.id.content_text_view, entity == null ? "" : entity.getDescription());
    }

    @Override
    public void bindDiscoverList(List<DiscoverEntity> discoverEntityList) {
        dispatchResult(discoverEntityList);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        super.onDestroyView();
    }
}
