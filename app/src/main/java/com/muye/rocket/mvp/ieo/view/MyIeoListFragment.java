package com.muye.rocket.mvp.ieo.view;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DateTimeTool;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.ieo.IEORecord;
import com.muye.rocket.mvp.ieo.contract.IEORecordContract;
import com.muye.rocket.mvp.ieo.presenter.IEORecordPresenter;

import java.util.List;

public class MyIeoListFragment extends BaseListFragment<IEORecord> implements IEORecordContract.View {
    IEORecordContract.Presenter mPresenter;

    public static MyIeoListFragment newInstance() {
        Bundle args = new Bundle();
        MyIeoListFragment fragment = new MyIeoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, IEORecord record) {
        if (record == null) return;
        MyIeoDetailActivity.openMyIeoDetailActivity(getContext(), record.getId());
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }


    @Override
    public void onRequest(int page) {
        if (mPresenter == null) {
            mPresenter = new IEORecordPresenter(this);
        }
        mPresenter.fetchRecord(page);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_my_ieo;
    }

    @Override
    public void onBindView(ViewHolder holder, IEORecord record, int position) {
        if (record != null) {
            //币种
            holder.setText(R.id.name_text, String.format(getString(R.string.space_plan_phase_), record.getRound()));
            //购买数量
            holder.setText(R.id.buyNum_text, NumberUtil.formatMoney(record.getAmount(), 0) + record.getBuy_coin_name());
            //锁仓数量
            holder.setText(R.id.lockNum_text, NumberUtil.formatMoney(record.getLock_amount()));
            //释放总量
            holder.setText(R.id.freedTotalNum_text, NumberUtil.formatMoney(record.getRelease_amount()));
            //释放天数
            holder.setText(R.id.freedTime_text, record.getRelease_day());
            //今日释放量
            holder.setText(R.id.freedTodayNum_text, NumberUtil.formatMoney(record.getRelease_today()));
            //时间
            holder.setText(R.id.time_text, DateTimeTool.getYYYYMMDDHHMMSS(record.getInputtime()));
        } else {
            //币种
            holder.setText(R.id.name_text, String.format(getString(R.string.space_plan_phase_), "--"));
            //购买数量
            holder.setText(R.id.buyNum_text, "--");
            //锁仓数量
            holder.setText(R.id.lockNum_text, "--");
            //释放总量
            holder.setText(R.id.freedTotalNum_text, "--");
            //释放天数
            holder.setText(R.id.freedTime_text, "--");
            //今日释放量
            holder.setText(R.id.freedTodayNum_text, "--");
            //时间
            holder.setText(R.id.time_text, "--");
        }
    }

    @Override
    public void bindRecord(List<IEORecord> recordList) {
        dispatchResult(recordList);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        super.onDestroyView();
    }
}
