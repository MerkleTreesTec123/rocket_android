package com.muye.rocket.mvp.c2c.view;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.c2c.C2CReleaseEntity;
import com.muye.rocket.mvp.c2c.contract.C2CReleaseListContract;
import com.muye.rocket.mvp.c2c.contract.C2CUpdateReleaseContract;
import com.muye.rocket.mvp.c2c.presenter.C2CReleaseListPresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CUpdateReleasePresenter;
import com.muye.rocket.widget.dialog.MessageDialog;

import java.util.List;

public class MyReleaseFragment extends BaseListFragment<C2CReleaseEntity> implements C2CReleaseListContract.View, C2CUpdateReleaseContract.View {

    C2CReleaseListContract.Presenter mReleaseListPresenter;
    C2CUpdateReleaseContract.Presenter mUpdatePresenter;

    public static MyReleaseFragment newInstance() {
        Bundle args = new Bundle();
        MyReleaseFragment fragment = new MyReleaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, C2CReleaseEntity releaseEntity) {

    }

    @Override
    protected void initData() {
    }

    @Override
    public void onStart() {
        super.onStart();
//        forceRefresh();
        onRefresh();
    }

    @Override
    public void onRequest(int page) {
        if (mReleaseListPresenter == null) {
            mReleaseListPresenter = new C2CReleaseListPresenter(this);
        }
        mReleaseListPresenter.fetchReleaseList(MMKVTools.getToken(), "", "", page);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.c2c_item_my_release;
    }

    @Override
    public void onBindView(ViewHolder holder, C2CReleaseEntity releaseEntity, int position) {
        ImageView deleteButton = holder.getView(R.id.delete_button);
        SwipeMenuLayout swipeLayout = holder.getView(R.id.swipe_layout);
        ConstraintLayout contentView = holder.getView(R.id.content_container);
        TextView titleTextView = holder.getView(R.id.title_text_view);
        TextView statusTextView = holder.getView(R.id.status_text_view);
        TextView totalTextView = holder.getView(R.id.total_text_view);
        TextView priceTextView = holder.getView(R.id.price_text_view);
        TextView numTextView = holder.getView(R.id.num_text_view);
        TextView timeTextView = holder.getView(R.id.time_text_view);
        TextView offlineButton = holder.getView(R.id.submit_button);

        //总量
        String total = "<font color=\"#B7B7C4\">" + (Constant.C2C_TRANSACTION_TYPE_SELL.equals(releaseEntity.getType())
                ? getString(R.string.c2c_total_sales) : getString(R.string.c2c_total_purchase))
                + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;" + NumberUtil.formatMoney(releaseEntity.getTotalnums()) + "</font>";
        totalTextView.setText(Html.fromHtml(total));
        //单价
        String price = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_price_cny)
                + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;" + NumberUtil.formatMoney(releaseEntity.getPrice()) + "</font>";
        priceTextView.setText(Html.fromHtml(price));
        //数量
        String num = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_volume_already_traded)
                + "</font>" + "<font color=\"#292A49\">&nbsp;&nbsp;" + NumberUtil.formatMoney(releaseEntity.getTotalnums() - releaseEntity.getNums()) + "</font>";
        numTextView.setText(Html.fromHtml(num));
        //时间
        String time = "<font color=\"#B7B7C4\">" + getString(R.string.c2c_time)
                + "</font>" + "<font color=\"#292A49\"><small>" + "&nbsp;&nbsp;" + releaseEntity.getInputtime() + "</small></font>";
        timeTextView.setText(Html.fromHtml(time));
        //标题
        String title = (Constant.C2C_TRANSACTION_TYPE_SELL.equals(releaseEntity.getType())
                ? getString(R.string.c2c_publish_and_sell) : getString(R.string.c2c_publish_buy)) + "  " + releaseEntity.getCoinname();
        titleTextView.setText(title);

        if ("1".equals(releaseEntity.getStatus()) || "2".equals(releaseEntity.getStatus())) {
            statusTextView.setText(R.string.c2c_publishing);
            statusTextView.setTextColor(getResources().getColor(R.color.c2cColor8488F5));
            swipeLayout.setSwipeEnable(false);
            offlineButton.setVisibility(View.VISIBLE);
        } else if ("4".equals(releaseEntity.getStatus())) {
            statusTextView.setText(R.string.c2c_completed);
            statusTextView.setTextColor(getResources().getColor(R.color.c2cColor00DA97));
            swipeLayout.setSwipeEnable(true);
            offlineButton.setVisibility(View.VISIBLE);
        } else {
            statusTextView.setText(R.string.c2c_offlined);
            statusTextView.setTextColor(getResources().getColor(R.color.c2cColorB7B7C4));
            swipeLayout.setSwipeEnable(true);
            offlineButton.setVisibility(View.GONE);
        }

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (releaseEntity == null) return;
                MyReleaseDetailActivity.openMyReleaseDetailActivity(getContext(), releaseEntity.getId(), releaseEntity.getType());
            }
        });

        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (releaseEntity == null) return;
                showCancelDialog(releaseEntity.getId());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (releaseEntity == null) return;
                showDeleteDialog(releaseEntity.getId());
            }
        });
    }

    @Override
    public void bindReleaseList(String orderType, List<C2CReleaseEntity> entityList) {
        dispatchResult(entityList);
    }

    @Override
    public void onUpdateReleaseStatusSuccess(String status) {
        forceRefresh();
    }

    private void showCancelDialog(String id) {
        new MessageDialog(getContext())
                .setMessage(getString(R.string.c2c_offline_release_message))
                .setCancelText(getString(R.string.c2c_cancel))
                .setOkText(getString(R.string.c2c_ok))
                .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                    @Override
                    public void onOkButtonClick(MessageDialog dialog) {
                        dialog.dismiss();
                        if (mUpdatePresenter == null) {
                            mUpdatePresenter = new C2CUpdateReleasePresenter(MyReleaseFragment.this);
                        }
                        mUpdatePresenter.updateReleaseStatus(id, "5");
                    }
                }).show();
    }

    private void showDeleteDialog(String id) {
        new MessageDialog(getContext())
                .setMessage(getString(R.string.c2c_delete_release_message))
                .setCancelText(getString(R.string.c2c_cancel))
                .setOkText(getString(R.string.c2c_ok))
                .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                    @Override
                    public void onOkButtonClick(MessageDialog dialog) {
                        dialog.dismiss();
                        if (mUpdatePresenter == null) {
                            mUpdatePresenter = new C2CUpdateReleasePresenter(MyReleaseFragment.this);
                        }
                        mUpdatePresenter.updateReleaseStatus(id, "6");
                    }
                }).show();
    }

    @Override
    public void onDestroyView() {
        if (mReleaseListPresenter != null) mReleaseListPresenter.dropView();
        if (mUpdatePresenter != null) mUpdatePresenter.dropView();
        super.onDestroyView();
    }
}
