package com.muye.rocket.mvp.me.view;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DateTimeTool;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.InviteRewardDetailEntity;
import com.muye.rocket.mvp.me.contract.InviteRewardDetailContract;
import com.muye.rocket.mvp.me.presenter.InviteRewardDetailPresenter;
import com.muye.rocket.widget.dialog.DescriptionDialog;

import java.util.ArrayList;

public class CumulativeRewardListFragment extends BaseListFragment<InviteRewardDetailEntity.LogBean> implements InviteRewardDetailContract.View {


    private TextView cumulativeTotalText;
    private TextView inactivatedStatusText;
    private TextView freezeNumText;
    private TextView releasedNumText;

    private String mCoinID;
    private String mCoinName;
    private DescriptionDialog mDescriptionDialog;

    InviteRewardDetailContract.Presenter mDetailPresenter;

    public static CumulativeRewardListFragment newInstance(String coinID, String coinName) {
        Bundle args = new Bundle();
        args.putString("coinID", coinID);
        args.putString("coinName", coinName);
        CumulativeRewardListFragment fragment = new CumulativeRewardListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_invite_cumulative_reward;
    }

    @Override
    protected int getRootViewLayout() {
        return R.layout.fragment_invite_cumulative_reward;
    }


    @Override
    protected void initView(View parentView) {
        mCoinID = getArguments().getString("coinID", "");
        mCoinName = getArguments().getString("coinName", "");
        super.initView(parentView);
        TextView titleTextView = parentView.findViewById(R.id.title_text_view);
        parentView.findViewById(R.id.back_button).setOnClickListener(this);
        parentView.findViewById(R.id.description_text_view).setOnClickListener(this);
        titleTextView.setText(mCoinName);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.back_button) {
            getCurrentActivity().finish();
        } else if (v.getId() == R.id.description_text_view) {
            showDescriptionDialog();
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, InviteRewardDetailEntity.LogBean s) {

    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public View getHeaderView() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_invite_cumulative_reward, null);
        cumulativeTotalText = headerView.findViewById(R.id.cumulativeTotal_text);
        inactivatedStatusText = headerView.findViewById(R.id.inactivatedStatus_text);
        freezeNumText = headerView.findViewById(R.id.freezeNum_text);
        releasedNumText = headerView.findViewById(R.id.releasedNum_text);
        return headerView;
    }

    @Override
    public void onRequest(int page) {
        if (mDetailPresenter == null) {
            mDetailPresenter = new InviteRewardDetailPresenter(this);
        }
        mDetailPresenter.fetchInviteReward(mCoinID, page);
    }


    @Override
    public void onBindView(ViewHolder holder, InviteRewardDetailEntity.LogBean logBean, int position) {
        if (logBean != null) {
            //标题
            holder.setText(R.id.title_text, logBean.getNote());
            //时间
            holder.setText(R.id.time_text, DateTimeTool.getYYYYMMDDHHMMSS(logBean.getInputtime()));
            //数量
            holder.setText(R.id.num_text, NumberUtil.formatMoney(logBean.getAmount()));
        } else {
            //标题
            holder.setText(R.id.title_text, "--");
            //时间
            holder.setText(R.id.time_text, "--");
            //数量
            holder.setText(R.id.num_text, "--");
        }
    }

    @Override
    public void bindInviteReward(InviteRewardDetailEntity detailEntity) {
        InviteRewardDetailEntity.RewardBean rewardBean = null;
        if (detailEntity != null) {
            initDescriptionDialog();
            mDescriptionDialog.setContent(detailEntity.getContent());
            rewardBean = detailEntity.getReward();
            dispatchResult(detailEntity.getLog());
        } else {
            initDescriptionDialog();
            mDescriptionDialog.setContent("");
            dispatchResult(new ArrayList<>());
        }

        if (rewardBean != null) {
            //累计总额
            cumulativeTotalText.setText(NumberUtil.formatMoney(rewardBean.getTotal(), 2));
            //是否激活
            if (rewardBean.getStatus().contains("未")) {
                inactivatedStatusText.setSelected(false);
            } else {
                inactivatedStatusText.setSelected(true);
            }
            inactivatedStatusText.setText(rewardBean.getStatus());
            //冻结
            freezeNumText.setText(NumberUtil.formatMoney(rewardBean.getFrozen(), 4));
            //已释放
            releasedNumText.setText(NumberUtil.formatMoney(rewardBean.getRelease_total(), 4));
        } else {
            //累计总额
            cumulativeTotalText.setText("--");
            //是否激活
            inactivatedStatusText.setSelected(false);
            inactivatedStatusText.setText("--");
            //冻结
            freezeNumText.setText("--");
            //已释放
            releasedNumText.setText("--");
        }
    }

    private void initDescriptionDialog() {
        if (mDescriptionDialog == null) {
            mDescriptionDialog = new DescriptionDialog(getContext());
        }
    }

    private void showDescriptionDialog() {
        initDescriptionDialog();
        if (!mDescriptionDialog.isShowing()) {
            mDescriptionDialog.show();
        }
    }

    @Override
    public void onDestroyView() {
        if (mDescriptionDialog != null && mDescriptionDialog.isShowing()) {
            mDescriptionDialog.dismiss();
        }
        if (mDetailPresenter != null) {
            mDetailPresenter.dropView();
        }
        super.onDestroyView();
    }
}
